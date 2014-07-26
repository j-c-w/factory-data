package backend.scala.datatypes

import java.util.Date

import backend.scala.datatypes.options.{DoubleOption, DataOption, IntegerOption}
import main.backend.scala.datatypes.builders.LineListObjectBuilder


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
                 val date: DataOption[Date]) extends DataType[LineListObject] {

  self =>

  type Self = LineListObject


  //////////////////////PRODUCTION WORKERS SECTION/////////////////////
  //just like in the inner classes,
  // this calls the values rather than the methods
  //to avoid infinite loops
  lazy val getTotalProductionWorkers: DoubleOption =
    totalProductionWorkers.getTotal or (operators.getTotal + helpers.getTotal)

  //just as above
  lazy val getTotalHelpers: DoubleOption =
    helpers.getTotal or (totalProductionWorkers.getTotal - operators.getTotal)

  lazy val getTotalOperators: DoubleOption =
    operators.getTotal or (totalProductionWorkers.getTotal - helpers.getTotal)

  lazy val getTotalProductionWorkersAbsent: DoubleOption =
    totalProductionWorkers.getAbsent or (operators.getAbsent + helpers.getAbsent)

  lazy val getTotalHelpersAbsent: DoubleOption =
    helpers.getAbsent or (totalProductionWorkers.getAbsent - operators.getAbsent)

  lazy val getTotalOperatorsAbsent: DoubleOption =
    operators.getAbsent or (totalProductionWorkers.getAbsent - helpers.getAbsent)

  lazy val getTotalProductionWorkersPresent: DoubleOption =
    totalProductionWorkers.getPresent or (operators.getPresent + helpers.getPresent)

  lazy val getTotalHelpersPresent: DoubleOption =
    helpers.getPresent or (totalProductionWorkers.getPresent - operators.getPresent)

  lazy val getTotalOperatorsPresent: DoubleOption =
    operators.getPresent or (totalProductionWorkers.getPresent - helpers.getPresent)

  lazy val getTotalProductionWorkersLeave: DoubleOption =
    totalProductionWorkers.getLeave or (operators.getLeave + helpers.getLeave)

  lazy val getOperatorLeave: DoubleOption =
    operators.getLeave or (totalProductionWorkers.getLeave - helpers.getLeave)

  lazy val getHelpersLeave: DoubleOption =
    helpers.getLeave or (totalProductionWorkers.getLeave - operators.getLeave)

  //methods are fine to use here instead of values
  lazy val getPercentProductionWorkersAbsent: DoubleOption =
    totalProductionWorkers.getPercentAbsent or {
      (getTotalProductionWorkersAbsent * 100) / getTotalProductionWorkers
    }

  lazy val getPercentHelpersAbsent: DoubleOption =
    helpers.getPercentAbsent or {
      (getTotalHelpersAbsent * 100) / getTotalHelpers
    }

  lazy val getPercentOperatorsAbsent: DoubleOption =
    operators.getPercentAbsent or {
      (getTotalOperatorsAbsent * 100) / getTotalOperators
    }
  ////////////////////////////SUPERVISORS SECTION/////////////////////////////////////////
  //in this section there are no second guesses, because there is really no secondary way
  //to work out these numbers
  lazy val getTotalSupervisors: DoubleOption =
    supervisors.getTotal

  lazy val getAbsentSupervisors: DoubleOption =
    supervisors.getAbsent

  lazy val getSupervisorsLeave: DoubleOption =
    supervisors.getLeave

  lazy val getPercentSupervisorsAbsent =
    supervisors.getPercentAbsent


  ///////////////////////////////MISCELLANEOUS SECTION////////////////////////////////////

  lazy val getDate: DataOption[Date] = date
  lazy val getFactoryCode: IntegerOption = factoryCode
  lazy val getLineCode: IntegerOption = lineCode

  def toBuilder: LineListObjectBuilder = {
    new LineListObjectBuilder {
      totalProductionWorkers = self.totalProductionWorkers.toBuilder.build
      operators = self.operators.toBuilder.build
      helpers = self.helpers.toBuilder.build
      supervisors = self.supervisors.toBuilder.build
      date = self.getDate
      factoryCode = self.getFactoryCode
      lineCode = self.getLineCode
    }
  }

  //averages this datapoint by a number
  def averageBy(number: Int) =
    this.toBuilder.averageBy(number).build

  def get = this

  //merges two datasets by adding the values together
  def mergeSum(other: LineListObject) =
    (this.toBuilder mergeSum other.toBuilder).build

  def takeOut = this

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