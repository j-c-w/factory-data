package backend.scala.datatypes.options

/*
 * Created by Jackson Woodruff on 21/08/2014 
 *
 * This is a trait that enables the comminication
 * of the data layer with the fronend layer.
 *
 * Because all datatypes now extend this type (which
 * has a few utils methods built in), it is easier
 * for things to happen at the frontend
 */

trait JOption[T] {
  //returns the object stored in the option
  def get: T

  def < (other: JOption[T]): Boolean
  def > (other: JOption[T]): Boolean
  def == (other: JOption[T]): Boolean
  def >= (other: JOption[T]): Boolean
  def <= (other: JOption[T]): Boolean

}
