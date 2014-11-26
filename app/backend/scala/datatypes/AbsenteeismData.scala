package backend.scala.datatypes

import backend.scala.datatypes.options.wrappers.DoubleOptionWrapper
import backend.scala.datatypes.builders.AbsenteeismDataBuilder
import backend.scala.datatypes.options._

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class AbsenteeismData(val machines: DoubleOptionWrapper,
                      val loPresent: DoubleOptionWrapper,
                      val loAbsent: DoubleOptionWrapper,
                      val hlPresent: DoubleOptionWrapper,
                      val hlAbsent: DoubleOptionWrapper) extends ImplementedDataType[AbsenteeismData, AbsenteeismDataBuilder] {
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
  override def get: AbsenteeismData = this

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
  override def averageBy(number: Int): AbsenteeismData = new AbsenteeismDataBuilder {
    machines = self.machines.average
    loPresent = self.loPresent.average
    loAbsent = self.loAbsent.average
    hlPresent = self.hlPresent.average
    hlAbsent = self.hlAbsent.average
  }.build

  /*
   * returns a builder object for this item.
   */
  override def toBuilder: AbsenteeismDataBuilder = new AbsenteeismDataBuilder {
    machines = self.machines
    loPresent = self.loPresent
    loAbsent = self.loAbsent
    hlPresent = self.hlPresent
    hlAbsent = self.hlAbsent
  }
}
