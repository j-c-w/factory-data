package backend.scala.datatypes

import backend.scala.datatypes.builders.EmployeeTypeBuilder
import backend.scala.datatypes.options.{NoDouble, DoubleOption}


/*
 * Created by Jackson Woodruff on 20/07/2014 
 *
 * Class that stores the attendance data for
 * employee types individually, splitting up
 * operators and helpers into separate data
 * types.
 */

class EmployeeTypes(total: DoubleOption,
                     present: DoubleOption,
                     absent: DoubleOption,
                     percentAbsent: DoubleOption,
                     leave: DoubleOption) extends DataType[EmployeeTypes] {
  self =>

  type Self = EmployeeTypes

  //note that this does not call the present/absent methods
  //because that would introduce an infinite circle of
  //data guessing if either were undefined
  lazy val getTotal: DoubleOption = total match {
    case NoDouble => present + absent + leave
    case data => data
  }


  //once again, the getTotal and getAnsent methods
  //are not used on purpose
  lazy val getPresent: DoubleOption = present match {
    case NoDouble => total - absent - leave
    case data => data
  }

  lazy val getAbsent: DoubleOption = absent match {
    case NoDouble => total - present - leave
    case data => data
  }

  //returns the percent absent rounded
  //note that this uses the get... methods
  //rather than the default values, because
  //this cannot cause an infinite loop
  lazy val getPercentAbsent: DoubleOption = percentAbsent match {
    case NoDouble => {
      (getAbsent * 100) / getTotal
    }
    case data => data
  }


  //AFAIK, there is no way to calculate leave numbers unless given
  lazy val getLeave: DoubleOption =
    leave or (total - present - absent)
  def toBuilder = new EmployeeTypeBuilder {
    employees = total
    present = self.present
    absent = self.absent
    percentAbsent = self.getPercentAbsent
    leave = self.leave
  }

  def averageBy(number: Int) =
    (this.toBuilder averageBy number).build

  def mergeSum(other: EmployeeTypes) = 
    (this.toBuilder mergeSum other.toBuilder).build

  def get = this

  /*
   * Note that this does not return a fully fledged
   * html table, because it has never been designed
   * for use on it's owm.
   *
   * It returns the items to be used in the
   * LineListObject thing
   * LineListObject thing
   */
  def toHtml = "<td>" + getTotal + "</td>" +
    "<td>" + getPresent + "</td>" +
    "<td>" + getAbsent + "</td>" +
    "<td>" + getLeave + "</td>" +
    "<td>" + getPercentAbsent + "</td>"


  override def toString =
    "     Employees = " + total +
    "     Present = " + present +
    "     Absent = " + absent +
    "     Percent Absent" + getPercentAbsent
    "     Leave = " + getLeave
}

