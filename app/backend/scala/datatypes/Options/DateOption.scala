package backend.scala.datatypes.options

import backend.scala.datatypes.FactoryDate
import backend.scala.datatypes.options.SomeDate

/*
 * Created by Jackson Woodruff on 23/08/2014 
 *
 *
 * This is now replacing the old DataOption class
 * that caused a bunch of difficulties due to the
 * various comparable classes that I wanted to
 * implement. This is a big improvement
 */

abstract class DateOption extends MathComparable[DateOption] {
  def isEmpty: Boolean
  def get: FactoryDate
  def toString: String

  def compareTo(other: DateOption) = (this, other) match {
    case (NoDate, NoDate) => 0
    case (SomeDate(d1), SomeDate(d2)) => d1 compareTo d2
    case (_, _) => -2
  }
}

object NoDate extends DateOption {
  override def isEmpty: Boolean = true
  override def get: FactoryDate = throw new NoSuchElementException("NoDate.get")
  override def toString = "No Date"
}

case class SomeDate(date: FactoryDate) extends DateOption {
  override def isEmpty: Boolean = false
  override def get: FactoryDate = date
  override def toString: String = date.toString
}
