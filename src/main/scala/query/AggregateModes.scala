package main.scala.query

/*
 * Created by Jackson Woodruff on 22/07/2014 
 * 
 */

object AggregateModes extends Enumeration {
  type Mode = Value
  //numeric only
  val Sum = Value("Sum")
  val Average = Value("Average")
  //for any type
  val First = Value("First")
  val Mode = Value("Mode")
}
