package backend.scala.query

import backend.scala.datatypes.DataType


/*
 * Created by Jackson Woodruff on 22/07/2014 
 *
 * The empty consructor will not filter anything out
 *
 */

class FilterBuilder[T <: DataType[T]](val f: T => Boolean) {
  def this() = this(x => true)

  def and(f2: T => Boolean) =
    add(f2, _ && _)

  def or(f2: T => Boolean) =
    add(f2, _ || _)

  def add(f2: T => Boolean, combinator: (Boolean, Boolean) => Boolean) =
    new FilterBuilder[T](x => combinator(f(x), f2(x)))
  
  def filter(list: List[T]): List[T] =
    list filter (f)
}
