package backend.scala.datatypes

import backend.scala.datatypes.builders.BuilderType


/*
 * Created by Jackson Woodruff on 24/07/2014
 *
 * This trait is used by every data type which
 * contains some crucial functions to enable the
 * abstraction of the data that will be used.
 *
 * This trait enables the passing of datatypes
 * to the aggregator, filter and sorter.
 *
 * The type parameter required should be the
 * implementing class e.g:
 *
 * Implementation:
 *    class Example extends DataType[Example]
 */

trait ImplementedDataType[T <: ImplementedDataType[T, B], B <: BuilderType[T, B]] extends DataType[T] {
  type Self <: ImplementedDataType[T, B]

  /*
   * This function takes another datatype of type T
   * and adds it to the current datatype by merging
   * all summable values into one.
   *
   * This process should consist of summing the values
   * that can be summed (i.e. total production workers)
   * and using common sense on all other values (i.e.
   * percents should be entirely discarded, line codes
   * should not be summed, but should be kept the same
   * if they are both equal etc.)
   */
  def mergeSum(other: T): T =
    (this.toBuilder mergeSum other.toBuilder).build

  /*
   * returns a builder object for this item.
   */
  def toBuilder: B
}