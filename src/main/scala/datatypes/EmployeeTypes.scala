package main.scala.datatypes


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
}


/*
 * a builder class for the employee types
 */
class EmployeeTypesBuilder {
  var totalEmployees: IntegerOption = NoInteger
  var totalPresent: IntegerOption = NoInteger
  var percentPresent: DoubleOption = NoDouble
  var totalAbsent: IntegerOption = NoInteger
  var totalLeave: IntegerOption = NoInteger

  def build: EmployeeTypes = new EmployeeTypes(
    totalEmployees,
    totalPresent,
    totalAbsent,
    percentPresent,
    totalLeave
  )
}