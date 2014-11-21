package backend.scala.datatypes.builders

import backend.scala.datatypes.FactoryDate
import backend.scala.datatypes.options.{IntegerOption, NoInteger}

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class FactoryDateBuilder extends BuilderType[FactoryDate, FactoryDateBuilder] {
  self =>

  var day: IntegerOption = NoInteger
  var month: IntegerOption = NoInteger
  var year: IntegerOption = NoInteger


  /*
   * This merges two builders together
   */
  def mergeSum(other: FactoryDateBuilder): FactoryDateBuilder = new FactoryDateBuilder {
    day = self.day mergeEqual other.day
    month = self.month mergeEqual other.month
    year = self.year mergeEqual other.year
  }

  /*
     * This method is used to turn the builder
     * into a type of T.
     */
  override def build: FactoryDate =
    new FactoryDate(day, month, year)
}
