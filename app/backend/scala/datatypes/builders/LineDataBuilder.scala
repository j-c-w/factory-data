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

  def build: LineData = new LineData(
    lineCode
  )

  def mergeSum(other: LineDataBuilder) = new LineDataBuilder {
    lineCode = self.lineCode mergeEqual other.lineCode
  }
}
