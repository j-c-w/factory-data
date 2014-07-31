package backend.scala.datatypes

import java.util.Date

import backend.scala.datatypes.options.{DoubleOption, DataOption, IntegerOption}

/*
 * Created by Jackson Woodruff on 31/07/2014 
 *
 * This is a set of classes for each field in the datatype.
 * 
 * this will need to be modified as more fields are added.
 * 
 * It enables the passing of these things as parameters, etc..
 *
 *
 * Not being used right now for the sake of sanity when there are
 * 100+ column headers, but could be used in the future if the
 * anonfun thing doesnt work out
 */

abstract class DataField[T] {
  def toString: String
  def get(data: LineListObject): T
}

case object FactoryCode extends DataField[IntegerOption] {
  override def toString = "Factory Code"
  def get(data: LineListObject) = data.factoryCode
}

case object LineCode extends DataField[IntegerOption] {
  override def toString = "Line Code"
  def get(data: LineListObject) = data.lineCode
}

case object DateObject extends  DataField[DataOption[Date]] {
  override def toString = "Date"
  def get(data: LineListObject) = data.date
}

case object HelpersPresent extends DataField[DoubleOption] {
  override def toString = "Helpers Present"
  def get(data: LineListObject) = data.getTotalHelpersPresent
}

case object HelpersAbsent extends DataField[DoubleOption] {
  override def toString = "Helpers Absent"
  def get(data: LineListObject) = data.getTotalHelpersAbsent
}

case object HelpersLeave extends DataField[DoubleOption] {
  override def toString = "Helpers Leave"
  def get(data: LineListObject) = data.getHelpersLeave
}

case object PercentHelpersAbsent extends DataField[DoubleOption] {
  override def toString = "Percent Helpers Absent"
  def get(data: LineListObject) = data.getPercentHelpersAbsent
}

case object TotalOperators extends DataField[DoubleOption] {
  override def toString = "Total Operators"
  def get(data: LineListObject) = data.getTotalOperators
}

case object OperatorsAbsent extends DataField[DoubleOption] {
  override def toString = "Operators Absent"
  def get(data: LineListObject) = data.getTotalOperatorsAbsent
}

case object OperatorsLeave extends DataField[DoubleOption] {
  override def toString = "Operators Leave"
  def get(data: LineListObject) = data.getOperatorLeave
}

case object PercentOperatorsAbsent extends DataField[DoubleOption] {
  override def toString = "Percent Operators Absent"
  def get(data: LineListObject) = data.getPercentOperatorsAbsent
}



object DataField {
  //doesnt compile right now -- should be fixed
  //if you decide to take this path
  /*def fromString(s: String) = s match {
    case FactoryCode.toString => FactoryCode
    case LineCode.toString => LineCode
    case DateObject.toString => DateObject
    case HelpersPresent.toString => HelpersPresent
    case HelpersAbsent.toString => HelpersAbsent
    case HelpersLeave.toString => HelpersLeave
    case PercentHelpersAbsent.toString => PercentHelpersAbsent
    case TotalOperators.toString => TotalOperators
    case OperatorsAbsent.toString => OperatorsAbsent
    case OperatorsLeave.toString => OperatorsLeave
    case PercentOperatorsAbsent.toString => PercentOperatorsAbsent
  }*/

  def toHtml(data: LineListObject): String = {
    "<td>" + FactoryCode.toString + "</td>" +
    "<td>" + LineCode.toString + "</td>" +
    "<td>" + DateObject.toString + "</td>" +
    "<td>" + HelpersPresent.toString + "</td>" +
    "<td>" + HelpersAbsent.toString + "</td>" +
    "<td>" + HelpersLeave.toString + "</td>" +
    "<td>" + PercentHelpersAbsent.toString + "</td>" +
    "<td>" + TotalOperators.toString + "</td>" +
    "<td>" + OperatorsAbsent.toString + "</td>" +
    "<td>" + OperatorsLeave.toString + "</td>" +
    "<td>" + PercentOperatorsAbsent.toString
  }

  def toHtml(data: List[LineListObject]): String = {
    "<table>" + htmlHeader + 
      (data map (x => DataField.toHtml(x))).mkString("<tr>", "</tr><tr>", "</tr>").toString +
      "</table>"
  }

  def htmlHeader: String = {
    "<th>" + FactoryCode.toString + "</th>" +
    "<th>" + LineCode.toString + "</th>" +
    "<th>" + DateObject.toString + "</th>" +
    "<th>" + HelpersPresent.toString + "</th>" +
    "<th>" + HelpersAbsent.toString + "</th>" +
    "<th>" + HelpersLeave.toString + "</th>" +
    "<th>" + PercentHelpersAbsent.toString + "</th>" +
    "<th>" + TotalOperators.toString + "</th>" +
    "<th>" + OperatorsAbsent.toString + "</th>" +
    "<th>" + OperatorsLeave.toString + "</th>" +
    "<th>" + PercentOperatorsAbsent.toString
  }
}