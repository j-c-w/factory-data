package main.scala.query

/*
 * Created by Jackson Woodruff on 22/07/2014 
 * 
 * Simple class that contains basic logic operations
 */

trait BooleanOps extends Function2[Boolean, Boolean, Boolean]

case object AND extends BooleanOps {
  def apply (x: Boolean, y: Boolean) = x && y
}

case object OR extends BooleanOps {
  def apply (x: Boolean, y: Boolean) = x || y
}

case object XOR extends BooleanOps {
  def apply (x: Boolean, y: Boolean) = x ^ y
}