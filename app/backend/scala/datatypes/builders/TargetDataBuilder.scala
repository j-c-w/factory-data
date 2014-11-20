package backend.scala.datatypes.builders

import backend.scala.datatypes.TargetData

/*
 * Created by Jackson Woodruff on 20/11/2014 
 * 
 */

class TargetDataBuilder extends BuilderType[TargetData, TargetDataBuilder]{
  /*
     * This method is used to turn the builder
     * into a type of T.
     */
  override def build: TargetData = ???

  /*
   * This takes another builder object of the same type
   * and merges it to create a single builder
   */
  override def mergeSum(other: TargetDataBuilder): TargetDataBuilder = ???
}
