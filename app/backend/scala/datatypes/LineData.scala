package backend.scala.datatypes

import backend.scala.datatypes.builders.{LineDataBuilder, BuilderType}
import backend.scala.datatypes.options.IntegerOption

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class LineData(val lineCode: IntegerOption,
                val merged: IntegerOption,
                val mergedWith1: IntegerOption,
                val mergedWith2: IntegerOption,
                val splitLine: IntegerOption) extends ImplementedDataType[LineData, LineDataBuilder] {
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
  override def get: LineData = this

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
     * Because this is all non-summable fields,
     * we are not going to be averaging anything here
     */
  override def averageBy(number: Int): LineData = this

  /*
     * returns a builder object for this item.
     */
  override def toBuilder: LineDataBuilder =
    new LineDataBuilder {
      lineCode = self.lineCode
      merged = self.merged
      mergedWith1 = self.mergedWith1
      mergedWith2 = self.mergedWith2
      splitLine = self.splitLine
    }
}
