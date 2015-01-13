package backend.scala.datatypes.options

/*
 * Created by Jackson Woodruff on 23/08/2014 
 *
 * This is an improved version of the simple
 * comparable class. It introduces various
 * useful methods like <, >, <= etc.
 */

trait MathComparable[T <: MathComparable[T]] extends Comparable[T] with OptionComparable {
  def > (other: T) =
    (this compareTo other) > 0

  def < (other: T) =
    (this compareTo other) < 0

  def >= (other: T) =
    (this compareTo other) >= 0

  def <= (other: T) =
    (this compareTo other) <= 0
}

trait OptionComparable {
  def isNone: Boolean
}