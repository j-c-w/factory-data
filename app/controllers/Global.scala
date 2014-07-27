package controllers

import scala.util.Random

/*
 * Created by Jackson Woodruff on 27/07/2014 
 * 
 */

object Global {
  lazy val randoms: Stream[Char] = new Random().alphanumeric
  def nextNRandoms(n: Int): Array[Char] =
    randoms.take(n).toArray
}
