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

  var outputMinutes: DoubleOptionWrapper = new DoubleOptionWrapper
  var dayOutput: DoubleOptionWrapper = new DoubleOptionWrapper

  /*
     * This method is used to turn the builder
     * into a type of T.
     */
  override def build: IOData =
    new IOData(outputMinutes, dayOutput)

  /*
   * This takes another builder object of the same type
   * and merges it to create a single builder
   */
  override def mergeSum(other: IODataBuilder): IODataBuilder = new IODataBuilder {
    outputMinutes = self.outputMinutes mergeSum other.outputMinutes
    dayOutput = self.dayOutput mergeSum other.dayOutput
  }
}
