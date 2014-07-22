package main.scala.query

import main.scala.datatypes._

/*
 * Created by Jackson Woodruff on 22/07/2014 
 *
 *
 * A small class that hides the monstrous complexity
 * of the Searches and SearchAction classes
 */

class SearchQuery(actions: Searches) {
  def this() = this(Nil)

  def add(action: SearchAction[Any, Any], combinator: (SearchAction, SearchAction) => SearchAction) =
    new SearchQuery(actions.add(action))

  def search(list: List[LineListObject]): List[LineListObject] =
    actions.search(list)
}
