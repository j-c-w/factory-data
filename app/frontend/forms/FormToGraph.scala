package frontend.forms

import java.io.File

import backend.scala.datatypes.options.{DayOfWeekOption, MathComparable}
import backend.scala.datatypes.{DoubleOptionDataField, DataField, DataType, LineListObject}
import backend.scala.graphing.data.DataParser
import backend.scala.graphing.regressions.{Regression, RegressionGenerator}
import backend.scala.graphing.{BarChartData, Graph}
import backend.scala.query.ResultListObject
import play.api.cache.Cache
import play.api.Play.current
import scala.concurrent._
import scala.concurrent.duration.Duration.Inf
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.util.Try


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
   * this takes a list of graph forms and then returns a
   * boolean indicating whether we are plotting a graph.
   */
  def formsToGraph(forms: List[GraphFormParser], data: Future[List[ResultListObject[LineListObject]]],
                   saveString: String) = {
    if (forms.length == 0) {
      false
    } else {
      val graphDrawing = future {
        val title = forms.head.title
        val graphType = forms.head.graphType
        val xAxisTitle = forms.head.xAxisTitle
        val yAxisTitle = forms.head.yAxisTitle
        val regressions = forms.map {
          x => RegressionGenerator.fromString(x.regression, x.yAxis)
        }
        val parsers = forms.map(form => {
          val xAx = DataField.fromString(form.xAxis)
          val yAx = DataField.fromString(form.yAxis).asInstanceOf[DoubleOptionDataField]
          new DataParser[Comparable[_], LineListObject](
            data, result => {
              (xAx.get(result.lineObject), yAx.get(result.lineObject).getOrElse(0))
            }, (result: ResultListObject[LineListObject]) => {
              !(xAx.get(result.lineObject).isNone || yAx.get(result.lineObject).isNone)
            }, generateSort(forms.head.graphSortMode), form.yAxis
          )
        })
        drawChart(new BarChartData(parsers.toList), title, graphType, xAxisTitle, yAxisTitle, regressions, saveString)
      }

      graphDrawing onFailure {
        case t =>
          printf("Error drawing graph. Stacktrace: ")
          printf(t.getMessage)
          t.printStackTrace()
          // Just like in the case of the data failure, we need to make
          // sure that the user is not just left with a spinning bar.
          Cache.set(saveString, true, 3600)
      }
      true
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
  def formToGraph(form: GraphFormParser, data: Future[List[ResultListObject[LineListObject]]],
                  saveString: String): String = {
    val xAxis = DataField.fromString(form.xAxis)
    //I think I am allowed to type-cast this because there should only be double options coming
    //through
    val yAxis: DoubleOptionDataField = DataField.fromString(form.yAxis).asInstanceOf[DoubleOptionDataField]

    val parser = new DataParser[Comparable[_], LineListObject](
      data, x => (xAxis.get(x.lineObject), yAxis.get(x.lineObject).getOrElse(0.0)),
      x => !(xAxis.get(x.lineObject).isNone || yAxis.get(x.lineObject).isNone), generateSort(form.graphSortMode), form.title)
    val chartData = new BarChartData(List(parser))
    drawChart(chartData, form.title, form.graphType, form.xAxis,
      form.yAxis, List(RegressionGenerator.fromString(form.regression, form.yAxis)), saveString)
  }

  private def drawChart[A <: Comparable[_], T <: DataType[T]](data: BarChartData[A, T], title: String,
                                                              graphType: String, xAxisTitle: String,
                                                              yAxisTitle: String, regression: List[Regression],
                                                              saveString: String): String = graphType match {
    case "Bar Chart" => Graph.drawBarChart(data, title, xAxisTitle, yAxisTitle, saveString)
    case "Line Graph" => Graph.drawLineGraph(data, title, xAxisTitle, yAxisTitle, saveString)
    case "Scatter Plot" => Graph.drawScatterPlot(data, title, xAxisTitle, yAxisTitle, regression.toArray, saveString)
  }

  private def generateSort[A <: Comparable[_]](sortMode: String): (((A, Double), (A, Double)) => Boolean) = sortMode match {
    case "xAxis Ascending" => {case ((x1, _), (x2, _)) =>
      // Should speed become an issue, this might be somewhere to look.
      // This check really does not have to be done every time the sort is done..
      // if we know that x1 or x2 is not a DayOfWeek, then there is not reason to check every time
      val (dow1, dow2) = (DayOfWeekOption.fromDayString(x1.toString), DayOfWeekOption.fromDayString(x2.toString))
      if (!(dow1.isNone && dow2.isNone)) {
        dow1.compareTo(dow2) < 0
      } else
      //we need to try sorting as numbers first, because if the values passed are indeed numbers,
      //then the string sort really doesn't cut it at all
      //I don't want to do this with a match statement, because that
      //would mean trying for both integers and doubles, whereas this is
      //much shorter (and cleaner?)
      Try (x1.toString.toDouble < x2.toString.toDouble).getOrElse(x1.toString.compareTo(x2.toString()) < 0)
    }
    case "xAxis Descending" => {
      case ((x1, _), (x2, _)) => {
        // Should speed become an issue, this might be somewhere to look.
        // This check really does not have to be done every time the sort is done..
        // if we know that x1 or x2 is not a DayOfWeek, then there is not reason to check every time
        val (dow1, dow2) = (DayOfWeekOption.fromDayString(x1.toString), DayOfWeekOption.fromDayString(x2.toString))
        if (!(dow1.isNone && dow2.isNone)) {
          dow1.compareTo(dow2) > 0
        } else
          Try(x1.toString.toDouble > x2.toString.toDouble).getOrElse(x1.toString.compareTo(x2.toString()) > 0)
      }
    }
    case "No Sort" => ((_, _) => false)
    case "yAxis Ascending" => {case ((_, y1), (_, y2)) => y1 < y2}
    case "yAxis Descending" => {case ((_, y1), (_, y2)) => y1 > y2}
  }

}
