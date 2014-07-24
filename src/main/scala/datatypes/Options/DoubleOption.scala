package main.scala.datatypes.options

/*
 * Created by Jackson Woodruff on 21/07/2014 
 * 
 */

abstract class DoubleOption {
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
    case (SomeDouble(thisDouble), SomeDouble(otherDouble)) => SomeDouble(thisDouble/otherDouble)
    case (_, _) => NoDouble
  }


  //pushes the rounding on to the rnd(dp: Int)
  //method w/ two dp
  def rnd: DoubleOption = rnd(2)

  def rnd (dp: Int): DoubleOption = this match {
    case NoDouble => NoDouble
    case SomeDouble(x) => SomeDouble(x)
  }

  def or(other: => DoubleOption): DoubleOption
  def get: Double
  def isEmpty: Boolean
}

case class SomeDouble(x: Double) extends DoubleOption {
  def or(other: => DoubleOption) = SomeDouble.this
  def get = x
  def isEmpty = false
  //rounds the output to 2dp before displaying
  override def toString = rnd(2).get.toString
}

case object NoDouble extends DoubleOption {
  def or(other: => DoubleOption) = other
  def get = throw new UnsupportedOperationException("NoDouble.get")
  def isEmpty = true
  override def toString = "No Data"
}
