package main.backend.scala.datatypes.builders

import java.util.Date

import backend.scala.datatypes.builders._
import backend.scala.datatypes._
import backend.scala.datatypes.options._


/*
 * Created by Jackson Woodruff on 23/07/2014 
 *
 *
 * A builder class, mainly for the results of
 * searches etc, made to make it easy to leave
 * values out
 */

class LineListObjectBuilder extends BuilderType[LineListObject, LineListObjectBuilder] {
  self =>

  var factoryDate: FactoryDate = new FactoryDateBuilder().build
  var lineInfo: LineData = new LineDataBuilder().build
  var orderInfo: OrderData = new OrderDataBuilder().build
  var targetInformation: TargetData = new TargetDataBuilder().build
  var inputOutput: IOData = new IODataBuilder().build
  var qualityInformation: QualityData = new QualityDataBuilder().build
  var absenteeism: AbsenteeismData = new AbsenteeismDataBuilder().build
  var numberOfObservations: IntegerOption = NoInteger
  var factoryCode: IntegerOption = NoInteger

  def build = new LineListObject(factoryDate, lineInfo, orderInfo, targetInformation,
      inputOutput, qualityInformation, absenteeism, numberOfObservations, factoryCode)

  def averageBy(number: Int) = new LineListObjectBuilder {
    factoryDate = self.factoryDate averageBy number
    lineInfo = self.lineInfo averageBy number
    orderInfo = self.orderInfo averageBy number
    targetInformation = self.targetInformation averageBy number
    inputOutput = self.inputOutput averageBy number
    qualityInformation = self.qualityInformation averageBy number
    absenteeism = self.absenteeism averageBy number
    numberOfObservations = self.numberOfObservations
    factoryCode = self.factoryCode
  }

  //merges two datasets by adding values together
  def mergeSum(other: LineListObjectBuilder) = new LineListObjectBuilder {
    factoryDate = self.factoryDate mergeSum other.factoryDate
    lineInfo = self.lineInfo mergeSum other.lineInfo
    orderInfo = self.orderInfo mergeSum other.orderInfo
    targetInformation = self.targetInformation mergeSum other.targetInformation
    inputOutput = self.inputOutput mergeSum other.inputOutput
    qualityInformation = self.qualityInformation mergeSum other.qualityInformation
    absenteeism = self.absenteeism mergeSum other.absenteeism
    numberOfObservations = self.numberOfObservations + other.numberOfObservations
    factoryCode = self.factoryCode mergeEqual other.factoryCode
  }

}
