package backend.scala.datatypes.options

import backend.scala.datatypes.options.MathComparable

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

abstract class StringOption extends MathComparable[StringOption] {
  override def compareTo(other: StringOption): Int = (this, other) match {
    case (SomeString(x), SomeString(y)) => x compareTo y
    case (NoString, NoString) => 0
    case (SomeString(x), NoString) => 1
    case (NoString, SomeString(x)) => -1
  }

  def mergeEqual(other: StringOption) =
    if ((this compareTo other) == 0) this
    else NoString

  def isEmpty: Boolean
  def get: String
  def getOrElse(x: => String): String
}

case class SomeString(s: String) extends StringOption {
  override def isEmpty: Boolean = false
  override def get: String = s
  override def getOrElse(x: => String): String = s
  override def toString = s
}

object NoString extends StringOption {
  override def isEmpty: Boolean = true
  override def get: String = throw new NoSuchElementException("NoString.get")
  override def getOrElse(x: => String): String = x
  override def toString = "No Data"
}

object StringOption {
  def apply(string: String): StringOption =
    SomeString(string)
}


