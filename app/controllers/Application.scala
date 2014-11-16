package controllers

import backend.scala.Backend
import backend.scala.datatypes.{DataField, OperatorsAbsent, LineListObject}
import backend.scala.query.{ResultListObject, FilterBuilder, QueryBuilder, NoAggregate}
import frontend.{FilterParser, ComparisonMethod, Equals}
import frontend.forms._
import play.api._
import play.api.cache.Cache
import play.api.data.{Form, Field}
import play.api.mvc._
import play.api.Play.current
import scala.concurrent.future
import java.io.File
import scala.concurrent.ExecutionContext.Implicits.global


import scala.util.{Success, Failure, Try}

object Application extends Controller {
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def moreData(sessionId: String) = Action {
    val possiblyData = Cache.getAs[Array[ResultListObject[LineListObject]]](sessionId)
    val (data, message) = possiblyData.map(_.toList) match {
      case None => (Nil, "Query Expired, Please re-run query")
      case Some(isData) => (isData, "Data OK")
    }
    Ok(views.html.generic.dataDisplay(data.toArray, message, sessionId))
  }

  def load(formType: String) = Action {
    formType match {
      case "graph" => Ok(views.html.formViews.graphForm(false, None))
      case "filter" => Ok(views.html.formViews.filterForm(true, None))
      case "sort" => Ok(views.html.formViews.sortForm(None))
      case _ => Ok(views.html.formViews.aggregateForm(None))
    }
  }

  def submitForm = Action { implicit request =>
    val dynamicForm = request.body.asFormUrlEncoded
    println(dynamicForm)
    //Now we parse that and turn it into a form parser
    val dataForm = fromMapToData(dynamicForm getOrElse {
      throw new NullPointerException("Bad Request")
    })

    val sessionId = Global.getSessionId.mkString("")
    val (filter, sort, aggregate, graph) = dataForm
    println("Building Query")
    val queryBuilder = FormToQuery.parse((filter, sort, aggregate))
    println("Query Built")
    val data = future {
      val processedData = queryBuilder.processData(Global.baseData)
      println("Data Processed")
      Cache.set(sessionId, processedData.toArray, 3600)
      println("data in cache under session id " + sessionId)
      Global.sendNotification(sessionId)
      processedData
    }
    println("Drawing Graph")
    val file = FormToGraph.formsToGraph(graph, data)
    println("Finished drawing graph")

    Ok(views.html.dataView(sessionId, Static.tableHeaders, dataForm, file))
  }

  def list = Action {
    val sessionId = Global.getSessionId.mkString("")
    Cache.set(sessionId, Backend.loadRaw, 3600)
    Global.sendNotification(sessionId)
    Ok(views.html.dataView(sessionId, Static.tableHeaders, (List(), List(), List(), List()), new File("")))
  }

  def endOfQuery = Action {
    Ok(views.html.generic.endOfQuery("End of Query"))
  }

  def dataStart(sessionId: String) = Action {
    val data = Cache.get(sessionId).asInstanceOf[Option[Array[ResultListObject[LineListObject]]]]
    data match {
      case None => Ok(views.html.generic.endOfQuery("Query Expired, please re-run your query"))
      case Some(x) => Ok(views.html.generic.lazyList(x, sessionId))
    }

  }

  /*
   * From this point onwards, these are no longer routes, but really
   * important methods that I don't want to get lost in the mix
   */

  /*
  * This takes a map and returns a tuple
  */
  def fromMapToData(map: Map[String, Seq[String]]): (List[FilterFormData], List[SortFormData], List[AggregateFormData], List[GraphFormParser]) = {
    val filterComparisons = map.getOrElse("filterComparison", List())
    val filterField = map.getOrElse("filterField", List())
    val filterValue = map.getOrElse("filterValue", List())
    val filterConnectors = (List("And") ++ map.getOrElse("filterConnector", List())).toSeq

    val sortField = map.getOrElse("sortField", List())
    val sortMode = map.getOrElse("sortMode", List())

    val aggregateField = map.getOrElse("aggregateField", List())
    val aggregateMode = map.getOrElse("aggregateMode", List())

    val xAxis = map.getOrElse("xAxis", List())
    val yAxis = map.getOrElse("yAxis", List())
    val graphType = map.getOrElse("graphType", List("Bar Chart"))
    val graphTitle = map.getOrElse("graphTitle", List("Error"))
    val xAxisTitle = map.getOrElse("xAxisTitle", List("Error"))
    val yAxisTitle = map.getOrElse("yAxisTitle", List("Error"))
    val graphSortMode = map.getOrElse("graphSortMode", List("xAxis"))

    val graphData = (xAxis, yAxis).zipped.map{
      case (x, y) => new GraphFormParser(x, y, graphTitle.head, graphType.head, xAxisTitle.head, yAxisTitle.head, graphSortMode.head)
    }.filter(!_.toList.contains(Static.noSelection))
    val filters = filterComparisons.zip(filterField).zip(filterValue).zip(filterConnectors).map(
    {case (((comparator, field), value), connector) => new FilterFormData(field, comparator, value, connector)}
    ).filter(!_.toList.contains(Static.noSelection))
    val sorters = (sortField, sortMode).zipped.map(
    {case (field, mode) => new SortFormData(field, mode)}
    ).filter(!_.toList.contains(Static.noSelection))
    val aggregators = (aggregateField, aggregateMode).zipped.map(
    {case (field, mode) => new AggregateFormData(field, mode)}
    ).filter(!_.toList.contains(Static.noSelection))

    (filters.toList, sorters.toList, aggregators.toList, graphData.toList)
  }
}