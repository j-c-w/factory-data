package backend.scala.datatypes.builders

import backend.scala.datatypes.IOData
import backend.scala.datatypes.options.wrappers.DoubleOptionWrapper
import backend.scala.datatypes.options.{NoDouble, DoubleOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class IODataBuilder extends BuilderType[IOData, IODataBuilder] {
  self =>

  var inputCount: DoubleOptionWrapper = new DoubleOptionWrapper
  var outputCount: DoubleOptionWrapper = new DoubleOptionWrapper
  var dayOutput: DoubleOptionWrapper = new DoubleOptionWrapper
  var outputMinutes: DoubleOptionWrapper = new DoubleOptionWrapper
  var lostMinutes: DoubleOptionWrapper = new DoubleOptionWrapper

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
    inputCount = self.inputCount mergeSum other.inputCount
    outputCount = self.outputCount mergeSum other.outputCount
    dayOutput = self.dayOutput mergeSum other.dayOutput
    outputMinutes = self.outputMinutes mergeSum other.outputMinutes
    lostMinutes = self.lostMinutes mergeSum other.lostMinutes
  }
}
