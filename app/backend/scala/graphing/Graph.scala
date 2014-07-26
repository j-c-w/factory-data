package backend.scala.graphing

import backend.scala.datatypes.DataType
import backend.scala.graphing.GraphDataSet
import backend.scala.query.ResultListObject

import scala.reflect.io.File

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

class Graph[T <: DataType[T]] (data: List[List[ResultListObject[T]]]) {
  def drawBarChart[B, C](xAxis: ResultListObject[T] => B,
                  yAxis: ResultListObject[T] => C): File = {
    val data = generateData(xAxis, yAxis)
    null
  }


  ////////////////////////Generating a graphable dataset from the passed data//////////////////////////////////////////

  private def generateData[B, C](xAxis: ResultListObject[T] => B,
                          yAxis: ResultListObject[T] => C) = {
    //map every single data entry to a set of x and y axis points
    val dataSets = data map (generateSingleDataSet(_, xAxis, yAxis))
    //fold all these together into a single GraphDataSet
    dataSets.fold(dataSets.head) (_ add _)
  }

  private def generateSingleDataSet[B, C](data: List[ResultListObject[T]],
                                    xAxis: (ResultListObject[T]) => B,
                                    yAxis: (ResultListObject[T]) => C): GraphDataSet[B, C] = {
    val (xData, yData) = (data map (x => (xAxis(x), yAxis(x)))).unzip
    new GraphDataSet(List(xData), List(yData))
  }
}
