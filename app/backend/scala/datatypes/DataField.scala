package backend.scala.datatypes

import java.util.{InputMismatchException, Date}

import backend.scala.datatypes.options._
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Comparison
import frontend.ComparisonMethod

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

/*
 * Never directly override this trait.
 *
 * Always use the DataField[T] abstract class
 * instead - this trait has some risky typeless
 * functions that could cause problems were
 * they not specified better further down the
 * chain.
 *
 * In fact, this doesn't really sove the problem.
 * however, it is important to remember that
 * SuperDataDield can't take type parameters (really)
 * so there is really on one path to take.
 */
trait SuperDataField {
  /*
 * This tries to convert the string passed to a type of T
 * then compare it to the data passed. If this conversion & comparison
 * is successful, then it returns a Success(Boolean),
 */
  def compare(data: LineListObject, comparisonMethod: ComparisonMethod, stringComparison: String): Try[Boolean]

  /*
   * This is a compare method that compares two different objects using a
   * comparison method
   */
  def compare(data: LineListObject, dataTwo: LineListObject, comparisonMethod: ComparisonMethod): Boolean

  /*
   * Because I need to have access to the field without knowing the type
   * when converting from strings to DataFields, this method has to be type
   * unsafe.
   *
   * The types are overriden ASAP in the next level down in the DataField class.
   *
   * Changing the type of the parameter creates some serious problems
   * so is not worth doing. This method will have to remain concrete for the
   * time being.
   */
  def get(data: LineListObject): Any
}

abstract class DataField[T <: MathComparable[T]] extends SuperDataField {
  def toString: String
  def get(data: LineListObject): T

  def compare(dataOne: LineListObject, dataTwo: LineListObject, comparisonMethod: ComparisonMethod): Boolean = {
    comparisonMethod.compare(get(dataOne), get(dataTwo))
  }
}

/*
 * These sub classes implement the comparison method,
 * which is very very helpful because it enables easy
 * type casting (which is somethat that I had serious
 * problems with before
 */
abstract class NothingDataField extends DataField[Nothing] {
  def compare(data: LineListObject, comparisonMethod: ComparisonMethod, stringComparison: String): Try[Boolean] =
    Try(throw new Exception("NothingDataField.compare"))
}

abstract class IntegerOptionDataField extends DataField[IntegerOption] {
  def compare(data: LineListObject, comparisonMethod: ComparisonMethod, stringComparison: String): Try[Boolean] = {
    Try(comparisonMethod.compare(get(data), IntegerOption.toIntegerOption(stringComparison)))
  }
}

abstract class DateOptionDataField extends DataField[DateOption] {
  /*
   * Todo enable easy comparison of dates by completely re-thinking this
   */
  def compare(data: LineListObject, comparisonMethod: ComparisonMethod, stringComparison: String): Try[Boolean] = {
    ??? //Try(comparisonMethod.compare(get(data), DateOption.toDateOption(FactoryDate.toFactoryDate(stringComparison)))
  }
}

abstract class DoubleOptionDataField extends DataField[DoubleOption] {
  def compare(data: LineListObject, comparisonMethod: ComparisonMethod, stringComparison: String): Try[Boolean] = {
    Try(comparisonMethod.compare(get(data), DoubleOption.toDoubleOption(stringComparison)))
  }
}

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
  def fromString(s: String): SuperDataField = s match {
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