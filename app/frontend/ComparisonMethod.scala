package frontend

import backend.scala.datatypes.options.MathComparable

/*
 * Created by Jackson Woodruff on 21/08/2014
 *
 * This is class full of various different comparison
 * methods, (==, >, < etc)
 *
 *
 */

object ComparisonMethod {
  def fromString(string: String) : ComparisonMethod = string match {
    case Equals.toString => Equals
    case NotEqual.toString => NotEqual
    case LessThan.toString => LessThan
    case GreaterThan.toString => GreaterThan
    case GreaterThanOrEqual.toString => GreaterThanOrEqual
    case LessThanOrEqual.toString => LessThanOrEqual
    case NotNone.toString => NotNone
  }

  def asList: List[String] = List(
    Equals.toString, NotEqual.toString, LessThan.toString,
    GreaterThan.toString, LessThanOrEqual.toString, GreaterThanOrEqual.toString,
    NotNone.toString
  )
}

trait ComparisonMethod {
  def toString: String
  def compare[A <: MathComparable[A]](o1: A, o2: A): Boolean
}

/*
 * This is a list of all
 * classes used for the comparisons.
 */

object Equals extends ComparisonMethod {
  override def compare[A <: MathComparable[A]](o1: A, o2: A): Boolean =
    o1 == o2
  override val toString = "equal to"
}
object NotEqual extends ComparisonMethod {
  override def compare[A <: MathComparable[A]](o1: A, o2: A): Boolean =
    o1 != o2
  override val toString = "not equal to"
}

object LessThan extends ComparisonMethod {
  override def compare[A <: MathComparable[A]](o1: A, o2: A): Boolean =
    o1 < o2
  override val toString = "less than"
}

object GreaterThan extends ComparisonMethod {
  override def compare[A <: MathComparable[A]](o1: A, o2: A): Boolean =
    o1 > o2
  override val toString = "greater than"
}

object LessThanOrEqual extends ComparisonMethod {
  override def compare[A <: MathComparable[A]](o1: A, o2: A): Boolean =
    o1 <= o2
  override val toString = "greater than or equal"
}

object GreaterThanOrEqual extends ComparisonMethod {
  override def compare[A <: MathComparable[A]](o1: A, o2: A): Boolean =
    o1 >= o2
  override val toString = "less than or equal"
}

object NotNone extends ComparisonMethod {
  /*
   * This is a special implementation of the methods above.
   * It does not make use of o2 because it only checks the not-none-ness
   * of o1
   */
  override def compare[A <: MathComparable[A]](o1: A, o2: A): Boolean =
    o1.isNone
  override val toString = "has data"
}