package backend.scala.datatypes.builders

import backend.scala.datatypes.options.wrappers.DoubleOptionWrapper
import backend.scala.datatypes.TargetData
import backend.scala.datatypes.options.{DoubleOption, NoDouble}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class TargetDataBuilder extends BuilderType[TargetData, TargetDataBuilder] {
  self =>

  var availableMinutes: DoubleOptionWrapper = new DoubleOptionWrapper
  var outputTarget: DoubleOptionWrapper = new DoubleOptionWrapper
  var totalTarget: DoubleOptionWrapper = new DoubleOptionWrapper

  /*
     * This method is used to turn the builder
     * into a type of T.
     */
  override def build: TargetData =
    new TargetData(availableMinutes, outputTarget, totalTarget)

  /*
   * This takes another builder object of the same type
   * and merges it to create a single builder
   */
  override def mergeSum(other: TargetDataBuilder): TargetDataBuilder = new TargetDataBuilder{
    availableMinutes = self.availableMinutes mergeSum other.availableMinutes
    outputTarget = self.outputTarget mergeSum other.outputTarget
    totalTarget = self.totalTarget mergeSum other.totalTarget
  }
}
