package frontend.forms

import java.io.File

import backend.scala.datatypes.options.MathComparable
import backend.scala.datatypes.{DoubleOptionDataField, DataField, DataType, LineListObject}
import backend.scala.graphing.data.DataParser
import backend.scala.graphing.{BarChartData, Graph}
import backend.scala.query.ResultListObject


/*
 * Created by Jackson Woodruff on 19/10/2014 
 *
 *
 * This is a helper object used to build up a
 * graph when given some data and a filled out
 * form.
 */

object FormToGraph {

  /*
   * this takes a list of graph forms and then returns a single graph file
   * with all of those lines plotted
   */
  def formToGraph(forms: List[GraphFormParser], data: List[ResultListObject[LineListObject]]): File = {
    if (forms.length == 0) {
      new File("No Data")//return something because nothing was passed
    } else {
      val title = forms.head.title
      val graphType = forms.head.graphType
      val xAxisTitle = forms.map(_.xAxis).mkString("/")
      val yAxisTitle = forms.map(_.yAxis).mkString("/")
      val parsers = forms.map(form => {new DataParser[Comparable[_], LineListObject](
        data, result => {
          val xAx = DataField.fromString(form.xAxis)
          val yAx = DataField.fromString(form.yAxis).asInstanceOf[DoubleOptionDataField]
          (xAx.get(result.lineObject), yAx.get(result.lineObject).getOrElse(0))
        }, form.xAxis + "/" + form.yAxis
      )})
      drawChart(new BarChartData(parsers.toList), title, graphType, xAxisTitle, yAxisTitle)
    }
  }
  /*
   * This takes a form of the graph data and a bunch of
   * data to be plotted, then draws the graph using
   * all this info.
   *
   * Finally it returns a File object that represents the
   * location of the drawn graph.
   */
  def formToGraph(form: GraphFormParser, data: List[ResultListObject[LineListObject]]): File = {
    val xAxis = DataField.fromString(form.xAxis)
    //I think I am allowed to type-cast this because there should only be double options coming
    //through
    val yAxis: DoubleOptionDataField = DataField.fromString(form.yAxis).asInstanceOf[DoubleOptionDataField]

    val parser = new DataParser[Comparable[_], LineListObject](
      data, x => (xAxis.get(x.lineObject), yAxis.get(x.lineObject).getOrElse(0.0)), form.title)
    val chartData = new BarChartData(List(parser))
    drawChart(chartData, form.title, form.graphType, form.xAxis, form.yAxis)
  }

  private def drawChart[A <: Comparable[_], T <: DataType[T]](data: BarChartData[A, T], title: String,
                                                              graphType: String, xAxisTitle: String,
                                                              yAxisTitle: String): File = graphType match {
    case "Bar Chart" => Graph.drawBarChart(data, title, xAxisTitle, yAxisTitle)
    case "Line Graph" => Graph.drawLineGraph(data, title, xAxisTitle, yAxisTitle)
  }


}
