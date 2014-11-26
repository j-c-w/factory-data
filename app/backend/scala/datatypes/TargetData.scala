package backend.scala.datatypes

import backend.scala.datatypes.options.wrappers.DoubleOptionWrapper
import backend.scala.datatypes.builders.{BuilderType, TargetDataBuilder}
import backend.scala.datatypes.options.{SomeDouble, DoubleOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class TargetData(val targetMinutes: DoubleOptionWrapper,
                 val hours: DoubleOptionWrapper,
                 val availableMinutes: DoubleOptionWrapper) extends ImplementedDataType[TargetData, TargetDataBuilder]{
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
  override def get: TargetData = this

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
  override def averageBy(number: Int): TargetData = new TargetDataBuilder {
    targetMinutes = self.targetMinutes.average
    hours = self.hours.average
    availableMinutes = self.availableMinutes.average
  }.build


  /*
   * returns a builder object for this item.
   */
  override def toBuilder: TargetDataBuilder = new TargetDataBuilder {
    targetMinutes = self.targetMinutes
    hours = self.hours
    availableMinutes = self.availableMinutes
  }
}
