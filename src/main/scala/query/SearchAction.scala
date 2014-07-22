package main.scala.query

import java.security.InvalidParameterException

import main.scala.datatypes.{FactoryCode, LineTypeList, LineListObject, LineType}

import scala.annotation.tailrec

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
    val searchReadyList = searches
    for {
      item <- list
      //convert this item into a list of successful searches
      //and their combinators
      (resultList, combinatorList) = searches map (x => (x.checkItem(item), x.combinator))
      if recursiveReduce(resultList, combinatorList)
    } yield item
  }

  //could be made tail recursive
  private def recursiveReduce(results: List[Boolean], combinators: List[(Boolean, Boolean) => Boolean]): Boolean =
    (results, combinators) match {
      case (Nil, _) => true
      case (_, Nil) => throw new InvalidParameterException("results list must be longer than combinators list")
      case (result :: Nil, combinator :: Nil) => result //this is the crucial exit case
      case (result :: restOf, combinator :: combinators) => combinator(result, recursiveReduce(restOf, combinators))
    }

  private val baseItem = new SearchAction[Any, Any](FactoryCode, x => true) {
    def combinator = (x, y) => y
  }
  
  def add(action: SearchAction[Any, Any]) =
    new Searches()
}