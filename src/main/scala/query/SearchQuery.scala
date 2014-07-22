package main.scala.query

import main.scala.datatypes._

/*
 * Created by Jackson Woodruff on 22/07/2014 
 * 
 */

class SearchQuery(actions: List[SearchAction]) {
  this = new SearchQuery(Nil)

  def add(action: SearchAction, combinator: (SearchAction, SearchAction) => SearchAction) =
    new SearchQuery()
}
