package backend.scala.datatypes

import backend.scala.datatypes.builders.QualityDataBuilder
import backend.scala.datatypes.options.{SomeDouble, DoubleOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class QualityData(val totalChecked: DoubleOption,
                  val totalQCPass: DoubleOption,
                  val altered: DoubleOption,
                  val spot: DoubleOption,
                  val reject: DoubleOption,
                  val fabricError: DoubleOption) extends ImplementedDataType[QualityData, QualityDataBuilder] {
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
  override def get: QualityData = this

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
  override def averageBy(number: Int): QualityData = new QualityDataBuilder {
    totalChecked = self.totalChecked / SomeDouble(number.toDouble)
    totalQCPass = self.totalQCPass / SomeDouble(number.toDouble)
    altered = self.altered / SomeDouble(number.toDouble)
    spot = self.spot / SomeDouble(number.toDouble)
    reject = self.reject / SomeDouble(number.toDouble)
    fabricError = self.fabricError / SomeDouble(number.toDouble)
  }.build

  /*
   * returns a builder object for this item.
   */
  override def toBuilder: QualityDataBuilder = new QualityDataBuilder {
    totalChecked = self.totalChecked
    totalQCPass = self.totalQCPass
    altered = self.altered
    spot = self.spot
    reject = self.reject
    fabricError = self.fabricError
  }
}
