package backend.scala.datatypes.builders

import backend.scala.datatypes.options.{NoString, StringOption}
import backend.scala.datatypes.OrderData
import backend.scala.datatypes.options.{NoDouble, NoInteger, DoubleOption, IntegerOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class OrderDataBuilder extends BuilderType[OrderData, OrderDataBuilder] {
  self =>

  var buyer: IntegerOption = NoInteger
  var smv: DoubleOption = NoDouble
  var runningDays: DoubleOption = NoDouble



  /*
     * This method is used to turn the builder
     * into a type of T.
     */
  override def build: OrderData =
    new OrderData(buyer, smv, runningDays)

  /*
   * This takes another builder object of the same type
   * and merges it to create a single builder
   */
  override def mergeSum(other: OrderDataBuilder): OrderDataBuilder = new OrderDataBuilder {
    buyer = self.buyer mergeEqual other.buyer
    smv = self.smv mergeEqual other.smv
    runningDays = self.runningDays + other.runningDays
  }
}
