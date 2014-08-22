package backend.scala.datatypes.options

/*
 * Created by Jackson Woodruff on 20/07/2014 
 *
 * This is an adaptation of the standard
 * Scala class Option[T]. However, this
 * setup changes the toString method
 * to return either None or the value
 * (rather than None or Some(value)
 */

//also, it is rather unhelpful that you
//cannot extend option
// because it would be superflous to implement
//all those methods, I have left many uninplemented
//implement them as need be

case object NoData extends DataOption[scala.Nothing] {
  def isEmpty: Boolean = true
  def get = throw new NoSuchElementException("NoData.get")
  //see abstract class definition
  def or[T](option: => DataOption[T]) = option
  override def toString = "No Data"
}

case class SomeData[+T](x: T) extends DataOption[T] {
  def isEmpty = false
  def get = x
  def or[A >: T](option: => DataOption[A]) = SomeData.this
  override def toString: String = x.toString
}

abstract class DataOption[+T] extends SimpleComparable[DataOption[T]]{
  def isEmpty: Boolean
  def get: T
  //this is a method that acts the like the
  //double bar or operator. If the first item
  //is SomeData, it is returned. Otherwise,
  //the second object is returned regardless
  //of value
  def or[A >: T](option: => DataOption[A]): DataOption[A]

  override def == (other: DataOption[T]): Boolean = (this, other) match {
    case (SomeData(x), SomeData(y)) => x == y
    case (NoData, NoData) => true
    case (_, _) => false
  }

  override def > (other: DataOption[T]): Boolean =
    (this compareTo other) == 1

  override def <= (other: DataOption[T]): Boolean = {
    val compared: Int = this compareTo other
    compared == 0 || compared == 1
  }
  override def < (other: DataOption[T]): Boolean =
    (this compareTo other) == -1

  override def >= (other: DataOption[T]): Boolean = {
    val compared: Int = this compareTo other
    compared == 0 || compared == -1
  }

  private def compareTo(other: DataOption[T]) = (this, other) match {
    case (NoData, NoData) => 0
    case (SomeData(x), SomeData(y))
      if x.isInstanceOf[Comparable[_]] && y.isInstanceOf[Comparable[_]]
        => x.asInstanceOf[Comparable[_]].compareTo(y.asInstanceOf)
    case (_, _) => -2 //at this point, there is no comparison possible
      //is NoData < SomeData?? Therefore -2 is returned
  }
}

