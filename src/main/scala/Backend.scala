package main.scala

import java.util.Date

import main.scala.datatypes.LineListObject


/*
 * Created by Jackson Woodruff on 20/07/2014 
 * 
 */

object Backend {
  def loadData(): Array[LineListObject] =
    DataLoader.dataAsList.toArray filter (x => !x.lineCode.isEmpty && x.lineCode.get == 100413)

}
