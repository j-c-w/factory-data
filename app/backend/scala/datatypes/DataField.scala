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
 * In fact, this doesn't really solve the problem.
 * however, it is important to remember that
 * SuperDataField can't take type parameters (really)
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
  def get(data: LineListObject): Comparable[_]
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

abstract class DoubleOptionDataField extends DataField[DoubleOption] {
  def compare(data: LineListObject, comparisonMethod: ComparisonMethod, stringComparison: String): Try[Boolean] = {
    Try(comparisonMethod.compare(get(data), DoubleOption.toDoubleOption(stringComparison)))
  }
}

case object NoField extends NothingDataField {
  override val toString = "Invalid Field"
  def get(data: LineListObject) = throw new NoSuchElementException("NoField.get")
}

object DataField {
  def fromString(s: String): SuperDataField = s match {
    /*case FactoryCode.toString => FactoryCode
    case LineCode.toString => LineCode
    case DateObject.toString => DateObject
    case TotalHelpers.toString => TotalHelpers
    case HelpersPresent.toString => HelpersPresent
    case HelpersAbsent.toString => HelpersAbsent
    case HelpersLeave.toString => HelpersLeave
    case PercentHelpersAbsent.toString => PercentHelpersAbsent
    case TotalOperators.toString => TotalOperators
    case OperatorsAbsent.toString => OperatorsAbsent
    case OperatorsLeave.toString => OperatorsLeave
    case PercentOperatorsAbsent.toString => PercentOperatorsAbsent
    case NumberOfObservations.toString => NumberOfObservations
    case OperatorsPresent.toString => OperatorsPresent */
    case _ => NoField
  }
}