package frontend.forms

/*
 * Created by Jackson Woodruff on 19/10/2014 
 *
 *
 * This is the holder for the data that comes out of the form
 */

case class GraphFormParser(xAxis: String, yAxis: String, title: String, graphType: String) extends FormData[GraphFormParser] {
  def this() = this("", "", "", "")
  def default = new GraphFormParser

  def toList = List(xAxis, yAxis, title, graphType)
}
