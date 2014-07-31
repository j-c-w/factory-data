package backend.scala


import java.io.File
import java.util.Date

import backend.scala.datatypes.{FactoryDate, DataType, LineListObject}
import backend.scala.graphing.{LineGraphData, Graph, BarChartData}
import backend.scala.graphing.data.DataParser
import backend.scala.query._


/*
 * Created by Jackson Woodruff on 20/07/2014 
 *
 *
 * This is a temporary class used for testing
 * putposts until I get the other things together
 */

object Backend {
  def loadData: Array[String] = {
    val x = loadRaw
    println(x.length)

    x map (_.toHtml)
  }

  def loadRaw: Array[ResultListObject[LineListObject]] =
    (performOperations(DataLoader.dataAsList)).toArray

  def drawGraph(data: List[ResultListObject[LineListObject]]): File = {
    val barChartData =
      new BarChartData[FactoryDate, LineListObject](
        x => x.lineObject.getDate.get, x => x.lineObject.getTotalProductionWorkers.get, "1", data.toList
      )
    val imageLocation = Graph.drawBarChart(barChartData, "Title", "xAxis", "yAxis")
    println(imageLocation)
    imageLocation
  }


  def performOperations(list: List[LineListObject]) =
    new SortBuilder[LineListObject](dateSort, true).sortBy(
      new NoAggregate[LineListObject].aggregate(
      //new AggregateAverageBy[Date, LineListObject](_.date.get).aggregate(
        generateFilter.filter(list)))


  def generateFilter =
    new FilterBuilder[LineListObject](!_.get.lineCode.isEmpty).and(!_.get.date.isEmpty).and(
      x => !x.get.getTotalProductionWorkers.isEmpty && x.get.getTotalProductionWorkers.get > 0)

  def generateSort =
    new SortBuilder[LineListObject](lineSort, true)

  def dateSort(res1: ResultListObject[LineListObject], res2: ResultListObject[LineListObject]): Boolean = //res match {
   // case (res1, res2) =>
    res1.lineObject.date.get.compareTo (res2.lineObject.date.get) > 0
   // }

  def lineSort (res1: ResultListObject[LineListObject], res2: ResultListObject[LineListObject]): Boolean =
    res1.lineObject.lineCode.get > res2.lineObject.lineCode.get
}
