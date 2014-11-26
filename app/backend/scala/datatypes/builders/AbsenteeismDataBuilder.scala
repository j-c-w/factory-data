package backend.scala.datatypes.builders

import backend.scala.datatypes.AbsenteeismData
import backend.scala.datatypes.options.wrappers.DoubleOptionWrapper
import backend.scala.datatypes.options.{NoDouble, DoubleOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class AbsenteeismDataBuilder extends BuilderType[AbsenteeismData, AbsenteeismDataBuilder] {
  self =>

  var machines: DoubleOptionWrapper = new DoubleOptionWrapper
  var loPresent: DoubleOptionWrapper = new DoubleOptionWrapper
  var loAbsent: DoubleOptionWrapper = new DoubleOptionWrapper
  var hlPresent: DoubleOptionWrapper = new DoubleOptionWrapper
  var hlAbsent: DoubleOptionWrapper = new DoubleOptionWrapper

  /*
     * This method is used to turn the builder
     * into a type of T.
     */
  override def build: AbsenteeismData =
    new AbsenteeismData(machines, loPresent,
      loAbsent, hlPresent, hlAbsent)

  /*
   * This takes another builder object of the same type
   * and merges it to create a single builder
   */
  override def mergeSum(other: AbsenteeismDataBuilder): AbsenteeismDataBuilder = new AbsenteeismDataBuilder {
    machines = self.machines mergeSum other.machines
    loPresent = self.loPresent mergeSum other.loPresent
    loAbsent = self.loAbsent mergeSum other.loAbsent
    hlPresent = self.hlPresent mergeSum other.hlPresent
    hlAbsent = self.hlAbsent mergeSum other.hlAbsent
  }
}
