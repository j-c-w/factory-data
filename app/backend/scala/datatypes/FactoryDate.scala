package backend.scala.datatypes

import java.util.{GregorianCalendar, Calendar}

import backend.scala.datatypes.options.{SomeInteger, NoData, NoInteger, IntegerOption}

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

class FactoryDate(year: IntegerOption, month: IntegerOption, day: IntegerOption) extends Comparable[FactoryDate]{
  def this(javaDate: java.util.Date) = this(SomeInteger(javaDate.getYear + 1900),
                    SomeInteger(javaDate.getMonth + 1), SomeInteger(javaDate.getDate))

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
    dayString + monthStr + yearStr
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
