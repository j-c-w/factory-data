package backend.scala.datatypes

import backend.scala.datatypes.builders.{FactoryDateBuilder, BuilderType}
import backend.scala.datatypes.options.IntegerOption

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class FactoryDate(val day: IntegerOption,
                  val month: IntegerOption,
                  val year: IntegerOption) extends ImplementedDataType[FactoryDate, FactoryDateBuilder] {
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
  override def get: FactoryDate = this

  /*
     * This is used in the aggregateByAverage function.
     *
     * It should divide all divisible numbers (hint:
     * the numbers that were summed in the merge sum
     * function) by the number provided.
     *
     * All other fields (date, factory code etc.)
     * should be ignored completely.
     *
     * Because you can't average a date, this should
     * be left untouched
     */
  override def averageBy(number: Int): FactoryDate =
    this

  /*
     * returns a builder object for this item.
     */
  override def toBuilder: FactoryDateBuilder =
    new FactoryDateBuilder {
      month = self.month
      year = self.year
      day = self.day
    }
}
