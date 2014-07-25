package main.scala.datatypes.options

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

abstract class DataOption[+T] {
  def isEmpty: Boolean
  def get: T
  //this is a method that acts the like the
  //double bar or operator. If the first item
  //is SomeData, it is returned. Otherwise,
  //the second object is returned regardless
  //of value
  def or[A >: T](option: => DataOption[A]): DataOption[A]
}

