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
    case (SomeDouble(thisDouble), SomeDouble(otherDouble)) => SomeDouble(thisDouble / otherDouble)
    case (_, _) => NoDouble
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
  override def toString = if (x < 0) x.toString //there has been a serious problem -- leave as is
  else {
    //do the rounding -- this should not be done anywhere else
    //for the sake of accuracy
    (Math.floor(100 * x + 0.5) / 100).toString;
  }
}

case object NoDouble extends DoubleOption {
  def or(other: => DoubleOption) = other
  def get = throw new UnsupportedOperationException("NoDouble.get")
  def isEmpty = true
  override def toString = "No Data"
}
