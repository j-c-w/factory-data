package backend.scala.datatypes

/*
 * Created by Jackson Woodruff on 20/11/2014 
 *
 *
 * This class was created to resolve compatibility issues
 * where the classes using this expected to pass a single
 * parameter.
 *
 * However when I moved over to the harmonized data I decided
 * it would be better to have two type paramters.
 *
 * Thus this is here to provide the (many many) old classes
 * with the key functions that they still need
 */

trait DataType[T <: DataType[T]] {
  /*
   * This function takes another datatype of type T
   * and adds it to the current datatype by merging
   * all summable values into one.
   *
   * This process should consist of summing the values
   * that can be summed (i.e. total production workers)
   * and using common sense on all other values (i.e.
   * percents should be entirely discarded, line codes
   * should not be summed, but should be kept the same
   * if they are both equal etc.)
   */
  def mergeSum(other: T): T

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
  def averageBy(number: Int): T

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
  def get: T
}
