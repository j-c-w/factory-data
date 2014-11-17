package backend.scala.graphing.data

import backend.scala.datatypes.DataType
import backend.scala.datatypes.options.{DoubleOption, MathComparable}
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
                                      sortMode: ((A, Double), (A, Double)) => Boolean, series: String) {
  lazy val parse: XYData[A, Double] = {
    val zippedData  = (data map converter).sortWith (sortMode)
    val (x, y) = zippedData.toList.unzip
    new XYData[A, Double](x, y, series)
  }

  lazy val parseXY: XYData[Double, Double] = {
    val zippedData  = (data map converter).sortWith (sortMode)
    val (x, y) = zippedData.toList.unzip
    val doubleX = x.asInstanceOf[List[DoubleOption]] map (x => x.getOrElse(0))
    new XYData[Double, Double](doubleX, y, series)
  }
}
