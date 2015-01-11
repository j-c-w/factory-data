package frontend

import java.text.SimpleDateFormat
import java.util.Calendar

import controllers.Global
import sun.util.calendar.LocalGregorianCalendar.Date

import scala.util.Try

/*
 * Created by Jackson Woodruff on 19/11/2014 
 *
 * This is a small object used exclusively for
 * serializing and un-serializing maps.
 */

object Serialization {
  def serialize(map: Map[String, Seq[String]]): String = {
    Global.getDateString + "\n" + map.map{
      case(identifier, stringSequence) => {
        identifier + ":->" + stringSequence.mkString("~.~")
      }
    }.mkString("\n")
  }

  def unserialize(string: String): Map[String, Seq[String]] = {
    // Drop the first line because it contains the date.
    // The date has already been dealt with by this stage.
    val lines = string.split("\n").drop(1)
    lines.map(line => {
      val lineSplit = line.split(":->")
      val end = Try(lineSplit.tail.head.split("~.~").toSeq)
      (lineSplit.head, end.getOrElse(List("")))
    }).toMap
  }
}
