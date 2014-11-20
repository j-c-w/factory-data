package backend.scala.datatypes

import backend.scala.datatypes.Options.StringOption
import backend.scala.datatypes.builders.{BuilderType, OrderDataBuilder}
import backend.scala.datatypes.options.{DoubleOption, IntegerOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class OrderData(val item: StringOption,
                val style: StringOption,
                val orderNo: IntegerOption,
                val orderQuantity: IntegerOption,
                val buyer: StringOption,
                val smv: DoubleOption,
                val runningDays: IntegerOption,
                val runningDaysNA: IntegerOption) extends ImplementedDataType[OrderData, OrderDataBuilder] {
  self =>

  override type Self = this.type

  /*
     * Because every instance of DataType[T] should
     * have itself as T, this function returns the
     * object that is held in this wrapper in pure
     * form.
     *
     * This is important for the use of anonymous
     * functions, because it allows Scala's typechecker
     * to compute the true type of the object and
     * allows access to its fields in more complicated
     * functions.
     *
     * Suggested implementation :
     *    def get: T = this
     */
  override def get: OrderData = this

  /*
     * This is used in the aggregateByAverage function.
     *
     * It should divide all divisible numbers (hint:
     * the numbers that were summed in the merge sum
     * function) by the number provided.
     *
     * All other fields (date, factory code etc.)
     * should be ignored completely.
     */
  override def averageBy(number: Int): OrderData = new OrderDataBuilder {
    item = self.item
    style = self.style
    orderNo = self.orderNo
    orderQuantity = self.orderQuantity
    buyer = self.buyer
    smv = self.smv
    runningDays = self.runningDays
    runningDaysNA = self.runningDaysNA
  }.build

  /*
   * returns a builder object for this item.
   */
  override def toBuilder: OrderDataBuilder = new OrderDataBuilder {
    item = self.item
    style = self.style
    orderNo = self.orderNo
    orderQuantity = self.orderQuantity
    buyer = self.buyer
    smv = self.smv
    runningDays = self.runningDays
    runningDaysNA = self.runningDaysNA
  }
}
