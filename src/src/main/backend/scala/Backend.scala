package main.backend.scala


import java.util.Date

import main.backend.scala.datatypes._
import main.backend.scala.datatypes.builders.LineListObjectBuilder
import main.backend.scala.datatypes.options.IntegerOption
import main.backend.scala.query.{SortBuilder, AggregateBuilder, FilterBuilder, ResultListObject}


/*
 * Created by Jackson Woodruff on 20/07/2014 
 * 
 */

object Backend {
  //todo -- I think I may have cracked the abstract type problem -- test it by fixing errors here
  def loadData() = {
    val x = (performOperations(DataLoader.dataAsList)).toArray
    println(x.length)
    x
  }

  def performOperations(list: List[LineListObject]) =
    generateSort.sortBy(generateAggregator.aggregateAverageBy(generateFilter.filter(list), _.lineCode.get))

  def generateFilter =
    new FilterBuilder[LineListObject](!_.get.lineCode.isEmpty).and(!_.get.date.isEmpty).and(
      x => !x.get.getTotalProductionWorkers.isEmpty && x.get.getTotalProductionWorkers.get > 0)

  def generateAggregator =
    new AggregateBuilder[LineListObject](_.map(x => new ResultListObject(x)))

  def generateSort =
    new SortBuilder[LineListObject](lineSort, true)

  def dateSort(res1: ResultListObject[LineListObject], res2: ResultListObject[LineListObject]): Boolean = //res match {
   // case (res1, res2) =>
    res1.lineObject.date.get.compareTo (res2.lineObject.date.get) > 0
   // }

  def lineSort (res1: ResultListObject[LineListObject], res2: ResultListObject[LineListObject]): Boolean =
    res1.lineObject.lineCode.get > res2.lineObject.lineCode.get
}
