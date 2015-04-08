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
  implicit val toDoubleOptionOrNone = DoubleOption.toDoubleOptionOrNone _
  implicit val toStringOption = StringOption(_)


  def rowToObject(list: List[String]): LineListObject = new LineListObject(
    getDate(list(15), DayOfWeekOption.fromOrderString(list(14))),
    new LineData(list(1)),
    new OrderData(list(2), list(3)),
    new TargetData(list(7), list(9), list(4)),
    new IOData(list(8), list(5)),
    new QualityData(list(11), list(12), list(13)),
    new AbsenteeismData(list(6), list(10)),
    IntegerOption(1), list(0)
  )

  private def getDate(date: String, dayOfWeek: DayOfWeekOption) = {
    Try {
      val splitDate = date.split("/")

      new FactoryDate(splitDate(0),
      splitDate(1),
      splitDate(2), dayOfWeek)}.getOrElse(
        new FactoryDate(NoInteger, NoInteger, NoInteger, dayOfWeek)
      )
  }

  def dataAsList: List[LineListObject] = loadData.drop(1).map(rowToObject)
}
