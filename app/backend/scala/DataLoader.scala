package backend.scala

import java.text.SimpleDateFormat
import java.util.Date
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
    new LineData(IntegerOption.toIntegerOptionOrNone(list(3)), IntegerOption.toIntegerOptionOrNone(list(4)), IntegerOption.toIntegerOptionOrNone(list(5)),
      IntegerOption.toIntegerOptionOrNone(list(6)), IntegerOption.toIntegerOptionOrNone(list(7)), IntegerOption.toIntegerOptionOrNone(list(8))),
    new OrderData(SomeString(list(9)), SomeString(list(10)), IntegerOption.toIntegerOptionOrNone(list(11)), DoubleOption.toDoubleOptionOrNone(list(12)),
      SomeString(list(13)), DoubleOption.toDoubleOptionOrNone(list(14)), DoubleOption.toDoubleOptionOrNone(list(15)), IntegerOption.toIntegerOptionOrNone(list(16))),
    new TargetData(DoubleOption.toDoubleOptionOrNone(list(17)), DoubleOption.toDoubleOptionOrNone(list(18)), DoubleOption.toDoubleOptionOrNone(list(19)),
      DoubleOption.toDoubleOptionOrNone(list(20)), DoubleOption.toDoubleOptionOrNone(list(21))),
    new IOData(DoubleOption.toDoubleOptionOrNone(list(22)), DoubleOption.toDoubleOptionOrNone(list(23)), DoubleOption.toDoubleOptionOrNone(list(24)),
      DoubleOption.toDoubleOptionOrNone(list(25)), DoubleOption.toDoubleOptionOrNone(list(26)), DoubleOption.toDoubleOptionOrNone(list(27))),
    new QualityData(DoubleOption.toDoubleOptionOrNone(list(28)), DoubleOption.toDoubleOptionOrNone(list(29)), DoubleOption.toDoubleOptionOrNone(list(30)),
      DoubleOption.toDoubleOptionOrNone(list(31)), DoubleOption.toDoubleOptionOrNone(list(32)), DoubleOption.toDoubleOptionOrNone(list(33))),
    new AbsenteeismData(DoubleOption.toDoubleOptionOrNone(list(34)), DoubleOption.toDoubleOptionOrNone(list(35)), DoubleOption.toDoubleOptionOrNone(list(36)),
      DoubleOption.toDoubleOptionOrNone(list(37)), DoubleOption.toDoubleOptionOrNone(list(38)), DoubleOption.toDoubleOptionOrNone(list(39)),
      DoubleOption.toDoubleOptionOrNone(list(40)), DoubleOption.toDoubleOptionOrNone(list(41)), DoubleOption.toDoubleOptionOrNone(list(42)),
      DoubleOption.toDoubleOptionOrNone(list(43))),
    IntegerOption(1), IntegerOption.toIntegerOptionOrNone(list(1))
  )

  private def getDate(date: String) = {
    val split = date.split("-")
    Try(new FactoryDate(IntegerOption.toIntegerOptionOrNone(split(2)),
      IntegerOption.toIntegerOptionOrNone(split(1)),
      IntegerOption.toIntegerOptionOrNone(split(0)))).getOrElse(
        new FactoryDate(NoInteger, NoInteger, NoInteger)
      )
  }

  def dataAsList: List[LineListObject] = loadData.drop(1).map(rowToObject)
}
