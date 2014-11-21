package backend.scala


import java.io.File
import java.util.Date

import backend.scala.datatypes.{FactoryDate, DataType, LineListObject}
import backend.scala.graphing.{LineGraphData, Graph, BarChartData}
import backend.scala.graphing.data.DataParser
import backend.scala.query._
import controllers.Global


/*
 * Created by Jackson Woodruff on 20/07/2014 
 *
 *
 * This is a temporary class used for testing
 * putposts until I get the other things together
 */

object Backend {
  def loadRaw: Array[ResultListObject[LineListObject]] =
    new NoAggregate().aggregate(Global.baseData).toArray
}
