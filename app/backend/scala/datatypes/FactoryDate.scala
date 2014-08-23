package backend.scala.datatypes

import java.util.{GregorianCalendar, Calendar}

import backend.scala.datatypes.options._

/*
 * Created by Jackson Woodruff on 31/07/2014 
 *
 *
 * This is a class that simplifies the java date class.
 *
 * Most importantly, it splits the date into three
 * numbers (day/month/year).
 * Also it creates reasonable strings that don't
 * show the time and timezone and useless
 * information in this context
 */

class FactoryDate(val year: IntegerOption, val month: IntegerOption, val day: IntegerOption) extends Comparable[FactoryDate]{
  def this(javaDate: java.util.Date) = this(SomeInteger(javaDate.getYear + 1900),
                    SomeInteger(javaDate.getMonth + 1), SomeInteger(javaDate.getDate))

  /*
   * merges two dates.
   *
   * If the values for any specific number are the same
   * (i.e. year1 == year2), then the value is kept
   * in the new date object.
   *
   * Otherwise it is replaced with a NoInteger
   * example:
   *  25/12/1996 merge 10/12/1776 -> NoInteger/12/NoInteger
   */
  def merge(other: FactoryDate) = {
    //I think I overdid this method -- please improve
    //have a merry christmas and a happy
    val newYear = other.year match {
      case NoInteger => NoInteger
      case otherYear =>
        if (this.year == otherYear) otherYear else NoInteger
    }
    val newMonth = other.month match {
      case NoInteger => NoInteger
      case otherMonth =>
        if (this.month == otherMonth) otherMonth else NoInteger
    }
    val newDay = other.day match {
      case NoInteger => NoInteger
      case otherDay =>
        if (this.day == otherDay) otherDay else NoInteger
    }
    new FactoryDate(newYear, newMonth, newDay)
  }

  override def toString = {
    val yearStr = year match {
      case NoInteger => ""
      case SomeInteger(x) => "/" + x
    }
    val monthStr = month match {
      case NoInteger => ""
      case SomeInteger(x) => x
    }
    val dayString = day match {
      case NoInteger => ""
      case SomeInteger(x) => x + "/"
    }
    val combined = dayString + monthStr + yearStr
    //just a little check to make sure a friendly message is shown
    if (combined == "") NoDate.toString else combined
  }

  override def compareTo(other: FactoryDate): Int = {
    this.toLongInt.compareTo(other.toLongInt)
  }

  /*
   * Returns this date as a single integer i.e
   * 25/12/1996 -> 19961225
   *
   * used for easy comparisons between dates
   */
  private def toLongInt: Int = {
    val y = year match {
      case NoInteger => 0
      case SomeInteger(x) => x * 10000//left shift the digits by 4
    }
    val m = month match {
      case NoInteger => 0
      case SomeInteger(x) => x * 100 //left shift by 2
    }
    val d = day match {
      case NoInteger => 0
      case SomeInteger(x) => x//don't left shift at all
    }
    y + m + d
  }
}
