package backend.scala.datatypes.builders

import backend.scala.datatypes.options.wrappers.DoubleOptionWrapper
import backend.scala.datatypes.QualityData
import backend.scala.datatypes.options.{NoDouble, DoubleOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class QualityDataBuilder extends BuilderType[QualityData, QualityDataBuilder] {
  self =>

  var totalChecked: DoubleOptionWrapper = new DoubleOptionWrapper
  var reject: DoubleOptionWrapper = new DoubleOptionWrapper
  var defect: DoubleOptionWrapper = new DoubleOptionWrapper

  /*
     * This method is used to turn the builder
     * into a type of T.
     */
  override def build: QualityData =
    new QualityData(totalChecked, reject, defect)

  /*
   * This takes another builder object of the same type
   * and merges it to create a single builder
   */
  override def mergeSum(other: QualityDataBuilder): QualityDataBuilder = new QualityDataBuilder {
    totalChecked = self.totalChecked mergeSum other.totalChecked
    reject = self.reject mergeSum other.reject
    defect = self. defect mergeSum other.defect
  }
}
