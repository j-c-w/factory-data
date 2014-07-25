package main.backend.scala.query

import main.backend.scala.datatypes.{LineListObject, DataType}

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

class ResultListObject[T <: DataType[T]](val lineObject: T, numberAggregated: Int) {
  //the constructor for most cases -- only 1 object has been combined
  //to make this
  def this(lineObject: T) = this(lineObject, 1)

  //adds one to the number aggregated
  def mergeSum(other: ResultListObject[T]) = {
    new ResultListObject[T](
      (lineObject.get mergeSum other.lineObject.get), numberAggregated + 1)
  }

  //averages by the number aggregated.
  // very useful for taking the averages
  def averageBy: ResultListObject[T] =
    averageBy(numberAggregated)

  def averageBy(number: Int): ResultListObject[T] =
    new ResultListObject((lineObject averageBy number))

  override def toString = lineObject.toString
}
