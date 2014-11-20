package backend.scala.datatypes.builders

import backend.scala.datatypes.DataType

/*
 * Created by Jackson Woodruff on 27/07/2014 
 *
 * This is extended by builder classes.
 *
 * Functions are the basic ones used by the builders
 * I have already written. This is the class that is
 * requred by the toBuilder function
 *
 * The first type parameter is the class repesented, while
 * the second parameter is the builder itself --
 * it is so I can define a mergeSum here.
 */

trait BuilderType[T <: DataType[T, B], B <: BuilderType[T, B]] {
  /*
   * This method is used to turn the builder
   * into a type of T.
   */
  def build: T

  /*
   * This takes another builder object of the same type
   * and merges it to create a single builder
   */
  def mergeSum(other: B): B
}
