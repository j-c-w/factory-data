package backend.scala.graphing.data

import backend.scala.datatypes.DataType
import backend.scala.datatypes.options.MathComparable
import backend.scala.query.ResultListObject
import java.lang.Comparable

/*
 * Created by Jackson Woodruff on 26/07/2014 
 *
 * This class takes one set of data (List[ResultListObject]) and
 * a function that converts from that to an XYData and combines
 * the two on request.
 */

class DataParser[A <: Comparable[_], T <: DataType[T]](data: List[ResultListObject[T]],
                                      converter: ResultListObject[T] => (A, Double),
                                      series: String) {
  lazy val parse: XYData[A, Double] = {
    val zippedData  = (data map converter).sortWith {
      //we have to sort with strings because the compiler is dying on me
      // and I do not have the time/stamina to sort out all the type errors
      //without an in-ide type checker.
      //Please do update this if possible
      case ((xAxis, _), (xAxisTwo, _)) => xAxis.toString().compareTo(xAxisTwo.toString()) < 1
    }
    println("data sorted")
    val (x, y) = zippedData.toList.unzip
    new XYData[A, Double](x, y, series)
  }
}
