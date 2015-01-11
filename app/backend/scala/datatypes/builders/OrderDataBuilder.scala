package backend.scala.datatypes.builders

import backend.scala.datatypes.options.wrappers.DoubleOptionWrapper
import backend.scala.datatypes.options.{NoString, StringOption}
import backend.scala.datatypes.OrderData
import backend.scala.datatypes.options.{NoDouble, NoInteger, DoubleOption, IntegerOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class OrderDataBuilder extends BuilderType[OrderData, OrderDataBuilder] {
  self =>

  var smv: DoubleOptionWrapper = new DoubleOptionWrapper
  var runningDays: DoubleOptionWrapper = new DoubleOptionWrapper



  /*
     * This method is used to turn the builder
     * into a type of T.
     */
  override def build: OrderData =
    new OrderData(smv, runningDays)

  /*
   * This takes another builder object of the same type
   * and merges it to create a single builder
   */
  override def mergeSum(other: OrderDataBuilder): OrderDataBuilder = new OrderDataBuilder {
    smv = self.smv mergeEqual other.smv
    runningDays = self.runningDays mergeSum other.runningDays
  }
}
