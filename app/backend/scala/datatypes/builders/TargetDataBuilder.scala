package backend.scala.datatypes.builders

import backend.scala.datatypes.TargetData
import backend.scala.datatypes.options.{DoubleOption, NoDouble}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class TargetDataBuilder extends BuilderType[TargetData, TargetDataBuilder] {
  self =>

  var targetMinutes: DoubleOption = NoDouble
  var hours: DoubleOption = NoDouble
  var availableMinutes: DoubleOption = NoDouble

  /*
     * This method is used to turn the builder
     * into a type of T.
     */
  override def build: TargetData =
    new TargetData(targetMinutes, hours, availableMinutes)

  /*
   * This takes another builder object of the same type
   * and merges it to create a single builder
   */
  override def mergeSum(other: TargetDataBuilder): TargetDataBuilder = new TargetDataBuilder{
    targetMinutes = self.targetMinutes + other.targetMinutes
    hours = self.hours + other.hours
    availableMinutes = self.availableMinutes + other.availableMinutes
  }
}
