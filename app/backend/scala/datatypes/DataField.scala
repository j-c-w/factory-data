package backend.scala.datatypes

import java.util.{InputMismatchException, Date}

import backend.scala.datatypes.options._

import scala.util.{Failure, Try}

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

trait DataField[T <: MathComparable[T]] {
  def toString: String
  def get(data: LineListObject): T
}

/*
 * These sub-traits are here to enable pattern matching
 * in the FormToQuery object.
 *
 * It is basically so that the form can convert between strings
 * and the required types.
 */
trait NothingDataField extends DataField[Nothing]
trait IntegerOptionDataField extends DataField[IntegerOption]
trait DateOptionDataField extends DataField[DateOption]
trait DoubleOptionDataField extends DataField[DoubleOption]

case object NoField extends NothingDataField {
  override val toString = "Invalid Field"
  def get(data: LineListObject) = throw new NoSuchElementException("NoField.get")
}

case object FactoryCode extends IntegerOptionDataField {
  override val toString = "Factory Code"
  def get(data: LineListObject) = data.factoryCode
}

case object LineCode extends IntegerOptionDataField {
  override val toString = "Line Code"
  def get(data: LineListObject) = data.lineCode
}

case object DateObject extends  DateOptionDataField {
  override val toString = "Date"
  def get(data: LineListObject) = data.date
}

case object HelpersPresent extends DoubleOptionDataField {
  override val toString = "Helpers Present"
  def get(data: LineListObject) = data.getTotalHelpersPresent
}

case object HelpersAbsent extends DoubleOptionDataField {
  override val toString = "Helpers Absent"
  def get(data: LineListObject) = data.getTotalHelpersAbsent
}

case object HelpersLeave extends DoubleOptionDataField {
  override val toString = "Helpers Leave"
  def get(data: LineListObject) = data.getHelpersLeave
}

case object PercentHelpersAbsent extends DoubleOptionDataField {
  override val toString = "Percent Helpers Absent"
  def get(data: LineListObject) = data.getPercentHelpersAbsent
}

case object TotalOperators extends DoubleOptionDataField {
  override val toString = "Total Operators"
  def get(data: LineListObject) = data.getTotalOperators
}

case object OperatorsAbsent extends DoubleOptionDataField {
  override val toString = "Operators Absent"
  def get(data: LineListObject) = data.getTotalOperatorsAbsent
}

case object OperatorsLeave extends DoubleOptionDataField {
  override val toString = "Operators Leave"
  def get(data: LineListObject) = data.getOperatorLeave
}

case object PercentOperatorsAbsent extends DoubleOptionDataField {
  override val toString = "Percent Operators Absent"
  def get(data: LineListObject) = data.getPercentOperatorsAbsent
}



object DataField {
  def fromString(s: String) = s match {
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
    case _ => NoField
  }

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