package main.scala.datatypes

/*
 * Created by Jackson Woodruff on 24/07/2014 
 *
 * This is a trait that all data classes should extend.
 *
 * It enables many different types to be passed to the
 * query classes and generalises a few different methods
 *
 */

trait DataType {
  type Self <: DataType

  /*
   * This takes another DataType and adds that to itself.
   * For summable datatypes, like production workers etc,
   * the numbers should just be added.
   *
   * For non-summable datatypes this should check for
   * equality (where valid, Date, LineCode, etc.) and
   * return the value if the two are equal and NoData
   * otherwise.
   *
   * For unsummable and uncomparable datatypes (percent),
   * this should return NoData
   *
   * i.e.
   *    factory code:
   *      if (this.factoryCode == other.factoryCode) this.factoryCode
   *      else NoData
   *    percent:
   *      NoData
   *    this.operators + other.operators
   */
  def mergeSum(other: Self): Self

  /*
   * This should divide all the types of itself that
   * are divisible by a number (i.e. total production
   * workers), and divide them by a new number.
   *
   * Non divisible types should remain untouched.
   *
   * NoData types should return NoData
   * i.e.
   *  line code:
   *    100413 -> 100413 (not a divisible type)
   *  total operators:
   *    506 -> 506/number
   */
  def averageBy(number: Int): Self
}
