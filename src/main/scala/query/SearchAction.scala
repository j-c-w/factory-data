package main.scala.query

import java.security.InvalidParameterException

import main.scala.datatypes.{FactoryCode, LineTypeList, LineListObject, LineType}

import scala.annotation.tailrec

/*
 * Created by Jackson Woodruff on 22/07/2014 
 * 
 */

class SearchAction[B] (field: LineType[B], isValid: (B => Boolean)) {
  self =>

  val isValidFunc = isValid
  def search(list: List[LineListObject]): List[LineListObject] =
    list.filter(x => isValid(field.get(x)))

  def checkItem(item: LineListObject): Boolean = isValid(field.get(item))

  def add(other: SearchAction[B], combinator: (Boolean, Boolean) => Boolean): SearchAction[B] = {
    val valid = isValid
    new SearchAction[B](field, x => {self.isValid(x) && valid(x)})
  }
}
