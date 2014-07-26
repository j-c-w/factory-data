package backend.scala.query

import backend.scala.datatypes.DataType


/*
 * Created by Jackson Woodruff on 23/07/2014 
 *
 *
 * This should be done last in the order of
 * searching, aggregating and finally sorting
 *
 * To ensure this order is kept, this takes a
 * ResultsListObject
 *
 * sorts are preformed in reverse order that
 * they are added.
 *
 * A sort with a functions list of Nil will
 * return the input list
 */

class SortBuilder[T <: DataType[T]](functions: List[(ResultListObject[T], ResultListObject[T]) => Boolean]) {
  def this(f: (ResultListObject[T], ResultListObject[T]) => Boolean, single: Boolean) = this (List(f))
  def this() = this(Nil)

  def add(f2: (ResultListObject[T], ResultListObject[T]) => Boolean) =
    new SortBuilder(functions :+ f2)

  def sortBy(list: List[ResultListObject[T]]): List[ResultListObject[T]] = functions match {
    case Nil => list
    case x :: xs => new SortBuilder(xs).sortBy(list sortWith x)
  }
}
