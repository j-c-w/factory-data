package backend.scala.datatypes.options

import backend.scala.datatypes.options.wrappers.DoubleOptionWrapper

import scala.util.Try

/*
 * Created by Jackson Woodruff on 21/07/2014 
 * 
 */

abstract class DoubleOption extends MathComparable[DoubleOption] {
  def * (other: DoubleOption): DoubleOption = (this, other) match {
    case (SomeDouble(thisDouble), SomeDouble(thatDouble)) => SomeDouble(thisDouble * thatDouble)
    case (_, _) => NoDouble
  }

  def * (other: Double): DoubleOption =
    this * SomeDouble(other)

  def + (other: DoubleOption): DoubleOption = (this, other) match {
    case (SomeDouble(x), SomeDouble(y)) => SomeDouble(x + y)
    case (_, _) => NoDouble
  }

  def - (other: DoubleOption) = (this, other) match {
    case (SomeDouble(x), SomeDouble(y)) => SomeDouble(x - y)
    case (_, _) => NoDouble
  }
  def / (other: DoubleOption) = (this, other) match {
    case (SomeDouble(thisDouble), SomeDouble(otherDouble)) => {
      if (otherDouble == 0) NoDouble//this is a divide by zero, so there is no result
      else SomeDouble(thisDouble / otherDouble)
    }
    case (_, _) => NoDouble
  }

  def compareTo(other: DoubleOption) = (this, other) match {
    case (SomeDouble(x), SomeDouble(y)) => x compareTo y
    case (NoDouble, NoDouble) => 0
    case (SomeDouble(x), NoDouble) => 1
    case (NoDouble, SomeDouble(x)) => -1
  }

  def mergeEqual(other: DoubleOption) =
    if ((this compareTo other) == 0) this
    else NoDouble

  def isNone = isEmpty

  def or(other: => DoubleOption): DoubleOption
  def get: Double
  def isEmpty: Boolean
  def getOrElse(otherwise: Double): Double
}

case class SomeDouble(x: Double) extends DoubleOption {
  def or(other: => DoubleOption) = SomeDouble.this
  def get = x
  def isEmpty = false
  //rounds the output to 2dp before displaying
  override def toString = if (x < 0) x.toString //there has been a serious problem -- leave as is
  else {
    //do the rounding -- this should not be done anywhere else
    //for the sake of accuracy
    (Math.floor(100 * x + 0.5) / 100).toString;
  }

  def getOrElse(double: Double) = x
}

case object NoDouble extends DoubleOption {
  def or(other: => DoubleOption) = other
  def get = throw new UnsupportedOperationException("NoDouble.get")
  def isEmpty = true
  override def toString = "No Data"
  def getOrElse(other: Double) = other
}

/*
 * This is a helper object to make the creation
 * of DoubleOptions easier
 */
object DoubleOption {
  def apply(input: Double) = SomeDouble(input)

  /*
   * This converts a string to an IntegerOption
   *
   * However, the reader should note that this
   * will throw if it is passed a badly formatted
   * number.
   */
  implicit def toDoubleOption(input: String) =
    SomeDouble(input.toDouble)

  /*
   * This is like the above, but it returns a NoInteger
   * if the conversion fails
   */
  implicit def toDoubleOptionOrNone(x: String) =
    Try(SomeDouble(x.toDouble)).getOrElse(NoDouble)

  implicit def toWrappedDoubleOrNone(x: String) =
    new DoubleOptionWrapper(toDoubleOptionOrNone(x))
}
