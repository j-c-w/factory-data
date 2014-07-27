package backend.scala


import java.util.Date

import backend.scala.datatypes.LineListObject
import backend.scala.graphing.{Graph, BarChartData}
import backend.scala.graphing.data.DataParser
import backend.scala.query.{SortBuilder, ResultListObject, Aggregator, FilterBuilder}


/*
 * Created by Jackson Woodruff on 20/07/2014 
 * 
 */

object Backend {
  def loadData(): Array[String] = {
    val x = (performOperations(DataLoader.dataAsList)).toArray
    println(x.length)

    x map (_.toString)
  }

  def performOperations(list: List[LineListObject]) =
    generateSort.sortBy(generateFilter.filter(list) map (new ResultListObject[LineListObject](_) ))

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
