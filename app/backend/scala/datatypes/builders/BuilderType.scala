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
 */

trait BuilderType[T <: DataType[T]] {
  /*
   * This method is used to turn the builder
   * into a type of T.
   */
  def build: T
}
