package backend.scala.datatypes.builders

import backend.scala.datatypes.IOData
import backend.scala.datatypes.options.{NoDouble, DoubleOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class IODataBuilder extends BuilderType[IOData, IODataBuilder] {
  self =>

  var inputCount: DoubleOption = NoDouble
  var outputCount: DoubleOption = NoDouble
  var dayOutput: DoubleOption = NoDouble
  var outputMinutes: DoubleOption = NoDouble
  var lostMinutes: DoubleOption = NoDouble

  /*
     * This method is used to turn the builder
     * into a type of T.
     */
  override def build: IOData =
    new IOData(inputCount, outputCount, dayOutput, outputMinutes, lostMinutes)

  /*
   * This takes another builder object of the same type
   * and merges it to create a single builder
   */
  override def mergeSum(other: IODataBuilder): IODataBuilder = new IODataBuilder {
    inputCount = self.inputCount + other.inputCount
    outputCount = self.outputCount + other.outputCount
    dayOutput = self.dayOutput + other.dayOutput
    outputMinutes = self.outputMinutes + other.outputMinutes
    lostMinutes = self.lostMinutes + other.lostMinutes
  }
}
