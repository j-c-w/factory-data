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

abstract class StringOptionDataField extends DataField[StringOption] {
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

case object LineCode extends IntegerOptionDataField {
  override def get(data: LineListObject): IntegerOption =
    data.lineInfo.lineCode
  override val toString = "Line Code"
}

case object SLine extends IntegerOptionDataField {
  override def get(data: LineListObject): IntegerOption =
    data.lineInfo.sLine
  override val toString = "sLine"
}

case object LineStatus extends IntegerOptionDataField {
  override def get(data: LineListObject): IntegerOption =
    data.lineInfo.lineStatus
  override val toString = "Line Status"
}

case object LineMerged extends IntegerOptionDataField {
  override def get(data: LineListObject): IntegerOption =
    data.lineInfo.merged
  override val toString = "Line Merged"
}

case object LineMergedWith extends IntegerOptionDataField {
  override def get(data: LineListObject): IntegerOption =
    data.lineInfo.mergedWith
  override val toString = "Line Merged With"
}

case object SplitLine extends IntegerOptionDataField {
  override def get(data: LineListObject): IntegerOption =
    data.lineInfo.splitLine
  override val toString = "Split Line"
}

///////////////////////////////////////////////
//Order Information ///////////////////
case object Item extends StringOptionDataField {
  override def get(data: LineListObject): StringOption =
    data.orderInfo.item
  override val toString = "Item"
}

case object Style extends StringOptionDataField {
  override def get(data: LineListObject): StringOption =
    data.orderInfo.style
  override val toString = "Style"
}

case object OrderNo extends IntegerOptionDataField {
  override def get(data: LineListObject): IntegerOption =
    data.orderInfo.orderNo
  override val toString = "Order Number"
}

case object OrderQuantity extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.orderInfo.orderQuantity
  override val toString = "Order Quantity"
}

case object Buyer extends StringOptionDataField {
  override def get(data: LineListObject): StringOption =
    data.orderInfo.buyer
  override val toString = "Buyer"
}

case object SMV extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.orderInfo.smv
  override val toString = "SMV"
}

case object RunningDays extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.orderInfo.runningDays
  override val toString = "Running Days"
}

case object RunningDaysNA extends IntegerOptionDataField {
  override def get(data: LineListObject): IntegerOption =
    data.orderInfo.runningDaysNA
  override val toString = "Running Days NA"
}

///////////////////////////////////////////////////////////
///////////Target Data////////////////////////////////////

case object Input extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.inputOutput.input
  override val toString = "Input"
}

case object InputCount extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.inputOutput.inputCount
  override val toString = "Input Count"
}

case object OutputCount extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.inputOutput.outputCount
  override val toString = "Output Count"
}

case object DayOutput extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.inputOutput.dayOutput
  override val toString = "Day Output"
}

case object OutputMinutes extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.inputOutput.outputMinutes
  override val toString = "Output Minutes"
}

case object LostMinutes extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.inputOutput.lostMinutes
  override val toString = "Lost Minutes"
}

//////////////////////////////////////////////////////
//////////Quality section////////////////////////////
case object TotalChecked extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.qualityInformation.totalChecked
  override val toString = "QC Total Checked"
}

case object TotalQCPass extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.qualityInformation.totalQCPass
  override val toString = "QC Total Passed"
}

case object QCAlter extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.qualityInformation.altered
  override val toString = "QC Total Altered"
}

case object QCSpot extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.qualityInformation.spot
  override val toString = "QC Spot"
}

case object QCRejected extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.qualityInformation.reject
  override val toString = "QC Rejected"
}

case object QCFabricError extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.qualityInformation.fabricError
  override val toString = "QC Fabric Error"
}

///////////////////////////////////////////////////////
//////////Attendance Data/////////////////////////////
case object OperatorsRegistered extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.absenteeism.loRegistered
  override val toString = "Line Operators Registered"
}

case object HelpersRegistered extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.absenteeism.hlRegistered
  override val toString = "Helpers Registered"
}

case object OperatorsActual extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.absenteeism.loActual
  override val toString = "Line Operators Actual"
}

case object HelpersActual extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.absenteeism.hlActual
  override val toString = "Helpers Actual"
}

case object ActualManpowerTotal extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.absenteeism.actualManpowerTotal
  override val toString = "Actual Manpower Total"
}

case object Machines extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.absenteeism.machines
  override val toString = "Machines"
}

