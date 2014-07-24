package main.scala

import java.text.SimpleDateFormat
import java.util.Date
import main.csv.scala.com.github.tototoshi.csv.CSVReader
import main.scala.datatypes._
import main.scala.datatypes.options.NoDouble
import main.scala.datatypes.options.SomeData
import main.scala.datatypes.options.SomeInteger
import main.scala.datatypes.options._

import scala.collection.parallel.immutable.ParSeq
import scala.reflect.ClassTag


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
  val reader = CSVReader.open("C:\\Users\\Jackson\\Projects\\Python\\IPAAttendanceTest\\data\\1004\\Attendance Report\\1004 Attendance 140714 QF.csv")

  def loadData: List[List[String]] = reader.all()

  val dateFormat = new SimpleDateFormat("yyyy-MM-dd")//.format(new Date())

  def rowToObject(list: List[String]): LineListObject = new LineListObject(
    //new EmployeeTypes(SomeDouble(16), SomeDouble(11), SomeDouble(6), NoDouble, SomeDouble(4)),
    new EmployeeTypes(NoDouble, NoDouble, NoDouble, NoDouble, NoDouble),
    new EmployeeTypes(
      NoDouble, SomeDouble(list(2).toInt), SomeDouble(list(3).toInt),
      NoDouble, SomeDouble(list.slice(4, 8).map(_.toInt).sum)),
    new EmployeeTypes(NoDouble, SomeDouble(list(8).toInt), SomeDouble(list(9).toInt), NoDouble, SomeDouble(list.slice(10,14).map(_.toInt).sum)),
    new EmployeeTypes(NoDouble, NoDouble, NoDouble, NoDouble, NoDouble),
    SomeInteger(list(14).trim.toInt),
    SomeInteger(list(16).toInt),
    SomeData(dateFormat.parse(list(1)))
  )

  def dataAsList: List[LineListObject] = loadData.drop(1).map(rowToObject)

}
