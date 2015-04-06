package frontend

import java.text.SimpleDateFormat
import java.util.Calendar

import controllers.Global
import play.api.templates.Xml
import sun.util.calendar.LocalGregorianCalendar.Date

import scala.util.Try
import scala.xml.{XML, Elem}

/*
 * Created by Jackson Woodruff on 19/11/2014 
 *
 * This is a small object used exclusively for
 * serializing and un-serializing maps.
 */

object Serialization {
  def serialize(map: Map[String, Seq[String]]): String = {
    val doc = <doc>
      <date>{Global.getDateString}</date>{
      for (x <- map.toList) yield {
        <series>
            <iden>{x._1}</iden>
            {
            for (item <- x._2) yield {
              <item>{item}</item>
            }
          }
        </series>
      }
    }</doc>

    doc.buildString(false)
  }

  def unserialize(string: String): Map[String, Seq[String]] = {
    val xml = XML.loadString(string)
    val series = xml \\ "series"
    val entries = series map {
      x => {
        val items = (x \\ "item") map (_.text)
        ((x \ "iden").text, items)
      }
    }
    entries.toMap
  }
}
