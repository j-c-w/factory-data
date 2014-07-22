package main.scala.query

import main.scala.datatypes._

/*
 * Created by Jackson Woodruff on 22/07/2014 
 *
 *
 * A small class that hides the monstrous complexity
 * of the Searches and SearchAction classes
 */

class SearchQuery[A](action: SearchAction[A]) {
  def add(action: SearchAction[A], combinator: (Boolean, Boolean) => Boolean) =
    new SearchQuery(action.add(action, combinator))

  def search(list: List[LineListObject]): List[LineListObject] =
    action.search(list)
}


