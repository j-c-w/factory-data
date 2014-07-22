package main.scala


import java.util.Date

import main.scala.datatypes._
import main.scala.query.{OR, SearchAction, SearchQuery}


/*
 * Created by Jackson Woodruff on 20/07/2014 
 * 
 */

object Backend {
  def loadData(): Array[LineListObject] = {
    query(DataLoader.dataAsList).toArray
  }

  def query(list: List[LineListObject]): List[LineListObject] =
    getQuery.search(list)

  def getQuery: SearchQuery[IntegerOption] = new SearchQuery[IntegerOption](
    new SearchAction (LineCode, {
      case NoInteger => false
      case SomeInteger(x) => x == 100403
    })).add(new SearchAction(LineCode, {case NoInteger => false; case SomeInteger(x) => x == 100404}), OR)

}
