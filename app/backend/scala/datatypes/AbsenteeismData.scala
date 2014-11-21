package backend.scala.datatypes

import backend.scala.datatypes.builders.AbsenteeismDataBuilder
import backend.scala.datatypes.options.{SomeDouble, DoubleOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class AbsenteeismData(val loRegistered: DoubleOption,
                      val hlRegistered: DoubleOption,
                      val loActual: DoubleOption,
                      val hlActual: DoubleOption,
                      val actualManpowerTotal: DoubleOption,
                      val machines: DoubleOption,
                      val loPresent: DoubleOption,
                      val loAbsent: DoubleOption,
                      val hlPresent: DoubleOption,
                      val hlAbsent: DoubleOption) extends ImplementedDataType[AbsenteeismData, AbsenteeismDataBuilder] {
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
    loRegistered = self.loRegistered / SomeDouble(number.toDouble)
    hlRegistered = self.hlRegistered / SomeDouble(number.toDouble)
    loActual = self.loActual / SomeDouble(number.toDouble)
    hlActual = self.hlActual / SomeDouble(number.toDouble)
    actualManpowerTotal = self.actualManpowerTotal / SomeDouble(number.toDouble)
    machines = self.machines / SomeDouble(number.toDouble)
    loPresent = self.loPresent / SomeDouble(number.toDouble)
    loAbsent = self.loAbsent / SomeDouble(number.toDouble)
    hlPresent = self.hlPresent / SomeDouble(number.toDouble)
    hlAbsent = self.hlAbsent / SomeDouble(number.toDouble)
  }.build

  /*
   * returns a builder object for this item.
   */
  override def toBuilder: AbsenteeismDataBuilder = new AbsenteeismDataBuilder {
    loRegistered = self.loRegistered
    hlRegistered = self.hlRegistered
    loActual = self.loActual
    hlActual = self.hlActual
    actualManpowerTotal = self.actualManpowerTotal
    machines = self.machines
    loPresent = self.loPresent
    loAbsent = self.loAbsent
    hlPresent = self.hlPresent
    hlAbsent = self.hlAbsent
  }
}
