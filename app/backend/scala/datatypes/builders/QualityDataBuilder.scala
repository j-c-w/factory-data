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

  var alterRate: DoubleOptionWrapper = new DoubleOptionWrapper
  var rejectRate: DoubleOptionWrapper = new DoubleOptionWrapper
  var spotRate: DoubleOptionWrapper = new DoubleOptionWrapper

  /*
     * This method is used to turn the builder
     * into a type of T.
     */
  override def build: QualityData =
    new QualityData(alterRate, spotRate, rejectRate)

  /*
   * This takes another builder object of the same type
   * and merges it to create a single builder
   */
  override def mergeSum(other: QualityDataBuilder): QualityDataBuilder = new QualityDataBuilder {
    alterRate = self.alterRate mergeSum other.alterRate
    rejectRate = self.rejectRate mergeSum other.rejectRate
    spotRate = self. spotRate mergeSum other.spotRate
  }
}
