package controllers

import backend.scala.Backend
import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def list = Action {
    Ok(Backend.loadData() mkString "\n")
  }

}