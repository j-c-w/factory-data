package backend.scala.graphing

import backend.scala.datatypes.DataType
import backend.scala.graphing.data.DataParser
import backend.scala.query.ResultListObject

/*
 * Created by Jackson Woodruff on 29/07/2014 
 *
 * Currently this class does not implement any
 * methods, and is just literally a renamed
 * BarChartData class.
 *
 * However, should I ever need to make changes for
 * a line graph, then this is the place to do it
 */

class LineGraphData[A <: Comparable[_], T <: DataType[T]](dataParser: List[DataParser[A, T]]) extends BarChartData[A, T](dataParser){
  def this(xAxis: ResultListObject[T] => A, yAxis: ResultListObject[T] => Double, series: String, data: List[ResultListObject[T]]) = this(List(new DataParser[A, T](
    data, ((x) => (xAxis(x), yAxis(x))), series
  )))

}
