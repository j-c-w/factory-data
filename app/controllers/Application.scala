package controllers

import backend.scala.Backend
import backend.scala.datatypes.{DataField, LineListObject}
import backend.scala.graphing.regressions.RegressionGenerator
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
    Ok(views.html.main())
  }

  def tempFile(file: String) = Action {
      Results.NotFound("File not found")
  }

  def dataLoaded(queryId: String) = Action {
    val cacheContents = Cache.get(queryId)
    cacheContents match {
      case None => {
        println("Data Not in cache yet")
        Results.NotFound("Cache not loaded data")
      }
      case Some(_) => {
        println("Data found in cache -- loading")
        Ok("Data in cache")
      }
    }
  }

  def moreData(queryId: String) = Action {
    val displayFields = Cache.getOrElse(queryId + "DisplayFields")(Static.defaultFields)
    val possiblyData = Cache.getAs[Array[ResultListObject[LineListObject]]](queryId)
    val position = Cache.getOrElse(queryId + "Position")(0)
    val (data, message) = possiblyData.map(_.toList) match {
      case None => (Nil, "Query Expired, Please re-run query")
      case Some(isData) => (isData, "Data OK")
    }
    Ok(views.html.generic.dataDisplay(data.toArray, displayFields, message, queryId, position))
  }

  def load(formType: String) = Action {
    formType match {
      case "graph" => Ok(views.html.formViews.graphForm(false, None))
      case "column" => Ok(views.html.formViews.fieldSelect(None))
      case "filter" => Ok(views.html.formViews.filterForm(true, None))
      case "sort" => Ok(views.html.formViews.sortForm(None))
      case _ => Ok(views.html.formViews.aggregateForm(None))
    }
  }

  def help() = Action {
    Ok(views.html.helpPage())
  }

  /*
   * This method returns an Ok if the cahce contains the key passed
   * Otherwise it returns a NotFound result
   */
  def cacheCheck(key: String) = Action {
    Cache.get(key) match {
      case None => Results.NotFound(key + " Not found in cache")
      case Some(_) => Ok("Key in cache")
    }
  }

  def loadImageFromCache(key: String) = Action {
    val image = Cache.getAs[String](key)
    Cache.remove(key)
    image match {
      case Some(x) => Ok(views.html.generic.imageDisplay(x))
      case None => Results.NotFound("No Base 64 encoded image found under " + key)
    }
  }

  def recoverSession() = Action { implicit request =>
    val sentForm = request.body.asFormUrlEncoded
    val queryId = sentForm.getOrElse(Map()).getOrElse("id", List("")).head
    //note we trim the query id here in case someone copied a space at
    //either end of it
    val dynamicForm = Global.restoreSession(queryId.trim)
    println("Restoring...")
    //note how we need a new SessionID so we don't run into
    //loading problems
    dynamicForm match {
      case Some(_) => loadDataPage(dynamicForm)
      //in the case that the restore failed this will have returned None,
      //so we return to the main page.
      case None => Ok(views.html.main())
    }
  }

  def submitForm = Action { implicit request =>
    val dynamicForm = request.body.asFormUrlEncoded
    println(dynamicForm)
    //Now we parse that and turn it into a form parser
    loadDataPage(dynamicForm)
  }

  private def loadDataPage(dynamicForm: Option[Map[String, Seq[String]]]) = {
    val dataForm = fromMapToData(dynamicForm getOrElse {
      throw new NullPointerException("Bad Request")
    })

    val queryId = Global.getQueryId.mkString("")
    val (filter, sort, aggregate, graph, displayAxes) = dataForm
    //after we extract the data, we have to remove the non-entered data values
    //for parsing by the rest of the program at this point
    val filteredFilter = filter.filter(!_.toList.contains(Static.noSelection))
    // Note that sorters are revered to make the sort order make sense form a user perspective.
    val filteredSort = sort.filter(!_.toList.contains(Static.noSelection)).reverse
    val filteredAggregate = aggregate.filter(!_.toList.contains(Static.noSelection))
    val filteredGraph = graph.filter(!_.toList.contains(Static.noSelection))
    val filteredDisplayAxes = displayAxes.filter(!_.equals(Static.noSelection))

    println("Building Query")
    val queryBuilder = FormToQuery.parse((filteredFilter, filteredSort, filteredAggregate))
    println("Query Built")

    val data = future {
      val processedData = queryBuilder.processData(Global.baseData)
      println("Data Processed")
      Cache.set(queryId, processedData.toArray, 3600)
      println("data in cache under session id " + queryId)
      Cache.set(queryId + "DisplayFields", filteredDisplayAxes, 3600)
      println("fields in cache under session id " + queryId)
      //we need to pass the dataForm so that the
      //global can put a serialized version of it in a file
      //that way, given a session ID, we can recover the query
      //Global.sendNotification(queryId, dynamicForm)
      processedData
    }
    data onFailure {
      //we need to send the notification anyways, but since we put a boolean in the cache
      //we know the query failed
      case t => {
        Cache.set(queryId, false, 3600)
        Global.sendNotification(queryId, dynamicForm)
        println("Query " + queryId + " Failed")
        println("Error Message: " + t.getMessage)
        println("Stacktrace: ")
        t.printStackTrace()
      }
    }
    println("Drawing Graph")
    val hasGraph = FormToGraph.formsToGraph(filteredGraph, data)
    println("Finished drawing graph")

    //note that we are passing the un=filtered data back to the
    //layout so people don't get confused when things dissapear
    Ok(views.html.dataView(queryId, Static.tableHeaders, dataForm, hasGraph))
  }

  def list = Action {
    val queryId = Global.getQueryId.mkString("")
    val fut = future {
      Cache.set(queryId, Backend.loadRaw, 3600)
      //Global.sendNotification(queryId, Some(Map()))
    }
    fut.onFailure {
      case t => {
        println("Error loading data, Stacktrace: ")
        t.printStackTrace
        // We set a string here becuase that will trigger the error mechanism
        // rather than just letting the javascript spin endlessly.
        Cache.set(queryId, "Query Failed", 180)
      }
    }
    Ok(views.html.dataView(queryId, Static.tableHeaders, (List(), List(), List(), List(), Static.defaultFields), ""))
  }

  def endOfQuery = Action {
    Ok(views.html.generic.endOfQuery("End of Query"))
  }

  def dataStart(queryId: String) = Action {
    val data = Cache.get(queryId)
    val displayFields = Cache.getOrElse(queryId + "DisplayFields")(Static.defaultFields)
    try {
      val convertedData = data.asInstanceOf[Option[Array[ResultListObject[LineListObject]]]]
      convertedData match {
        case None => Ok(views.html.generic.endOfQuery("Query Expired, please re-run your query"))
        case Some(x) => Ok(views.html.generic.lazyList(x, displayFields, queryId))
      }
    } catch {
      case e: Exception => Ok(views.html.generic.endOfQuery("Error -- Query failed"))
    }
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
      case "Scatter Plot" => xAxisDoubles
    }
    val graphTitle = map.getOrElse("graphTitle", List(""))
    val xAxisTitle = map.getOrElse("xAxisTitle", List(""))
    val yAxisTitle = map.getOrElse("yAxisTitle", List(""))
    val graphSortMode = map.getOrElse("graphSortMode", List("xAxis"))
    val regressions = map.getOrElse("regressions", List(""))
    //

    val axesDisplayed = map.getOrElse("displayAxes", Static.defaultFields)

    val graphData = (xAxis, yAxis, regressions).zipped.map{
      case (x, y, regression) => new GraphFormParser(x, y, graphTitle.head, graphType.head,
        xAxisTitle.head, yAxisTitle.head, graphSortMode.head, regression)//this last item is the regression
        //which is currently not in use
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