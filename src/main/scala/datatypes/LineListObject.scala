package main.scala.datatypes

import java.util.Date


/*
 * Created by Jackson Woodruff on 20/07/2014 
 *
 *
 * Holding class for the attendance data objects.
 * For unknown fields pass NoData
 *
 * It is important in the class that you avoid cyclical functions
 */

class LineListObject(totalProductionWorkers: EmployeeTypes,
                 operators: EmployeeTypes,
                 helpers: EmployeeTypes,
                 supervisors: EmployeeTypes,
                 val factoryCode: IntegerOption,
                 val lineCode: IntegerOption,
                 val date: DataOption[Date]) {


  //////////////////////PRODUCTION WORKERS SECTION/////////////////////
  //just like in the inner classes,
  // this calls the values rather than the methods
  //to avoid infinite loops
  lazy val getTotalProductionWorkers: IntegerOption =
    totalProductionWorkers.getTotal or (operators.getTotal + helpers.getTotal)

  //just as above
  lazy val getTotalHelpers: IntegerOption =
    helpers.getTotal or (totalProductionWorkers.getTotal - operators.getTotal)

  lazy val getTotalOperators: IntegerOption =
    operators.getTotal or (totalProductionWorkers.getTotal - helpers.getTotal)

  lazy val getTotalProductionWorkersAbsent: IntegerOption =
    totalProductionWorkers.getAbsent or (operators.getAbsent + helpers.getAbsent)

  lazy val getTotalHelpersAbsent: IntegerOption =
    helpers.getAbsent or (totalProductionWorkers.getAbsent - operators.getAbsent)

  lazy val getTotalOperatorsAbsent: IntegerOption =
    operators.getAbsent or (totalProductionWorkers.getAbsent - helpers.getAbsent)

  lazy val getTotalProductionWorkersPresent: IntegerOption =
    totalProductionWorkers.getPresent or (operators.getPresent + helpers.getPresent)

  lazy val getTotalHelpersPresent: IntegerOption =
    helpers.getPresent or (totalProductionWorkers.getPresent - operators.getPresent)

  lazy val getTotalOperatorsPresent: IntegerOption =
    operators.getPresent or (totalProductionWorkers.getPresent - helpers.getPresent)

  lazy val getTotalProductionWorkersLeave: IntegerOption =
    totalProductionWorkers.getLeave or (operators.getLeave + helpers.getLeave)

  lazy val getOperatorLeave: IntegerOption =
    operators.getLeave or (totalProductionWorkers.getLeave - helpers.getLeave)

  lazy val getHelpersLeave: IntegerOption =
    helpers.getLeave or (totalProductionWorkers.getLeave - operators.getLeave)

  //methods are fine to use here instead of values
  lazy val getPercentProductionWorkersAbsent: DoubleOption =
    totalProductionWorkers.getPercentAbsent or {
      ((getTotalProductionWorkersAbsent * 100).toDoubleOption / getTotalProductionWorkers.toDoubleOption).rnd
    }

  lazy val getPercentHelpersAbsent: DoubleOption =
    helpers.getPercentAbsent or {
      ((getTotalHelpersAbsent * 100).toDoubleOption / getTotalHelpers.toDoubleOption).rnd
    }

  lazy val getPercentOperatorsAbsent: DoubleOption =
    operators.getPercentAbsent or {
      ((getTotalOperatorsAbsent * 100).toDoubleOption / getTotalOperators.toDoubleOption).rnd
    }
  ////////////////////////////SUPERVISORS SECTION/////////////////////////////////////////
  //in this section there are no second guesses, because there is really no secondary way
  //to work out these numbers
  lazy val getTotalSupervisors: IntegerOption =
    supervisors.getTotal

  lazy val getAbsentSupervisors: IntegerOption =
    supervisors.getAbsent

  lazy val getSupervisorsLeave: IntegerOption =
    supervisors.getLeave

  lazy val getPercentSupervisorsAbsent =
    supervisors.getPercentAbsent


  ///////////////////////////////MISCELLANEOUS SECTION////////////////////////////////////

  lazy val getDate: DataOption[Date] = date
  lazy val getFactoryCode: IntegerOption = factoryCode
  lazy val getLineCode: IntegerOption = lineCode
  
  override def toString = {
    "Factory code = " + getFactoryCode + 
    "     , Line code = " + getLineCode +
    "     , Date = " + getDate +
    "     , Total = " + getTotalProductionWorkers +
    "     , Present = " + getTotalProductionWorkersPresent +
    "     , Absent = " + getTotalProductionWorkersAbsent +
    "     , Leave = " + getTotalProductionWorkersLeave +
    "     , Percent Absent = " + getPercentProductionWorkersAbsent +
    "     , Total Operators = " + getTotalOperators +
    "     , Total Helpers = " + getTotalHelpers
  }
}


/*
 * This class is used to build AttendanceDataObjects by allowing
 * the data to be added one attribute at a time.
 */
class AttendanceDataObjectBuilder {
  var totalProductionWorkers: EmployeeTypes = new EmployeeTypes(NoInteger, NoInteger, NoInteger, NoDouble, NoInteger)
  var operators: EmployeeTypes = new EmployeeTypes(NoInteger, NoInteger, NoInteger, NoDouble, NoInteger)
  var helpers: EmployeeTypes = new EmployeeTypes(NoInteger, NoInteger, NoInteger, NoDouble, NoInteger)
  var supervisors: EmployeeTypes = new EmployeeTypes(NoInteger, NoInteger, NoInteger, NoDouble, NoInteger)
  var factoryCode: IntegerOption = NoInteger
  var lineCode: IntegerOption = NoInteger
  var date: DataOption[Date] = NoData

  def build: LineListObject =
    new LineListObject(totalProductionWorkers, operators, helpers, supervisors, factoryCode, lineCode, date)
}
