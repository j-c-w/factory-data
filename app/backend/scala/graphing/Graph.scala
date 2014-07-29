package backend.scala.graphing

import java.io.File

import backend.java.{LineGraph, BarChart}
import backend.scala.datatypes.DataType
import org.jfree.chart.{ChartUtilities, ChartFactory, JFreeChart}

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
                  yAxisTitle: String): File = {
    val chart = new BarChart(data.toCategorySet, title, xAxisTitle, yAxisTitle)
    chart.saveAsPNG()
  }

  def drawLineGraph[A <: Comparable[_], T <: DataType[T]](data: LineGraphData[A, T],
                  title: String,
                  xAxisTitle: String,
                  yAxisTitle: String) : File = {
    val chart = new LineGraph(data.toCategorySet, title, xAxisTitle, yAxisTitle)
    chart.saveAsPNG()
  }

}
