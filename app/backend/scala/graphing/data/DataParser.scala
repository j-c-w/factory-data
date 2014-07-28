package backend.scala.graphing.data

import backend.scala.datatypes.DataType
import backend.scala.query.ResultListObject

/*
 * Created by Jackson Woodruff on 26/07/2014 
 *
 * This class takes one set of data (List[ResultListObject]) and
 * a function that converts from that to an XYData and combines
 * the two on request.
 */

class DataParser[A, T <: DataType[T]](data: List[ResultListObject[T]],
                                      converter: ResultListObject[T] => (A, Double),
                                      series: String) {
  lazy val parse: XYData[A, Double] = {
    val zippedData  = (data map converter)
    val (x, y) = zippedData.unzip
    new XYData[A, Double](x, y, series)
  }
}
