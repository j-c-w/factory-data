package backend.scala.datatypes.builders

import backend.scala.datatypes.Options.{NoString, StringOption}
import backend.scala.datatypes.OrderData
import backend.scala.datatypes.options.{NoDouble, NoInteger, DoubleOption, IntegerOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class OrderDataBuilder extends BuilderType[OrderData, OrderDataBuilder] {
  self =>

  var item: StringOption = NoString
  var style: StringOption = NoString
  var orderNo: IntegerOption = NoInteger
  var orderQuantity: IntegerOption = NoInteger
  var buyer: StringOption = NoString
  var smv: DoubleOption = NoDouble
  var runningDays: IntegerOption = NoInteger
  var runningDaysNA: IntegerOption = NoInteger



  /*
     * This method is used to turn the builder
     * into a type of T.
     */
  override def build: OrderData =
    new OrderData(item, style, orderNo, orderQuantity, buyer, smv, runningDays, runningDaysNA)

  /*
   * This takes another builder object of the same type
   * and merges it to create a single builder
   */
  override def mergeSum(other: OrderDataBuilder): OrderDataBuilder = new OrderDataBuilder {
    item = self.item  mergeEqual other.item
    style = self.style mergeEqual other.style
    orderNo = self.orderNo mergeEqual other.orderNo
    orderQuantity = self.orderQuantity mergeEqual other.orderQuantity
    buyer = self.buyer mergeEqual other.buyer
    smv = self.smv mergeEqual other.smv
    runningDays = self.runningDays mergeEqual other.runningDays
    runningDaysNA = self.runningDaysNA mergeEqual other.runningDaysNA
  }
}
