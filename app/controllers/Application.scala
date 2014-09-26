package controllers

import backend.scala.Backend
import frontend.forms.{SearchFormParser, DataManipulationForm}
import play.api._
import play.api.data.{Form, Field}
import play.api.mvc._

import scala.util.{Success, Failure, Try}

object Application extends Controller {
  private val tableHeaders: List[String] = List(
    "Factory Code", "Line Code", "Date", "Total Operators",
    "Operators Present", "Operators Absent", "Operators on Leave",
    "Percent Operators Absent", "Total Helpers", "Helpers Present",
    "Helpers Absent", "Helpers on Leave", "Percent Helpers Absent"
  )

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def submitForm = Action { /*implicit request =>
    val filledFormTry = Try(DataManipulationForm.form.bindFromRequest.get)
    val filledForm = filledFormTry match {
      case Failure(fail) => new SearchFormParser
      case Success(parser) => parser
    }*/
    Ok(views.html.statsTestView(Backend.loadRaw))
    //Ok(views.html.dataView(Backend.loadRaw, tableHeaders, Form(DataManipulationForm.filterForm)))
  }

  def list = Action {
    Ok(views.html.dataView(Backend.loadRaw, tableHeaders, Form(DataManipulationForm.filterForm)))
  }

}