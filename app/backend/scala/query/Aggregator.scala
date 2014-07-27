package backend.scala.query

import backend.scala.datatypes.DataType


/*
 * Created by Jackson Woodruff on 22/07/2014 
 * 
 */

object Aggregator {
  private def converter[T](list: List[T]) = list map (x => new ResultListObject[T](x))
  
  def aggregateSum[T](list: List[T]): ResultListObject[T] = {
    val functionApplied = converter(list)
    (functionApplied.tail fold functionApplied.head) ((x, y) => {
      x mergeSum y
    })
  }

  def noAggregate[T](list: List[T]): List[ResultListObject[T]] =
    converter(list)

  //aggregates by a given stat, given by the aggregateBy function.
  //Averages the data points
  //(listObject) => listObject.date for date separation
  def aggregateAverageBy[K, T](list: List[T], aggregateBy: T => K): List[ResultListObject[T]] =
    aggregateSumBy(list, aggregateBy) map (_.averageBy)
  
  //aggregates by a given stat, summing the data points
  def aggregateSumBy[K, T](list: List[T], aggregateBy: T => K): List[ResultListObject[T]] =
    (list groupBy (aggregateBy)).map { case (k, resultList) => aggregateSum(resultList)}.toList

}
