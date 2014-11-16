package main.backend.scala.datatypes.builders

import java.util.Date

import backend.scala.datatypes.builders.BuilderType
import backend.scala.datatypes.{FactoryDate, LineListObject, EmployeeTypes}
import backend.scala.datatypes.options._


/*
 * Created by Jackson Woodruff on 23/07/2014 
 *
 *
 * A builder class, mainly for the results of
 * searches etc, made to make it easy to leave
 * values out
 */

class LineListObjectBuilder extends BuilderType[LineListObject] {
  self =>

  var totalProductionWorkers: EmployeeTypes = emptyEmployeeType
  var operators: EmployeeTypes = emptyEmployeeType
  var helpers: EmployeeTypes = emptyEmployeeType
  var supervisors: EmployeeTypes = emptyEmployeeType
  var factoryCode: IntegerOption = NoInteger
  var lineCode: IntegerOption = NoInteger
  var date: DateOption = NoDate
  var numberOfObservations: IntegerOption = NoInteger

  private val emptyEmployeeType = new EmployeeTypes(
    NoDouble, NoDouble, NoDouble, NoDouble, NoDouble
  )

  def build = new LineListObject(totalProductionWorkers,
      operators, helpers, supervisors, numberOfObservations, factoryCode, lineCode, date)

  def averageBy(number: Int) = new LineListObjectBuilder {
    totalProductionWorkers = self.totalProductionWorkers averageBy number
    operators = self.operators averageBy number
    helpers = self.helpers averageBy number
    supervisors = self.supervisors averageBy number
    numberOfObservations = self.numberOfObservations
    factoryCode = self.factoryCode
    lineCode = self.lineCode
    date = self.date
  }

  //merges two datasets by adding values together
  def mergeSum(other: LineListObjectBuilder) = new LineListObjectBuilder {
    totalProductionWorkers = self.totalProductionWorkers mergeSum other.totalProductionWorkers
    operators = self.operators mergeSum other.operators
    helpers = self.helpers mergeSum other.helpers
    supervisors = self.supervisors mergeSum other.supervisors
    numberOfObservations = self.numberOfObservations + other.numberOfObservations
    factoryCode = if (self.factoryCode == other.factoryCode) self.factoryCode else NoInteger
    lineCode = if (self.lineCode == other.lineCode) self.lineCode else NoInteger
    date = (self.date, other.date) match {
      case (SomeDate(thisDate), SomeDate(otherDate)) => SomeDate(thisDate merge otherDate)
      case (_, _) => NoDate
    }
  }

}
