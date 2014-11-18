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
    val displayFields = Cache.getOrElse(sessionId + "DisplayFields")(Static.defaultFields)
    val possiblyData = Cache.getAs[Array[ResultListObject[LineListObject]]](sessionId)
    val (data, message) = possiblyData.map(_.toList) match {
      case None => (Nil, "Query Expired, Please re-run query")
      case Some(isData) => (isData, "Data OK")
    }
    Ok(views.html.generic.dataDisplay(data.toArray, displayFields, message, sessionId))
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
    val (filter, sort, aggregate, graph, displayAxes) = dataForm
    //after we extract the data, we have to remove the non-entered data values
    //for parsing by the rest of the program at this point
    val filteredFilter = filter.filter(!_.toList.contains(Static.noSelection))
    val filteredSort = sort.filter(!_.toList.contains(Static.noSelection))
    val filteredAggregate = aggregate.filter(!_.toList.contains(Static.noSelection))
    val filteredGraph = graph.filter(!_.toList.contains(Static.noSelection))
    val filteredDisplayAxes = displayAxes.filter(!_.equals(Static.noSelection))

    println("Building Query")
    val queryBuilder = FormToQuery.parse((filteredFilter, filteredSort, filteredAggregate))
    println("Query Built")

    val data = future {
      val processedData = queryBuilder.processData(Global.baseData)
      println("Data Processed")
      Cache.set(sessionId, processedData.toArray, 3600)
      println("data in cache under session id " + sessionId)
      Cache.set(sessionId + "DisplayFields", filteredDisplayAxes, 3600)
      println("fields in cache under session id " + sessionId)
      Global.sendNotification(sessionId)
      processedData
    }
    data onFailure {
      //we need to send the notification anyways, but since we put a boolean in the cache
      //we know the query failed
      case t => {
        Cache.set(sessionId, false, 3600)
        Global.sendNotification(sessionId)
        println("Query " + sessionId + " Failed")
        println("Error Message: " + t.getMessage)
      }
    }
    println("Drawing Graph")
    val file = FormToGraph.formsToGraph(filteredGraph, data)
    println("Finished drawing graph")

    //note that we are passing the un=filtered data back to the
    //layout so people don't get confused when things dissapear
    Ok(views.html.dataView(sessionId, Static.tableHeaders, dataForm, file))
  }

  def list = Action {
    val sessionId = Global.getSessionId.mkString("")
    Cache.set(sessionId, Backend.loadRaw, 3600)
    Global.sendNotification(sessionId)
    Ok(views.html.dataView(sessionId, Static.tableHeaders, (List(), List(), List(), List(), Static.defaultFields), new File("")))
  }

  def endOfQuery = Action {
    Ok(views.html.generic.endOfQuery("End of Query"))
  }

  def dataStart(sessionId: String) = Action {
    val data = Cache.get(sessionId)
    val displayFields = Cache.getOrElse(sessionId + "DisplayFields")(Static.defaultFields)
    try {
      val convertedData = data.asInstanceOf[Option[Array[ResultListObject[LineListObject]]]]
      convertedData match {
        case None => Ok(views.html.generic.endOfQuery("Query Expired, please re-run your query"))
        case Some(x) => Ok(views.html.generic.lazyList(x, displayFields, sessionId))
      }
    } catch {
      case e: Exception => Ok(views.html.generic.endOfQuery("Error -- Query failed"))
    }
  }

  def deleteConformationFile(sessionId: String) = Action {
    val deletionFile = new File(Global.getPictureFile + "/" + sessionId)
    Ok(views.html.generic.endOfQuery(deletionFile.delete.toString))
  }

  /*
   * From this point onwards, these are no longer routes, but really
   * important methods that I don't want to get lost in the mix
   */

  /*
  * This takes a map and returns a tuple
  */
  def fromMapToData(map: Map[String, Seq[String]]):
  (List[FilterFormData], List[SortFormData], List[AggregateFormData], List[GraphFormParser], List[String]) = {
    val filterComparisons = map.getOrElse("filterComparison", List())
    val filterField = map.getOrElse("filterField", List())
    val filterValue = map.getOrElse("filterValue", List())
    val filterConnectors = (List("And") ++ map.getOrElse("filterConnector", List())).toSeq

    val sortField = map.getOrElse("sortField", List())
    val sortMode = map.getOrElse("sortMode", List())

    val aggregateField = map.getOrElse("aggregateField", List())
    val aggregateMode = map.getOrElse("aggregateMode", List())

    val xAxisAll = map.getOrElse("xAxisAll", List())
    val xAxisDoubles = map.getOrElse("xAxisDoubles", List())
    val yAxis = map.getOrElse("yAxis", List())
    val graphType = map.getOrElse("graphType", List("Bar Chart"))
    val xAxis = graphType.head match {
      case "Bar Chart" => xAxisAll
      case "Line Graph" => xAxisDoubles
    }
    val graphTitle = map.getOrElse("graphTitle", List(""))
    val xAxisTitle = map.getOrElse("xAxisTitle", List(""))
    val yAxisTitle = map.getOrElse("yAxisTitle", List(""))
    val graphSortMode = map.getOrElse("graphSortMode", List("xAxis"))

    val axesDisplayed = map.getOrElse("displayAxes", Static.defaultFields)

    val graphData = (xAxis, yAxis).zipped.map{
      case (x, y) => new GraphFormParser(x, y, graphTitle.head, graphType.head, xAxisTitle.head, yAxisTitle.head, graphSortMode.head)
    }
    val filters = filterComparisons.zip(filterField).zip(filterValue).zip(filterConnectors).map(
    {case (((comparator, field), value), connector) => new FilterFormData(field, comparator, value, connector)}
    )
    val sorters = (sortField, sortMode).zipped.map(
    {case (field, mode) => new SortFormData(field, mode)}
    )
    val aggregators = (aggregateField, aggregateMode).zipped.map(
    {case (field, mode) => new AggregateFormData(field, mode)}
    )

    (filters.toList, sorters.toList, aggregators.toList, graphData.toList, axesDisplayed.toList)
  }
}