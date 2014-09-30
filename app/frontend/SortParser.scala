package frontend

import backend.scala.datatypes.{LineListObject, DataField}
import backend.scala.datatypes.options.MathComparable
import backend.scala.query.SortBuilder

/*
 * Created by Jackson Woodruff on 25/08/2014 
 *
 * After already having designed two classes,
 * I have decided that there is nothing worth
 * keeping about a class for these parser
 * things.
 */

object SortParser {
  def toList =
    List("Ascending", "Descending")

  def fromString[T <: MathComparable[T]](order: String, field: DataField[T], comparator: ComparisonMethod) = order match {
    case "Ascending" => new SortBuilder[LineListObject](
      (o1, o2) => comparator.compare(field.get(o1.lineObject), field.get(o2.lineObject)), true)
    case ("Descending") => new SortBuilder[LineListObject](
      (o1, o2) => !comparator.compare(field.get(o1.lineObject), field.get(o2.lineObject)), true)
  }
}
