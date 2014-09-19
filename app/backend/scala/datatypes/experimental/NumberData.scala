package backend.scala.datatypes.experimental

import backend.scala.datatypes.options.{SomeDouble, DoubleOption}

/*
 * Created by Jackson Woodruff on 19/09/2014 
 *
 *
 * This uses a double option to store and manipulate
 * data.
 *
 */

class NumberData(number: DoubleOption) extends CellData {
  def this(number: String) = this(SomeDouble(number.toDouble))

  override def mergeSum(other: CellData): CellData = ???

  override def averageBy(number: Int): CellData = ???
}
