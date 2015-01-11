package backend.scala

import java.text.SimpleDateFormat
import java.util.{Locale, Date}
import backend.scala.datatypes.options._
import backend.scala.datatypes._
import controllers.Global
import main.backend.csv.scala.com.github.tototoshi.csv.CSVReader

import scala.collection.parallel.immutable.ParSeq
import scala.reflect.ClassTag
import scala.util.Try


/*
 * Created by Jackson Woodruff on 20/07/2014 
 *
 * For converting CSV files into ListObjects
 *
 * Until there is a database with fixed headers,
 * this will only work for one database --
 * currently wired to data from factory 1004
 */

object DataLoader {
  val reader = CSVReader.open(Global.dataCVSLocation)

  def loadData: List[List[String]] = {
    printf("Loading Data From CSV")
    reader.all
  }
  
  implicit val toIntegerOptionOrNone = IntegerOption.toIntegerOptionOrNone _
  implicit val toDoubleOptionOrNone = DoubleOption.toDoubleOption _


  def rowToObject(list: List[String]): LineListObject = new LineListObject(
    getDate(list(1)),
    new LineData(list(2)),
    new OrderData(list(3), list(4)),
    new TargetData(list(4), list(6)),
    new IOData(list(4), list(4)),
    new QualityData(list(8), list(9), list(10)),
    new AbsenteeismData(list(5), list(7)),
    IntegerOption(1), list(0)
  )

  private def getDate(date: String) = {
    Try {
      val splitDate = date.split("/")

      new FactoryDate(splitDate(1),
      splitDate(0),
      splitDate(2))}.getOrElse(
        new FactoryDate(NoInteger, NoInteger, NoInteger)
      )
  }

  def dataAsList: List[LineListObject] = loadData.drop(1).map(rowToObject)
}
