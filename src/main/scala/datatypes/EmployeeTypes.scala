package main.scala.datatypes

import main.scala.datatypes.builders.EmployeeTypeBuilder
import main.scala.datatypes.options.{NoDouble, DoubleOption, NoInteger, IntegerOption}


/*
 * Created by Jackson Woodruff on 20/07/2014 
 *
 * Class that stores the attendance data for
 * employee types individually, splitting up
 * operators and helpers into separate data
 * types.
 */

class EmployeeTypes(total: IntegerOption,
                     present: IntegerOption,
                     absent: IntegerOption,
                     percentAbsent: DoubleOption,
                     leave: IntegerOption) extends DataType{
  self =>


  //note that this does not call the present/absent methods
  //because that would introduce an infinite circle of
  //data guessing if either were undefined
  lazy val getTotal: IntegerOption = total match {
    case NoInteger => present + absent + leave
    case data => data
  }


  //once again, the getTotal and getAnsent methods
  //are not used on purpose
  lazy val getPresent: IntegerOption = present match {
    case NoInteger => total - absent - leave
    case data => data
  }

  lazy val getAbsent: IntegerOption = absent match {
    case NoInteger => total - present - leave
    case data => data
  }

  //returns the percent absent rounded
  //note that this uses the get... methods
  //rather than the default values, because
  //this cannot cause an infinite loop
  lazy val getPercentAbsent: DoubleOption = percentAbsent match {
    case NoDouble => {
      ((getAbsent * 100).toDoubleOption / getTotal.toDoubleOption).rnd
    }
    case data => data.rnd
  }


  //AFAIK, there is no way to calculate leave numbers unless given
  lazy val getLeave: IntegerOption =
    leave or (total - present - absent)

  def toBuilder = new EmployeeTypeBuilder {
    employees = getTotal
    present = getPresent
    absent = getAbsent
    percentAbsent = getPercentAbsent
    leave = getLeave
  }

  //helper method to take advantage of the
  //merge method in the Builder class
  def merge(other: EmployeeTypes) =
    (this.toBuilder merge other.toBuilder).build
}

