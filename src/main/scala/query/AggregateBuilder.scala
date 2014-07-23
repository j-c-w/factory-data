package main.scala.query

import main.scala.datatypes.LineListObject

/*
 * Created by Jackson Woodruff on 22/07/2014 
 * 
 */

class AggregateBuilder(f: List[LineListObject] => List[ResultListObject]) {
  def add(f2: List[LineListObject] => List[ResultListObject]) =
    new AggregateBuilder(x => (f(x) zip f2(x)).map
      {case (result1, result2) => result1 merge result2})

  def aggregate(list: List[LineListObject]): ResultListObject = {
    val functionApplied = f(list)
    (functionApplied.tail foldLeft functionApplied.head) ((x, y) => x.merge(y))
  }

  //aggregates by a given stat, given by the aggregateBy function.
  //(listObject) => listObject.date for date separation
  def aggregateBy[K](list: List[LineListObject], aggregateBy: LineListObject => K):List[ResultListObject] =
    (list groupBy(aggregateBy)).map{ case (k, resultList) => aggregate(resultList) }.toList
}
