package backend.scala.datatypes.builders

import backend.scala.datatypes.LineData
import backend.scala.datatypes.options.{NoInteger, IntegerOption}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class LineDataBuilder extends  BuilderType[LineData, LineDataBuilder] {
  self =>

  var lineCode: IntegerOption = NoInteger
  var merged: IntegerOption = NoInteger
  var mergedWith1: IntegerOption = NoInteger
  var mergedWith2: IntegerOption = NoInteger
  var splitLine: IntegerOption = NoInteger

  def build: LineData = new LineData(
    lineCode, merged, mergedWith1, mergedWith2, splitLine
  )

  def mergeSum(other: LineDataBuilder) = new LineDataBuilder {
    lineCode = self.lineCode mergeEqual other.lineCode
    merged = self.merged mergeEqual other.merged
    mergedWith1 = self.mergedWith1 mergeEqual other.mergedWith1
    mergedWith2 = self.mergedWith1 mergeEqual other.mergedWith2
    splitLine = self.splitLine mergeEqual other.splitLine
  }
}
