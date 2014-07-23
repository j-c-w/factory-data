package main.scala.datatypes.builders

import java.util.Date

import main.scala.datatypes.{LineListObject, EmployeeTypes}
import main.scala.datatypes.options._

/*
 * Created by Jackson Woodruff on 23/07/2014 
 *
 *
 * A builder class, mainly for the results of
 * searches etc, made to make it easy to leave
 * values out
 */

class LineListObjectBuilder {
  self =>

  var totalProductionWorkers: EmployeeTypes = emptyEmployeeType
  var operators: EmployeeTypes = emptyEmployeeType
  var helpers: EmployeeTypes = emptyEmployeeType
  var supervisors: EmployeeTypes = emptyEmployeeType
  var factoryCode: IntegerOption = NoInteger
  var lineCode: IntegerOption = NoInteger
  var date: DataOption[Date] = NoData

  private val emptyEmployeeType = new EmployeeTypes(
    NoInteger, NoInteger, NoInteger, NoDouble, NoInteger
  )

  def build = new LineListObject(totalProductionWorkers,
      operators, helpers, supervisors, factoryCode, lineCode, date)

  //takes two LineListObjectBuilders and
  //combines them into one by averaging the values
  def mergeAverage(other: LineListObjectBuilder) = new LineListObjectBuilder {
    totalProductionWorkers = self.totalProductionWorkers mergeAverage other.totalProductionWorkers
    operators = self.operators mergeAverage other.operators
    helpers = self.helpers mergeAverage other.helpers
    supervisors = self.supervisors mergeAverage other.supervisors
    //only merge these data if they are the same
    factoryCode = if (self.factoryCode == other.factoryCode) self.factoryCode else NoInteger
    lineCode = if (self.lineCode == other.lineCode) self.lineCode else NoInteger
    date = if (self.date == other.date) self.date else NoData
  }

  //merges two datasets by adding values together
  def mergeSum(other: LineListObjectBuilder) = new LineListObjectBuilder {
    totalProductionWorkers = self.totalProductionWorkers mergeSum other.totalProductionWorkers
    operators = self.operators mergeSum other.totalProductionWorkers
    helpers = self.helpers mergeSum other.helpers
    supervisors = self.supervisors mergeSum other.supervisors
    factoryCode = if (self.factoryCode == other.factoryCode) self.factoryCode else NoInteger
    lineCode = if (self.lineCode == other.lineCode) self.lineCode else NoInteger
    date = if (self.date == other.date) self.date else NoData
  }

}
