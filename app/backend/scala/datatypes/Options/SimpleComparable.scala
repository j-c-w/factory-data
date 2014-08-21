package backend.scala.datatypes.options

/*
 * Created by Jackson Woodruff on 21/08/2014 
 *
 *
 * This is a class with a few basic methods to
 * allow for the easy comparison of Option types
 */

trait SimpleComparable[T <: SimpleComparable[T]] {
  def == (other: T): Boolean
  def < (other: T): Boolean
  def > (other: T): Boolean
  def <= (other: T): Boolean
  def >= (other: T): Boolean
}
