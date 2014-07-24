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

  var employees: DoubleOption = NoDouble
  var present: DoubleOption = NoDouble
  var percentAbsent: DoubleOption = NoDouble
  var absent: DoubleOption = NoDouble
  var leave: DoubleOption = NoDouble

  def build: EmployeeTypes = new EmployeeTypes(
    employees,
    present,
    absent,
    percentAbsent,
    leave
  )

  def averageBy(number: Int) = new EmployeeTypeBuilder {
    employees = self.employees / SomeDouble(number)
    present = self.present / SomeDouble(number)
    absent = self.absent / SomeDouble(number)
    leave = self.leave / SomeDouble(number)
    //leaves out the percent because that wouldn't make much, if any, sense
  }

  //SUMS the data points
  def mergeSum(other: EmployeeTypeBuilder) = new EmployeeTypeBuilder {
    employees = self.employees + other.employees
    present = self.present + other.present
    absent = self.absent + other.absent
    leave = self.leave + other.leave
  }
}

