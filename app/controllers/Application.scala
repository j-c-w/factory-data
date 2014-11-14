package controllers

import backend.scala.Backend
import backend.scala.datatypes.{DataField, OperatorsAbsent, LineListObject}
import backend.scala.query.{ResultListObject, FilterBuilder, QueryBuilder, NoAggregate}
import frontend.{FilterParser, ComparisonMethod, Equals}
import frontend.forms.{FormToQuery, FilterFormData, SearchFormParser, DataManipulationForm}
import play.api._
import play.api.cache.Cache
import play.api.data.{Form, Field}
import play.api.mvc._
import play.api.Play.current


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
      case "filter" => Ok(views.html.formViews.filterForm())
      case "sort" => Ok(views.html.formViews.sortForm())
      case _ => Ok(views.html.formViews.aggregateForm())
    }
  }

  def submitForm = Action { implicit request =>
    //this first gets the form from the request
    val filledFormTry = Try(
      DataManipulationForm.form.bindFromRequest.get)

    //this then takes the form out of the monad
    //and if there was a problem resets it
    val submittedForm: SearchFormParser = filledFormTry match {
      case Success(form) => form
      case Failure(failureMessage) => new SearchFormParser
    }

    //finally, this creates the query using the FormToQuery calss
    val query = FormToQuery.wholeForm(submittedForm)
    //and then this pushes it to the view -- success!!
    Ok(views.html.dataView(query.processData(Global.baseData).toArray, Static.tableHeaders, DataManipulationForm.form))
  }

  def list = Action {
    Ok(views.html.dataView(Backend.loadRaw, Static.tableHeaders, DataManipulationForm.form))
  }

}