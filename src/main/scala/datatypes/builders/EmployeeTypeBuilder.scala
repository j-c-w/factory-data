package main.scala.datatypes.builders

import main.scala.datatypes.EmployeeTypes
import main.scala.datatypes.options._

/*
 * Created by Jackson Woodruff on 23/07/2014 
 *
 *
 * Designed to make it easy to leave data out
 * when it is not needed (i.e. after an aggregate,
 * where a lot of data is not relevant)
 */

class EmployeeTypeBuilder {
  self =>

  var employees: IntegerOption = NoInteger
  var present: IntegerOption = NoInteger
  var percentAbsent: DoubleOption = NoDouble
  var absent: IntegerOption = NoInteger
  var leave: IntegerOption = NoInteger

  def build: EmployeeTypes = new EmployeeTypes(
    employees,
    present,
    absent,
    percentAbsent,
    leave
  )

  //AVERAGES the two data points
  def merge(other: EmployeeTypeBuilder) = new EmployeeTypeBuilder {
    employees = (self.employees + other.employees) / SomeInteger(2)
    present = (self.present + other.present) / SomeInteger(2)
    absent = (self.absent + other.absent) / SomeInteger(2)
    leave = (self.leave + other.leave) / SomeInteger(2)
    //leaves out percent for the moment
  }
}

