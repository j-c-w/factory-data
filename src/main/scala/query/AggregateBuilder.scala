package main.scala.query

import main.scala.datatypes.LineListObject

/*
 * Created by Jackson Woodruff on 22/07/2014 
 * 
 */

class AggregateBuilder(f: List[LineListObject] => List[ResultListObject]) {
  def this() = this(_.map (x => new ResultListObject(x)))
  
  def aggregateSum(list: List[LineListObject]): ResultListObject = {
    val functionApplied = f(list)
    (functionApplied.tail fold functionApplied.head) ((x, y) => {
      x mergeSum y
    })
  }

  def noAggregate(list: List[LineListObject]): List[ResultListObject] =
    f(list)

  //aggregates by a given stat, given by the aggregateBy function.
  //Averages the data points
  //(listObject) => listObject.date for date separation
  def aggregateAverageBy[K](list: List[LineListObject], aggregateBy: LineListObject => K): List[ResultListObject] =
    aggregateSumBy(list, aggregateBy) map (_.averageBy)
  
  //aggregates by a given stat, summing the data points
  def aggregateSumBy[K](list: List[LineListObject], aggregateBy: LineListObject => K): List[ResultListObject] = 
    (list groupBy (aggregateBy)).map { case (k, resultList) => aggregateSum(resultList)}.toList

}
