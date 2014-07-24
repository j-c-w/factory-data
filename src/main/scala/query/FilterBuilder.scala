package main.scala.query

import main.scala.datatypes.{DataType}


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
  /*
   * Created by Jackson Woodruff on 24/07/2014
   *
   * This trait is here for historical reference, not actual use.
   *
   * The real problem with this trying to use this trait is that
   * the anonymous functions that I have been using to shift
   * data around do not work at all well with this model.
   *
   * It is possible to define the correct functions that you need
   * and have them return the correct datatypes, as is shown below,
   * but the problem comes when you cannot access the other fields
   * of the class
   */

  private trait DataType {

  }

  def or(f2: T => Boolean) =
    add(f2, _ && _)

  def add(f2: T => Boolean, combinator: (Boolean, Boolean) => Boolean) =
    new FilterBuilder[T](x => combinator(f(x), f2(x)))
  
  def filter(list: List[T]): List[T] =
    list filter (f)
}
