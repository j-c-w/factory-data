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

trait DataField[T <: MathComparable[T]] extends SuperDataField {
  def toString: String
  def get(data: LineListObject): T

  def compare(dataOne: LineListObject, dataTwo: LineListObject, comparisonMethod: ComparisonMethod): Boolean = {
    comparisonMethod.compare(get(dataOne), get(dataTwo))
  }
}

/*
 * These sub traits implement the comparison method,
 * which is very very helpful because it enables easy
 * type casting (which is somethat that I had serious
 * problems with before
 */
trait NothingDataField extends DataField[Nothing] {
  def compare(data: LineListObject, comparisonMethod: ComparisonMethod, stringComparison: String): Try[Boolean] =
    Try(throw new Exception("NothingDataField.compare"))
}

trait IntegerOptionDataField extends DataField[IntegerOption] {
  def compare(data: LineListObject, comparisonMethod: ComparisonMethod, stringComparison: String): Try[Boolean] = {
    Try(comparisonMethod.compare(get(data), IntegerOption.toIntegerOption(stringComparison)))
  }
}

trait DoubleOptionDataField extends DataField[DoubleOption] {
  def compare(data: LineListObject, comparisonMethod: ComparisonMethod, stringComparison: String): Try[Boolean] = {
    Try(comparisonMethod.compare(fieldGet(data), DoubleOption.toDoubleOption(stringComparison)))
  }
  
  final def get(data: LineListObject) =
    fieldGet(data).round
  
  def fieldGet(data: LineListObject): DoubleOption
}

trait StringOptionDataField extends DataField[StringOption] {
  def compare(data: LineListObject, comparisonMethod: ComparisonMethod, stringComparison: String): Try[Boolean] = {
    Try(comparisonMethod.compare(get(data), SomeString(stringComparison)))
  }
}

case object NoField extends NothingDataField {
  override val toString = "Invalid Field"
  def get(data: LineListObject) = throw new NoSuchElementException("NoField.get")
}

case object FactoryCode extends IntegerOptionDataField {
  override def get(data: LineListObject): IntegerOption =
    data.factoryCode
  override val toString = "Factory Code"
}

case object Day extends IntegerOptionDataField {
  override def get(data: LineListObject): IntegerOption =
    data.factoryDate.day
  override val toString = "Day"
}

case object Month extends IntegerOptionDataField {
  override def get(data: LineListObject): IntegerOption =
    data.factoryDate.month
  override val toString = "Month"
}

case object Year extends IntegerOptionDataField {
  override def get(data: LineListObject): IntegerOption =
    data.factoryDate.year
  override val toString = "Year"
}

case object DayOfWeek extends StringOptionDataField {
  override def get(data: LineListObject): StringOption = 
    data.factoryDate.dayOfWeek
  override val toString = "Day of Week"
}

case object LineCode extends IntegerOptionDataField {
  override def get(data: LineListObject): IntegerOption =
    data.lineInfo.lineCode
  override val toString = "Line Code"
}

///////////////////////////////////////////////
//Order Information ///////////////////

case object SMV extends DoubleOptionDataField {
  override def fieldGet(data: LineListObject): DoubleOption =
    data.orderInfo.smv.get
  override val toString = "SMV"
}

case object RunningDays extends DoubleOptionDataField {
  override def fieldGet(data: LineListObject): DoubleOption =
    data.orderInfo.runningDays.get
  override val toString = "Running Days"
}

///////////////////////////////////////////////////////////
///////////Target Data////////////////////////////////////

case object TotalTarget extends DoubleOptionDataField {
  override def fieldGet(data: LineListObject): DoubleOption =
    data.targetInformation.totalTarget.get
  override val toString = "Total Target"
}

case object AvailableMinutes extends DoubleOptionDataField {
  override def fieldGet(data: LineListObject): DoubleOption =
    data.targetInformation.availableMinutes.get
  override val toString = "Available Minutes"
}

case object OutputTarget extends DoubleOptionDataField {
  override def fieldGet(data: LineListObject): DoubleOption =
    data.targetInformation.outputTarget.get
  override val toString = "Output Target"
}

case object OutputOverTarget extends DoubleOptionDataField {
  override def fieldGet(data: LineListObject): DoubleOption =
    data.outputOverTarget
  override val toString = "Output/Target"
}

