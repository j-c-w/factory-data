package frontend

import scala.util.Try

/*
 * Created by Jackson Woodruff on 19/11/2014 
 *
 * This is a small object used exclusively for
 * serializing and un-serializing maps.
 */

object Serialization {
  def serialize(map: Map[String, Seq[String]]): String = {
    map.map{
      case(identifier, stringSequence) => {
        identifier + ":->" + stringSequence.mkString("~.~")
      }
    }.mkString("\n")
  }

  def unserialize(string: String): Map[String, Seq[String]] = {
    val lines = string.split("\n")
    lines.map(line => {
      val lineSplit = line.split(":->")
      val end = Try(lineSplit.tail.head.split("~.~").toSeq)
      (lineSplit.head, end.getOrElse(List()))
    }).toMap
  }
}
