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
  var sLine: IntegerOption = NoInteger
  var lineStatus: IntegerOption = NoInteger
  var merged: IntegerOption = NoInteger
  var mergedWith: IntegerOption = NoInteger
  var splitLine: IntegerOption = NoInteger

  def build: LineData = new LineData(
    lineCode, sLine, lineStatus, merged, mergedWith, splitLine
  )

  def mergeSum(other: LineDataBuilder) = new LineDataBuilder {
    lineCode = self.lineCode mergeEqual other.lineCode
    sLine = self.sLine mergeEqual other.sLine
    lineStatus = self.lineStatus mergeEqual other.lineStatus
    merged = self.merged mergeEqual other.merged
    mergedWith = self.mergedWith mergeEqual other.mergedWith
    splitLine = self.splitLine mergeEqual other.splitLine
  }
}
