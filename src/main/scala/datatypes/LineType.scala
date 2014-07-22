package main.scala.datatypes

import java.util.Date

/*
 * Created by Jackson Woodruff on 22/07/2014 
 *
 * This is a file with a case object for each
 * parameter in the LineListObject file --
 * this enables searching, sorting, etc on particular
 * parameters in such a way that is easy to extend to
 * production
 */

trait LineType[T] {
  def get(line: LineListObject): T
}

case class LineTypeList(line: List[LineType[Any]])

case object FactoryCode extends LineType[IntegerOption] {
  def get(line: LineListObject): IntegerOption = line.getFactoryCode
}

case object LineCode extends LineType[IntegerOption] {
  def get(line: LineListObject) = line.getLineCode
}

case object Date extends LineType[DataOption[Date]] {
  def get(line: LineListObject) = line.getDate
}

case object TotalProductionWorkers extends LineType[IntegerOption] {
  def get(line: LineListObject) = line.getTotalProductionWorkers
}

case object ProductionWorkersPresent extends LineType[IntegerOption] {
  def get(line: LineListObject) = line.getTotalProductionWorkersPresent
}

case object ProductionWorkersAbsent extends LineType[IntegerOption] {
  def get(line: LineListObject) = line.getTotalProductionWorkersAbsent
}

//etc..
