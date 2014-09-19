package backend.scala.datatypes.experimental

/*
 * Created by Jackson Woodruff on 19/09/2014 
 *
 *
 * This is a trait that enables the merging, sorting,
 * filtering etc. of otherwise unknown datatypes.
 *
 * The CellData object works be looking at the passed
 * string and seeing if it will conform to a number or
 * date. If not, it is left as a string.
 *
 *
 */

trait CellData {
  def mergeSum(other: CellData): CellData
  def averageBy(number: Int): CellData

}
