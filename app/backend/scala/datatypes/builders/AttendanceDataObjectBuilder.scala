package backend.scala.datatypes.builders

import backend.scala.datatypes.{AttendanceDataObject, EmployeeAttendanceObject}
import backend.scala.datatypes.options.{DoubleOption, NoDouble}

/*
 * Created by Jackson Woodruff on 19/09/2014 
 * 
 */

class AttendanceDataObjectBuilder extends BuilderType[AttendanceDataObject] {
  self =>

  var lineOperatorData: EmployeeAttendanceObject = new EmployeeAttendanceObjectBuilder().build
  var helperData: EmployeeAttendanceObject = new EmployeeAttendanceObjectBuilder().build
  var manpowerTotal: DoubleOption = NoDouble
  var machines: DoubleOption = NoDouble

  def build = new AttendanceDataObject(
    lineOperatorData, helperData, manpowerTotal, machines
  )
}
