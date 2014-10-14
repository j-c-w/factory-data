package controllers

import backend.scala.Backend
import backend.scala.datatypes.{DataField, OperatorsAbsent, LineListObject}
import backend.scala.query.{FilterBuilder, QueryBuilder, NoAggregate}
import frontend.{FilterParser, ComparisonMethod, Equals}
import frontend.forms.{FormToQuery, FilterFormData, SearchFormParser, DataManipulationForm}
import play.api._
import play.api.data.{Form, Field}
import play.api.mvc._

import scala.util.{Success, Failure, Try}

object Application extends Controller {
  val tableHeaders: List[String] = List(
    "Factory Code", "Line Code", "Date", "Total Operators",
    "Operators Present", "Operators Absent", "Operators on Leave",
    "Percent Operators Absent", "Total Helpers", "Helpers Present",
    "Helpers Absent", "Helpers on Leave", "Percent Helpers Absent"
  )

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
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
    Ok(views.html.dataView(query.processData(Global.baseData).toArray, tableHeaders, Form(DataManipulationForm.filterForm)))
  }

  def list = Action {
    Ok(views.html.dataView(Backend.loadRaw, tableHeaders, Form(DataManipulationForm.filterForm)))
  }

}