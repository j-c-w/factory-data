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


  def rowToObject(list: List[String]): LineListObject = new LineListObject(
    getDate(list(1)),
    new LineData(IntegerOption.toIntegerOptionOrNone(list(2)), IntegerOption.toIntegerOptionOrNone(list(3)), IntegerOption.toIntegerOptionOrNone(list(4)),
      IntegerOption.toIntegerOptionOrNone(list(5)), IntegerOption.toIntegerOptionOrNone(list(6))),
    new OrderData(IntegerOption.toIntegerOptionOrNone(list(7)), DoubleOption.toWrappedDoubleOrNone(list(8)), DoubleOption.toWrappedDoubleOrNone(list(9))),
    new TargetData(DoubleOption.toWrappedDoubleOrNone(list(10)), DoubleOption.toWrappedDoubleOrNone(list(11)), DoubleOption.toWrappedDoubleOrNone(list(12))),
    new IOData(DoubleOption.toWrappedDoubleOrNone(list(13)), DoubleOption.toWrappedDoubleOrNone(list(14)), DoubleOption.toWrappedDoubleOrNone(list(15)),
      DoubleOption.toWrappedDoubleOrNone(list(16)), DoubleOption.toWrappedDoubleOrNone(list(17))),
    new QualityData(DoubleOption.toWrappedDoubleOrNone(list(18)), DoubleOption.toWrappedDoubleOrNone(list(19)), DoubleOption.toWrappedDoubleOrNone(list(25))),
    new AbsenteeismData(DoubleOption.toWrappedDoubleOrNone(list(20)), DoubleOption.toWrappedDoubleOrNone(list(21)), DoubleOption.toWrappedDoubleOrNone(list(22)),
      DoubleOption.toWrappedDoubleOrNone(list(23)), DoubleOption.toWrappedDoubleOrNone(list(24))),
    IntegerOption(1), IntegerOption.toIntegerOptionOrNone(list(0))
  )

  private def getDate(date: String) = {
    Try {
      val splitDate = date.split("/")

      new FactoryDate(IntegerOption.toIntegerOptionOrNone(splitDate(1)),
      IntegerOption.toIntegerOptionOrNone(splitDate(0)),
      IntegerOption.toIntegerOptionOrNone(splitDate(2)))}.getOrElse(
        new FactoryDate(NoInteger, NoInteger, NoInteger)
      )
  }

  def dataAsList: List[LineListObject] = loadData.drop(1).map(rowToObject)
}
