package backend.scala.graphing

import java.io.File

import backend.java.utils.FileUtility
import backend.java.{LineGraph, BarChart}
import backend.scala.datatypes.DataType
import backend.scala.graphing.regressions.{RegressionGenerator}
import controllers.Global
import org.jfree.chart.{ChartUtilities, ChartFactory, JFreeChart}
import play.api.cache.Cache
import scala.concurrent.future
import scala.actors.threadpool.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current

//import org.jfree.chart.JFreeChart

//import backend.scala.graphing.GraphDataSet
import backend.scala.query.ResultListObject


/*
 * Created by Jackson Woodruff on 23/07/2014 
 *
 *
 * Takes a list of ResultListObject[DataType[T]] and two
 * functions (x axis values and y axis values) and converts
 * them into datasets to draw on a graph
 *
 * Every function here takes two functions (xAxis/yAxis) and returns
 * a File, which contains the location of the newly made graph png
 */

object Graph {


  def drawBarChart[B <: Comparable[_], T <: DataType[T]](data: BarChartData[B, T],
                  title: String,
                  xAxisTitle: String,
                  yAxisTitle: String) = {
    val saveString = Global.getPictureSaveString
    val drawer = future {
      val chart = new BarChart(data.toCategorySet, title, xAxisTitle, yAxisTitle)
      val base64 = chart.toBase64
      Cache.set(saveString, base64, 3600)
    }
    drawer onFailure {
      case t =>
        println("Graph failed: " + t.getMessage)

    }
    saveString
  }

  def drawLineGraph[A <: Comparable[_], T <: DataType[T]](data: BarChartData[A, T],
                  regression: RegressionGenerator,
                  title: String,
                  xAxisTitle: String,
                  yAxisTitle: String) = {
    val saveString = Global.getPictureSaveString
    val drawer = future {
      val chart = new LineGraph(data.toXYSeriesCollection, regression,
        title, xAxisTitle, yAxisTitle)
      val base64 = chart.toBase64
      Cache.set(saveString, base64, 3600)
    }

    drawer onFailure {
      case t =>
        println("Graph failed: " + t.getMessage)

    }
    saveString
  }

}
