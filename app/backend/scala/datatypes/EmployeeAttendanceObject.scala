package backend.scala.datatypes

import backend.scala.datatypes.builders.{EmployeeAttendanceObjectBuilder, BuilderType}
import backend.scala.datatypes.options.{DoubleOption, SomeDouble, IntegerOption}

/*
 * Created by Jackson Woodruff on 19/09/2014 
 *
 * This class stores the basic attendance data
 * for a single employee type (i.e. LO or HL
 */

class EmployeeAttendanceObject (val registered: DoubleOption,
                                   val actual: DoubleOption,
                                   val present: DoubleOption,
                                   val absent: DoubleOption) extends DataType[EmployeeAttendanceObject] {

  self =>

  override type Self = EmployeeAttendanceObject

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
  override def get: EmployeeAttendanceObject = this

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
  override def mergeSum(other: EmployeeAttendanceObject): EmployeeAttendanceObject = new EmployeeAttendanceObjectBuilder {
    registered = self.registered + other.registered
    actual = self.actual + other.actual
    present = self.present + other.absent
    absent = self.absent + other.absent
  }.build

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
  override def averageBy(number: Int): EmployeeAttendanceObject = new EmployeeAttendanceObjectBuilder {
    registered = self.registered / SomeDouble(number)
    actual = self.actual / SomeDouble(number)
    present = self.present / SomeDouble(number)
    absent = self.absent / SomeDouble(number)
  }.build

  /*
     * returns a builder object for this item.
     */
  override def toBuilder: BuilderType[EmployeeAttendanceObject] = new EmployeeAttendanceObjectBuilder {
    registered = self.registered
    actual = self.actual
    present = self.present
    absent = self.absent
  }

  /*
  * returns an Html representation of this peice of
  * data. Used for displaying the data in table format
  *
  */
  override def toHtml: String = "<tb>" + List(registered, actual, present, absent).mkString("</tb><tb>") + "</tb>"
}
