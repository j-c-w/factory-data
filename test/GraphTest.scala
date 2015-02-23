import backend.scala.datatypes.LineListObject
import backend.scala.query.NoAggregate
import controllers.Global
import frontend.forms.{FormToGraph, GraphFormParser}

import scala.concurrent.Future

/*
 * Created by Jackson Woodruff on 22/10/2014 
 *
 *
 * Note that this class does not extend the normal Specification
 * class because it doesn't really create comparable objects.
 * They have to be looked at to verify them.
 */

object GraphTest {
  def main(args: Array[String]) {
    draw()
  }

  def draw() = {
    val futureData = Future {
      new NoAggregate().aggregate(Global.baseData)
    }
    val parser = new GraphFormParser("Line Code", "Total Production Workers",
      "Line Code vs Total Production Workers", "LineGraph", "xAxis Title", "yAxis Title", "xAxis Ascending", "No Regression")
    FormToGraph.formToGraph(parser, futureData)
  }
}
