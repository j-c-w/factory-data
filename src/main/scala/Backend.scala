package main.scala


import java.util.Date

import main.scala.datatypes._
import main.scala.datatypes.builders.LineListObjectBuilder
import main.scala.datatypes.options.IntegerOption
import main.scala.query.{SortBuilder, AggregateBuilder, FilterBuilder, ResultListObject}


/*
 * Created by Jackson Woodruff on 20/07/2014 
 * 
 */

object Backend {
  def loadData() = {
    val x = (performOperations(DataLoader.dataAsList)).toArray
    println(x.length)
    x
  }

  def performOperations(list: List[LineListObject]) =
    (generateAggregator.aggregateBy(generateFilter.filter(list), _.lineCode.get))

  def generateFilter =
    new FilterBuilder(!_.lineCode.isEmpty).and(!_.date.isEmpty).and(
      x => !x.getTotalProductionWorkers.isEmpty && x.getTotalProductionWorkers.get > 0)

  def generateAggregator =
    new AggregateBuilder(_.map(x => new ResultListObject(x)))

  def generateSort =
    new SortBuilder(dateSort, true).add(lineSort)

  def dateSort(res1: ResultListObject, res2: ResultListObject): Boolean = //res match {
   // case (res1, res2) =>
    res1.lineObject.date.get.compareTo (res2.lineObject.date.get) > 0
 // }

  def lineSort (res1: ResultListObject, res2: ResultListObject): Boolean =
    res1.lineObject.lineCode.get > res2.lineObject.lineCode.get
}
