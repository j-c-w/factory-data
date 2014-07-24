package main.scala.query

import main.scala.datatypes.DataType

/*
 * Created by Jackson Woodruff on 22/07/2014 
 * 
 */

class AggregateBuilder[T <: DataType](f: List[T] => List[ResultListObject[T]]) {
  def this() = this(_.map (x => new ResultListObject[T](x)))
  
  def aggregateSum(list: List[T]): ResultListObject[T] = {
    val functionApplied = f(list)
    (functionApplied.tail fold functionApplied.head) ((x, y) => {
      x mergeSum y
    }.asInstanceOf)
  }

  def noAggregate(list: List[T]): List[ResultListObject[T]] =
    f(list)

  //aggregates by a given stat, given by the aggregateBy function.
  //Averages the data points
  //(listObject) => listObject.date for date separation
  def aggregateAverageBy[K](list: List[T], aggregateBy: T => K): List[ResultListObject[T]] =
    aggregateSumBy(list, aggregateBy) map (_.averageBy)
  
  //aggregates by a given stat, summing the data points
  def aggregateSumBy[K](list: List[T], aggregateBy: T => K): List[ResultListObject[T]] =
    (list groupBy (aggregateBy)).map { case (k, resultList) => aggregateSum(resultList)}.toList

}
