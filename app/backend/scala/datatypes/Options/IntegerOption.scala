package backend.scala.datatypes.options



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
  def or(other: => IntegerOption) = other
  def get = throw new NoSuchElementException("NoInteger.get")
  def isEmpty = true
  override def toString = "No Data"
}

//I have decided that for this class it is more efficient
//to declare all additional methods in the IntegerOption class
//rather than in its sub classes, please stick to that
abstract class IntegerOption extends MathComparable[IntegerOption] {
  def compareTo(other: IntegerOption) = (this, other) match {
    case (NoInteger, NoInteger) => 0
    case (SomeInteger(x), SomeInteger(y)) => x compareTo y
    case (_, _) => -2
  }

  def + (other: IntegerOption): IntegerOption = (this, other) match {
    case (SomeInteger(thisInteger), SomeInteger(thatInteger)) => SomeInteger(thisInteger + thatInteger)
    case (_, _) => NoInteger
  }

  def - (other: IntegerOption): IntegerOption = (this, other) match {
    case (SomeInteger(thisInt), SomeInteger(thatInt)) => SomeInteger(thisInt - thatInt)
    case (_, _) => NoInteger
  }

  def * (other: IntegerOption): IntegerOption = (this, other) match {
    case (SomeInteger(thisInt), SomeInteger(thatInt)) => SomeInteger(thisInt * thatInt)
    case (_, _) => NoInteger
  }

  //also define a helper * method that takes an int
  def * (other: Int): IntegerOption = this match {
    case NoInteger => NoInteger
    case _ => this * SomeInteger(other)
  }

  //IMPORANT NOTE -- this function is different from the others here
  //in that it returns NoInteger if EITHER of the inputs is NoInteger
  def / (other: IntegerOption): IntegerOption = (this, other) match {
    case (SomeInteger(thisInt), SomeInteger(otherInt)) =>
      SomeInteger((thisInt.toDouble/otherInt.toDouble).toInt)
    case (_, _) => NoInteger //if we fall through to here, at least one is NoInteger
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
  def apply(number: Int) =
    new SomeInteger(number)

  //returns first the sum of all the options
  // with 0 as the default for a NoInteger
  // and then the number of SomeIntegers in
  // the list (for use when calculating most
  //things
  def sum(list: List[IntegerOption]): (IntegerOption, Int) = {
    val notNones = list filter (x => !x.isEmpty)
    val numUsed = notNones.length
    if (numUsed == 0)//everything in the list is NoInteger
      NoInteger
    (SomeInteger((notNones map (x => x.get)).sum), numUsed)
  }

  /*
   * This converts a string to an IntegerOption
   *
   * However, the reader should note that this
   * will throw if it is passed a badly formatted
   * number.
   */
  implicit def toIntegerOption(x: String) =
    SomeInteger(x.toInt)
}