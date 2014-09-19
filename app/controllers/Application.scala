package controllers

import backend.scala.Backend
import play.api._
import play.api.mvc._

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

  def submitForm = Action {
    Ok(views.html.dataView(Backend.loadRaw, tableHeaders))
  }

  def list = Action {
    Ok(views.html.dataView(Backend.loadRaw, tableHeaders))
  }

}