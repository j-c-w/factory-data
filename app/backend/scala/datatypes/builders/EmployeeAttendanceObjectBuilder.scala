package backend.scala.datatypes.builders

import backend.scala.datatypes.EmployeeAttendanceObject
import backend.scala.datatypes.options.{NoDouble, DoubleOption}

/*
 * Created by Jackson Woodruff on 19/09/2014 
 * 
 */

class EmployeeAttendanceObjectBuilder extends BuilderType[EmployeeAttendanceObject] {
  var registered: DoubleOption = NoDouble
  var actual: DoubleOption = NoDouble
  var present: DoubleOption = NoDouble
  var absent: DoubleOption = NoDouble

  def build: EmployeeAttendanceObject = new EmployeeAttendanceObject(
    registered, actual, present, absent
  )
}
