package frontend.forms

/*
 * Created by Jackson Woodruff on 19/10/2014 
 *
 *
 * This is the holder for the data that comes out of the form
 */

case class GraphFormParser(xAxis: String, yAxis: String, title: String, graphType: String,
                           xAxisTitle: String, yAxisTitle: String, graphSortMode: String,
                           regression: String) extends FormData[GraphFormParser] {
  def this() = this("", "", "", "", "", "", "", "")
  def default = new GraphFormParser

  /*
   * Note that the regression is not included here.
   *
   * That is because at the time of writing this list is used exclusively for filtering
   * out the -- None -- values (i.e. things that should not be drawn -- where the user has not
   * selected anything.
   *
   * I don't want to filter on the regression, so it is not included in this list.
   */
  def toList = List(xAxis, yAxis, title, graphType, xAxisTitle, yAxisTitle, graphSortMode)
}
