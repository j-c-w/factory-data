package main.scala.query


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

class SortBuilder(functions: List[(ResultListObject, ResultListObject) => Boolean]) {
  def this(f: (ResultListObject, ResultListObject) => Boolean, single: Boolean) = this (List(f))
  def this() = this(Nil)

  def add(f2: (ResultListObject, ResultListObject) => Boolean) =
    new SortBuilder(functions :+ f2)

  def sortBy(list: List[ResultListObject]): List[ResultListObject] = functions match {
    case Nil => list
    case x :: xs => new SortBuilder(xs).sortBy(list sortWith x)
  }
}
