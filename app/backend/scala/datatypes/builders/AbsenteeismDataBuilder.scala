package backend.scala.datatypes.builders

import backend.scala.datatypes.AbsenteeismData
import backend.scala.datatypes.options.{NoDouble, DoubleOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class AbsenteeismDataBuilder extends BuilderType[AbsenteeismData, AbsenteeismDataBuilder] {
  self =>

  var machines: DoubleOption = NoDouble
  var loPresent: DoubleOption = NoDouble
  var loAbsent: DoubleOption = NoDouble
  var hlPresent: DoubleOption = NoDouble
  var hlAbsent: DoubleOption = NoDouble

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
    machines = self.machines + other.machines
    loPresent = self.loPresent + other.loPresent
    loAbsent = self.loAbsent + other.loAbsent
    hlPresent = self.hlPresent + other.hlPresent
    hlAbsent = self.hlAbsent + other.hlAbsent
  }
}
