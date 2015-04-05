package backend.scala.graphing

import java.io.File

import backend.java.utils.FileUtility
import backend.java.{ScatterPlot, LineGraph, BarChart}
import backend.scala.datatypes.DataType
import backend.scala.graphing.regressions.{Regression, RegressionGenerator}
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
  def drawScatterPlot[B <: Comparable[_], T <: DataType[T]] (data: BarChartData[B, T],
                  title: String,
                  xAxisTitle: String,
                  yAxisTitle: String,
                  regressions: Array[Regression],
                  saveString: String) = {
    drawGraph(new ScatterPlot(data.toXYSeriesCollection, title, xAxisTitle, yAxisTitle, regressions), saveString)
  }

  def drawBarChart[B <: Comparable[_], T <: DataType[T]](data: BarChartData[B, T],
                  title: String,
                  xAxisTitle: String,
                  yAxisTitle: String,
                  saveString: String) = {
    drawGraph(new BarChart(data.toCategorySet, title, xAxisTitle, yAxisTitle), saveString)
  }

  def drawLineGraph[A <: Comparable[_], T <: DataType[T]](data: BarChartData[A, T],
                  title: String,
                  xAxisTitle: String,
                  yAxisTitle: String,
                  saveString: String) = {
    drawGraph(new LineGraph(data.toXYSeriesCollection, title, xAxisTitle, yAxisTitle), saveString)
  }

  private def drawGraph(chart: backend.java.Graph, saveString: String): String = {
    val drawer = future {
      val base64 = chart.toBase64
      Cache.set(saveString, base64, 3600)
    }

    drawer onFailure {
      case t =>
        println("Graph failed: " + t.getMessage)
        Cache.set(saveString, backend.java.Graph.errorBase64, 3600)
    }
    saveString
  }
}