/////////////////////////////////////////////////////////
//////////////IO Data///////////////////////////////////////
case object DayOutput extends DoubleOptionDataField {
  override def fieldGet(data: LineListObject): DoubleOption =
    data.inputOutput.dayOutput.get
  override val toString = "Day Output"
}

case object OutputMinutes extends DoubleOptionDataField {
  override def fieldGet(data: LineListObject): DoubleOption =
    data.inputOutput.outputMinutes.get
  override val toString = "Output Minutes"
}

//////////////////////////////////////////////////////
//////////Quality section////////////////////////////
case object AlterRate extends DoubleOptionDataField {
  override def fieldGet(data: LineListObject): DoubleOption =
    data.qualityInformation.alterRate.get
  override val toString = "Alter Rate"
}

case object SpotRate extends DoubleOptionDataField {
  override def fieldGet(data: LineListObject): DoubleOption =
    data.qualityInformation.spotRate.get
  override val toString = "Spot Rate"
}

case object RejectRate extends DoubleOptionDataField {
  override def fieldGet(data: LineListObject): DoubleOption =
    data.qualityInformation.rejectRate.get
  override val toString = "Reject Rate"
}

///////////////////////////////////////////////////////
//////////Attendance Data/////////////////////////////
case object TotalPresent extends DoubleOptionDataField {
  override def fieldGet(data: LineListObject): DoubleOption =
    data.absenteeism.totalPresent.get
  override val toString = "Total Present"
}

case object AbsentRate extends DoubleOptionDataField {
  override def fieldGet(data: LineListObject): DoubleOption =
    data.absenteeism.absentRate.get
  override val toString = "Absent Rate"
}

/////////////////////////////////////////////////////
/////////efficiency//////////////////////////////////
case object Efficiency extends DoubleOptionDataField {
  override def fieldGet(data: LineListObject): DoubleOption =
    data.efficiency
  override val toString = "Efficiency"
}

/////////////////////////////////////////////////
////////////miscelanious//////////////////////
case object NumberOfObservations extends IntegerOptionDataField {
  override def get(data: LineListObject): IntegerOption =
    data.numberOfObservations
  override val toString = "Number Of Observations"
}


object DataField {
  def fromString(s: String): SuperDataField = s match {
    case FactoryCode.toString => FactoryCode
    case Day.toString => Day
    case Month.toString => Month
    case Year.toString => Year
    case LineCode.toString => LineCode
    case SMV.toString => SMV
    case RunningDays.toString => RunningDays
    case OutputMinutes.toString => OutputMinutes
    case AlterRate.toString => AlterRate
    case SpotRate.toString => SpotRate
    case RejectRate.toString => RejectRate
    case TotalPresent.toString => TotalPresent
    case AbsentRate.toString => AbsentRate
    case Efficiency.toString => Efficiency
    case OutputTarget.toString => OutputTarget
    case AvailableMinutes.toString => AvailableMinutes
    case TotalTarget.toString => TotalTarget
    case DayOutput.toString => DayOutput
    case DayOfWeek.toString => DayOfWeek
    case NumberOfObservations.toString => NumberOfObservations
    case _ => NoField
  }

  val asList = List(
    FactoryCode.toString,
    Day.toString,
    Month.toString,
    Year.toString,
    LineCode.toString,
    SMV.toString,
    RunningDays.toString,
    OutputMinutes.toString,
    AlterRate.toString,
    RejectRate.toString,
    SpotRate.toString,
    TotalPresent.toString,
    AbsentRate.toString,
    Efficiency.toString,
    OutputTarget.toString,
    AvailableMinutes.toString,
    TotalTarget.toString,
    DayOutput.toString,
    DayOfWeek.toString,
    NumberOfObservations.toString
  )

  //this is a list of only the
  //double fields, for graphing etc.
  val asDoublesList = List(
    SMV.toString,
    RunningDays.toString,
    DayOutput.toString,
    OutputMinutes.toString,
    AlterRate.toString,
    RejectRate.toString,
    SpotRate.toString,
    TotalPresent.toString,
    AbsentRate.toString,
    Efficiency.toString,
    OutputTarget.toString,
    AvailableMinutes.toString,
    TotalTarget.toString
  )
}