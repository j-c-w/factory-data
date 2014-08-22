package frontend

import backend.scala.datatypes.options.SimpleComparable

/*
 * Created by Jackson Woodruff on 21/08/2014 
 *
 * This is class full of various different comparison
 * methods, (==, >, < etc)
 *
 *
 */

abstract class ComparisonMethod {
  def fromString(string: String) : ComparisonMethod = string match {
    case "==" => Equals
    case "<" => LessThan
    case ">" => GreaterThan
    case ">=" => LessThanOrEqual
    case "<=" => GreaterThanOrEqual
  }

  def asList: List[String] = List(
    "==", "<", ">", ">=", "<="
  )

  def toString: String

  def compare[A <: SimpleComparable[A]](o1: A, o2: A): Boolean
}

/*
 * This is a list of all
 * classes used for the comparisons.
 */

object Equals extends ComparisonMethod {
  override def compare[A <: SimpleComparable[_]](o1: A, o2: A): Boolean =
    o1 == o2

  override def toString = "=="
}
object LessThan extends ComparisonMethod {

  override def compare[A <: SimpleComparable[A]](o1: A, o2: A): Boolean =
    o1 < o2

  override def toString = "<"
}
object GreaterThan extends ComparisonMethod {

  override def compare[A <: SimpleComparable[A]](o1: A, o2: A): Boolean =
    o1 > o2

  override def toString = ">"
}
object LessThanOrEqual extends ComparisonMethod {

  override def compare[A <: SimpleComparable[A]](o1: A, o2: A): Boolean =
    o1 <= o2

  override def toString = ">="
}
object GreaterThanOrEqual extends ComparisonMethod {

  override def compare[A <: SimpleComparable[A]](o1: A, o2: A): Boolean =
    o1 >= o2

  override def toString = "<="
}
