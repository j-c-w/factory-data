package backend.scala.datatypes.builders

import backend.scala.datatypes.AbsenteeismData
import backend.scala.datatypes.options.{NoDouble, DoubleOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class AbsenteeismDataBuilder extends BuilderType[AbsenteeismData, AbsenteeismDataBuilder] {
  self =>

  var loRegistered: DoubleOption = NoDouble
  var hlRegistered: DoubleOption = NoDouble
  var loActual: DoubleOption = NoDouble
  var hlActual: DoubleOption = NoDouble
  var actualManpowerTotal: DoubleOption = NoDouble
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
    new AbsenteeismData(loRegistered, hlRegistered, loActual, hlActual,
      actualManpowerTotal, machines, loPresent,
      loAbsent, hlPresent, hlAbsent)

  /*
   * This takes another builder object of the same type
   * and merges it to create a single builder
   */
  override def mergeSum(other: AbsenteeismDataBuilder): AbsenteeismDataBuilder = new AbsenteeismDataBuilder {
    loRegistered = self.loRegistered + other.loRegistered
    hlRegistered = self.hlRegistered + other.hlRegistered
    loActual = self.loActual + other.loActual
    hlActual = self.hlActual + other.hlActual
    actualManpowerTotal = self.actualManpowerTotal + other.actualManpowerTotal
    machines = self.machines + other.machines
    loPresent = self.loPresent + other.loPresent
    loAbsent = self.loAbsent + other.loAbsent
    hlPresent = self.hlPresent + other.hlPresent
    hlAbsent = self.hlAbsent + other.hlAbsent
  }
}
