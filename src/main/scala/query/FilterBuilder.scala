package main.scala.query

import main.scala.datatypes.LineListObject

/*
 * Created by Jackson Woodruff on 22/07/2014 
 *
 * The empty consructor will not filter anything out
 *
 */

class FilterBuilder(val f: LineListObject => Boolean) {
  def this() = this(x => true)

  def add(f2: LineListObject => Boolean, combinator: (Boolean, Boolean) => Boolean) =
    new FilterBuilder(x => combinator(f(x), f2(x)))
  
  def filter(list: List[LineListObject]): List[LineListObject] =
    list filter (f)
}
