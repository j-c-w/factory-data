package main.scala.datatypes.options

import main.scala.datatypes.LineListObject


/*
 * Created by Jackson Woodruff on 20/07/2014 
 *
 *
 * This is a set of classes that let me do standard operations
 * with Options without taking the value out of the monad.
 * i.e:
 *    SomeInteger(3) + SomeInteger(4) = SomeInteger(7)
 *    SomeInteger(4) + NoInteger = SomeInteger(4)
 *    etc..
 *
 * The NoInteger value is treated as the unit value for
 * the operation (1 for * / /, 0 for +/- etc..)
 */


case class SomeInteger(x: Int) extends IntegerOption {
  def or(other: => IntegerOption): IntegerOption = SomeInteger.this
  def get: Int = x
  def isEmpty = false
  override def toString = x.toString
}

case object NoInteger extends IntegerOption {
  //this is a dangerous method....
  //has to cast the other passed data type
  //...
  def or(other: => IntegerOption) = other
  def get = throw new NoSuchElementException("NoInteger.get")
  def isEmpty = true
}

//I have decided that for this class it is more efficient
//to declare all additional methods in the IntegerOption class
//rather than in its sub classes, please stick to that
abstract class IntegerOption {
  def + (other: IntegerOption): IntegerOption = (this, other) match {
    case (_, NoInteger) => this
    case (NoInteger, _) => other
    case (SomeInteger(thisInteger), SomeInteger(thatInteger)) => SomeInteger(thisInteger + thatInteger)
  }

  def - (other: IntegerOption): IntegerOption = (this, other) match {
    case (_, NoInteger) => this
    case (NoInteger, _) => other
    case (SomeInteger(thisInt), SomeInteger(thatInt)) => SomeInteger(thisInt - thatInt)
  }

  def * (other: IntegerOption): IntegerOption = (this, other) match {
    case (_, NoInteger) => this
    case (NoInteger, _) => other
    case (SomeInteger(thisInt), SomeInteger(thatInt)) => SomeInteger(thisInt * thatInt)
  }

  //also define a helper * method that takes an int
  def * (other: Int): IntegerOption = this match {
    case NoInteger => NoInteger
    case _ => this * SomeInteger(other)
  }

  def / (other: IntegerOption): IntegerOption = (this, other) match {
    case (_, NoInteger) => this
    case (NoInteger, _) => other
    case (SomeInteger(thisInt), SomeInteger(otherInt)) => SomeInteger(thisInt/otherInt)
  }

  def toDoubleOption: DoubleOption = this match {
    case NoInteger => NoDouble
    case SomeInteger(x) => SomeDouble(x.toDouble)
  }

  def or(other: => IntegerOption): IntegerOption
  def isEmpty: Boolean
  def get: Int
}

object IntegerOption {
  //returns first the sum of all the options
  // with 0 as the default for a NoInteger
  // and then the number of SomeIntegers in
  // the list (for use when calculating most
  //things
  def sum(list: List[IntegerOption]): (Int, Int) = {
    val notNones = list filter (x => !x.isEmpty)
    val numUsed = notNones.length
    ((notNones map (x => x.get)).sum, numUsed)
  }
}