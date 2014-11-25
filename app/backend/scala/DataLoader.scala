package backend.scala

import java.text.SimpleDateFormat
import java.util.{Locale, Date}
import backend.scala.datatypes.options._
import backend.scala.datatypes._
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
  val reader = CSVReader.open("C:\\Users\\Jackson\\Projects\\IPA\\Data\\harmonized data\\first_harmonised.csv")

  def loadData: List[List[String]] = {
    printf("Loading Data From CSV")
    reader.all
  }

  val dateFormat = new SimpleDateFormat("yyyy-MM-dd")//.format(new Date())

  def rowToObject(list: List[String]): LineListObject = new LineListObject(
    getDate(list(2)),
    new LineData(IntegerOption.toIntegerOptionOrNone(list(2)), IntegerOption.toIntegerOptionOrNone(list(3)), IntegerOption.toIntegerOptionOrNone(list(4)),
      IntegerOption.toIntegerOptionOrNone(list(5)), IntegerOption.toIntegerOptionOrNone(list(6))),
    new OrderData(IntegerOption.toIntegerOptionOrNone(list(7)), DoubleOption.toDoubleOptionOrNone(list(8)), DoubleOption.toDoubleOptionOrNone(list(9))),
    new TargetData(DoubleOption.toDoubleOptionOrNone(list(10)), DoubleOption.toDoubleOptionOrNone(list(11)), DoubleOption.toDoubleOptionOrNone(list(12))),
    new IOData(DoubleOption.toDoubleOptionOrNone(list(13)), DoubleOption.toDoubleOptionOrNone(list(14)), DoubleOption.toDoubleOptionOrNone(list(15)),
      DoubleOption.toDoubleOptionOrNone(list(16)), DoubleOption.toDoubleOptionOrNone(list(17))),
    new QualityData(DoubleOption.toDoubleOptionOrNone(list(18)), DoubleOption.toDoubleOptionOrNone(list(19)), DoubleOption.toDoubleOptionOrNone(list(25))),
    new AbsenteeismData(DoubleOption.toDoubleOptionOrNone(list(20)), DoubleOption.toDoubleOptionOrNone(list(21)), DoubleOption.toDoubleOptionOrNone(list(22)),
      DoubleOption.toDoubleOptionOrNone(list(23)), DoubleOption.toDoubleOptionOrNone(list(24))),
    IntegerOption(1), IntegerOption.toIntegerOptionOrNone(list(0))
  )

  private def getDate(date: String) = {
    val dateFormat = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(date)


    Try(new FactoryDate(IntegerOption(dateFormat.getDay),
      IntegerOption(dateFormat.getMonth),
      IntegerOption(dateFormat.getYear))).getOrElse(
        new FactoryDate(NoInteger, NoInteger, NoInteger)
      )
  }

  def dataAsList: List[LineListObject] = loadData.drop(1).map(rowToObject)
}
