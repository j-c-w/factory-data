package backend.scala.datatypes

import java.util.Date

import backend.scala.datatypes.options.{DateOption, DoubleOption, IntegerOption}
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

class LineListObject(val factoryDate: FactoryDate,
                 val lineInfo: LineData,
                 val orderInfo: OrderData,
                 val targetInformation: TargetData,
                 val inputOutput: IOData,
                 val qualityInformation: QualityData,
                 val absenteeism: AbsenteeismData,
                 val numberOfObservations: IntegerOption,
                 val factoryCode: IntegerOption) extends DataType[LineListObject, LineListObjectBuilder] {

  self =>

  type Self = LineListObject

  def toBuilder: LineListObjectBuilder = {
    new LineListObjectBuilder {
      factoryDate = self.factoryDate
      orderInfo = self.orderInfo
      targetInformation = self.targetInformation
      inputOutput = self.inputOutput
      qualityInformation = self.qualityInformation
      absenteeism = self.absenteeism
      numberOfObservations = self.numberOfObservations
      factoryCode = self.factoryCode
    }
  }

  //we have to define efficiency down here because it is calculated
  //from other values
  def efficiency: DoubleOption = ???

  //averages this datapoint by a number
  def averageBy(number: Int) =
    this.toBuilder.averageBy(number).build

  def get = this
}