package backend.scala.datatypes.builders

import backend.scala.datatypes.AbsenteeismData
import backend.scala.datatypes.options.wrappers.DoubleOptionWrapper
import backend.scala.datatypes.options.{NoDouble, DoubleOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class AbsenteeismDataBuilder extends BuilderType[AbsenteeismData, AbsenteeismDataBuilder] {
  self =>

  var totalPresent: DoubleOptionWrapper = new DoubleOptionWrapper
  var absentRate: DoubleOptionWrapper = new DoubleOptionWrapper

  /*
     * This method is used to turn the builder
     * into a type of T.
     */
  override def build: AbsenteeismData =
    new AbsenteeismData(totalPresent, absentRate)

  /*
   * This takes another builder object of the same type
   * and merges it to create a single builder
   */
  override def mergeSum(other: AbsenteeismDataBuilder): AbsenteeismDataBuilder = new AbsenteeismDataBuilder {
    totalPresent = self.totalPresent mergeSum other.totalPresent
    absentRate = self.absentRate mergeSum other.absentRate
  }
}
