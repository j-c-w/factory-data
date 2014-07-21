package main.scala.datatypes

import java.lang.Exception
import java.math.{MathContext, BigDecimal}
import java.text.DecimalFormat

import scala.util.control.Exception

/*
 * Created by Jackson Woodruff on 21/07/2014 
 * 
 */

abstract class DoubleOption {
  def * (other: DoubleOption) = (this, other) match {
    case (_, NoDouble) => this
    case (NoDouble, _) => other
    case (SomeDouble(thisDouble), SomeDouble(thatDouble)) => SomeDouble(thisDouble * thatDouble)
  }

  def / (other: DoubleOption) = (this, other) match {
    case (_, NoDouble) => this
    case (NoDouble, _) => other
    case (SomeDouble(thisDouble), SomeDouble(otherDouble)) => SomeDouble(thisDouble/otherDouble)
  }

  def / (other: IntegerOption): DoubleOption =
    this / other.toDoubleOption

  //pushes the rounding on to the rnd(dp: Int)
  //method w/ two dp
  def rnd: DoubleOption = rnd(2)

  def rnd (dp: Int): DoubleOption = this match {
    case NoDouble => NoDouble
    case SomeDouble(x) => SomeDouble(x.toInt.toDouble)
  }

  def or(other: => DoubleOption): DoubleOption
  def get: Double
  def isEmpty: Boolean
}

case class SomeDouble(x: Double) extends DoubleOption {
  def or(other: => DoubleOption) = SomeDouble.this
  def get = x
  def isEmpty = false
  override def toString = x.toString
}

case object NoDouble extends DoubleOption {
  def or(other: => DoubleOption) = other
  def get = throw new UnsupportedOperationException("NoDouble.get")
  def isEmpty = true
  override def toString = "No Data"
}
