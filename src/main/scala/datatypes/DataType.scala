package main.scala.datatypes


/*
 * Created by Jackson Woodruff on 24/07/2014
 *
 * This trait is used by every data type,
 * todo -- the documentation for this crucial trait
 */

trait DataType[T <: DataType[T]] {
  type Self <: DataType[T]

  def mergeSum(other: T): T

  def averageBy(number: Int): T

  //T every class puts an instance itself as a

  def get: T
}