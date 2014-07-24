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
 *
 * numberAggregated is for the averaging class -- used to average
 *
 */

class ResultListObject(val lineObject: LineListObject, numberAggregated: Int) {
  //the constructor for most cases -- only 1 object has been combined
  //to make this
  def this(lineObject: LineListObject) = this(lineObject, 1)

  //adds one to the number aggregated
  def mergeSum(other: ResultListObject) = {
    new ResultListObject(lineObject mergeSum other.lineObject, numberAggregated + 1)
  }

  //averages by the number aggregated.
  // very useful for taking the averages
  def averageBy: ResultListObject =
    averageBy(numberAggregated)

  def averageBy(number: Int): ResultListObject =
    new ResultListObject(lineObject averageBy number)

  override def toString = lineObject.toString
}
