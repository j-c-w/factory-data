package main.scala.query

import main.scala.datatypes.{FactoryCode, LineTypeList, LineListObject, LineType}
/*
 * Created by Jackson Woodruff on 22/07/2014 
 * 
 */

abstract class SearchAction[B, T <: LineType[B]] (field: T, isValid: (B => Boolean)) {
  def search(list: List[LineListObject]): List[LineListObject] = 
    list.filter(x => isValid(field.get(x)))

  def checkItem(item: LineListObject): Boolean = isValid(field.get(item))

  //defines the way in which the next item should be added (i.e. &&)
  abstract def combinator: ((Boolean, Boolean) => Boolean)
}

class Searches(searches: List[SearchAction[Any, Any]]) {
  def search(list: List[LineListObject]): List[LineListObject] = {
    val searchReadyList = baseItem :: searches
    for (item <- list) {
      //list of the booleans for each seach param
      val resultList = searches map (_.checkItem(item))
      //now combine this into one
      //for (0 until resultLength)
    }
  }

  private val baseItem = new SearchAction[Any, Any](FactoryCode, x => true) {
    def combinator = _ && _
  }
  
  def add(action: SearchAction[Any, Any]) =
    new Searches()
}