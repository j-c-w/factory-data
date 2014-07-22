package main.scala.query

import main.scala.datatypes.LineListObject

/*
 * Created by Jackson Woodruff on 22/07/2014 
 * 
 */

class FilterBuilder(val f: LineListObject => Boolean) {
  def add(f2: LineListObject => Boolean, combinator: (Boolean, Boolean) => Boolean) =
    new FilterBuilder(x => combinator(f(x), f2(x)))
  
  
}
