package backend.scala.datatypes

import backend.scala.datatypes.builders.{AttendanceDataObjectBuilder, BuilderType}
import backend.scala.datatypes.options.{SomeDouble, DoubleOption}

/*
 * Created by Jackson Woodruff on 19/09/2014 
 *
 * This class is a smaller class that combines two
 * EmployeeAttendanceObjects.
 *
 *
 */

class AttendanceDataObject (val lineOperatorData: => EmployeeAttendanceObject,
                            val helperData: => EmployeeAttendanceObject,
                            val manpowerTotal: => DoubleOption,
                            val machines: => DoubleOption) extends DataType[AttendanceDataObject] {
  self =>
  
  override type Self = AttendanceDataObject

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
  override def get: AttendanceDataObject = this

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
  override def mergeSum(other: AttendanceDataObject): AttendanceDataObject = new AttendanceDataObjectBuilder {
    lineOperatorData = self.lineOperatorData mergeSum other.lineOperatorData
    helperData = self.helperData mergeSum other.helperData
    manpowerTotal = self.manpowerTotal + other.manpowerTotal
    machines = self.machines + other.machines
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
  override def averageBy(number: Int): AttendanceDataObject = new AttendanceDataObjectBuilder {
    lineOperatorData = self.lineOperatorData averageBy number
    helperData = self.helperData averageBy number
    manpowerTotal = self.manpowerTotal / SomeDouble(number)
    machines = self.machines / SomeDouble(number)
  }.build

  /*
     * returns a builder object for this item.
     */
  override def toBuilder: BuilderType[AttendanceDataObject] = new AttendanceDataObjectBuilder {
    lineOperatorData = self.lineOperatorData
    helperData = self.helperData
    manpowerTotal = self.manpowerTotal
    machines = self.machines
  }

  /*
  * returns an Html representation of this peice of
  * data. Used for displaying the data in table format
  *
  */
  override def toHtml: String =
    lineOperatorData.toHtml +
    helperData.toHtml +
    "<tb>" + manpowerTotal + "</tb>" +
    "<tb>" + machines + "</tb>"
}