case object OperatorsPresent extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.absenteeism.loPresent
  override val toString = "Line Operators Present"
}

case object OperatorsAbsent extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.absenteeism.loAbsent
  override val toString = "Line Operators Absent"
}

case object HelpersPresent extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.absenteeism.hlPresent
  override val toString = "Helpers Present"
}

case object HelpersAbsent extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
    data.absenteeism.hlAbsent
  override val toString = "Helpers Absent"
}

/////////////////////////////////////////////////////
/////////efficiency//////////////////////////////////
case object Efficiency extends DoubleOptionDataField {
  override def get(data: LineListObject): DoubleOption =
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
    case SLine.toString => SLine
    case LineStatus.toString => LineStatus
    case LineMerged.toString => LineMerged
    case LineMergedWith.toString => LineMergedWith
    case SplitLine.toString => SplitLine
    case Item.toString => Item
    case Style.toString => Style
    case OrderNo.toString => OrderNo
    case OrderQuantity.toString => OrderQuantity
    case Buyer.toString => Buyer
    case SMV.toString => SMV
    case RunningDays.toString => RunningDays
    case RunningDaysNA.toString => RunningDaysNA
    case Input.toString => Input
    case InputCount.toString => InputCount
    case OutputCount.toString => OutputCount
    case DayOutput.toString => DayOutput
    case OutputMinutes.toString => OutputMinutes
    case LostMinutes.toString => LostMinutes
    case TotalChecked.toString => TotalChecked
    case TotalQCPass.toString => TotalQCPass
    case QCAlter.toString => QCAlter
    case QCSpot.toString => QCSpot
    case QCRejected.toString => QCRejected
    case QCFabricError.toString => QCFabricError
    case OperatorsRegistered.toString => OperatorsRegistered
    case HelpersRegistered.toString => HelpersRegistered
    case OperatorsActual.toString => OperatorsActual
    case HelpersActual.toString => HelpersActual
    case ActualManpowerTotal.toString => ActualManpowerTotal
    case Machines.toString => Machines
    case OperatorsPresent.toString => OperatorsPresent
    case OperatorsAbsent.toString => OperatorsAbsent
    case HelpersPresent.toString => HelpersPresent
    case HelpersAbsent.toString => HelpersAbsent
    case Efficiency.toString => Efficiency
    case NumberOfObservations.toString => NumberOfObservations
    case _ => NoField
  }

  val asList = List(
    FactoryCode.toString,
    Day.toString,
    Month.toString,
    Year.toString,
    LineCode.toString,
    SLine.toString,
    LineStatus.toString,
    LineMerged.toString,
    LineMergedWith.toString,
    SplitLine.toString,
    Item.toString,
    Style.toString,
    OrderNo.toString,
    OrderQuantity.toString,
    Buyer.toString,
    SMV.toString,
    RunningDays.toString,
    RunningDaysNA.toString,
    Input.toString,
    InputCount.toString,
    OutputCount.toString,
    DayOutput.toString,
    OutputMinutes.toString,
    LostMinutes.toString,
    TotalChecked.toString,
    TotalQCPass.toString,
    QCAlter.toString,
    QCSpot.toString,
    QCRejected.toString,
    QCFabricError.toString,
    OperatorsRegistered.toString,
    HelpersRegistered.toString,
    OperatorsActual.toString,
    HelpersActual.toString,
    ActualManpowerTotal.toString,
    Machines.toString,
    OperatorsPresent.toString,
    OperatorsAbsent.toString,
    HelpersPresent.toString,
    HelpersAbsent.toString,
    Efficiency.toString,
    NumberOfObservations.toString
  )

  //this is a list of only the
  //double fields, for graphing etc.
  val asDoublesList = List(
    OrderQuantity.toString,
    SMV.toString,
    RunningDays.toString,
    Input.toString,
    InputCount.toString,
    OutputCount.toString,
    DayOutput.toString,
    OutputMinutes.toString,
    LostMinutes.toString,
    TotalChecked.toString,
    TotalQCPass.toString,
    QCAlter.toString,
    QCSpot.toString,
    QCRejected.toString,
    QCFabricError.toString,
    OperatorsRegistered.toString,
    HelpersRegistered.toString,
    OperatorsActual.toString,
    HelpersActual.toString,
    ActualManpowerTotal.toString,
    Machines.toString,
    OperatorsPresent.toString,
    OperatorsAbsent.toString,
    HelpersPresent.toString,
    HelpersAbsent.toString,
    Efficiency.toString
  )
}