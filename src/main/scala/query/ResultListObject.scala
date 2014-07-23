package main.scala.query

import main.scala.datatypes.LineListObject

/*
 * Created by Jackson Woodruff on 22/07/2014 
 *
 * This is a list object that stores any number of
 * different params, without names -- not idea for
 * manipulation, but for a result of a AggregateBuilder,
 * where most of the data is missing anyways, it is
 * fine -- particularly for testing
 */

class ResultListObject(val lineObject: LineListObject) {
  def mergeAverage(other: ResultListObject) = {
    new ResultListObject(lineObject mergeAverage other.lineObject)
  }
  
  def mergeSum(other: ResultListObject) = {
    new ResultListObject(lineObject mergeSum other.lineObject)
  }
  override def toString = lineObject.toString
}
