package main.scala.datatypes


/*
 * Created by Jackson Woodruff on 24/07/2014
 *
 * This trait is here for historical reference, not actual use.
 *
 * The real problem with this trying to use this trait is that
 * the anonymous functions that I have been using to shift
 * data around do not work at all well with this model.
 *
 * It is possible to define the correct functions that you need
 * and have them return the correct datatypes, as is shown below,
 * but the problem comes when you cannot access the other fields
 * of the class
 */

trait DataType[T] {
  type Self <: DataType[T]

  def mergeSum(other: Self): Self

  def averageBy(number: Int): Self


}