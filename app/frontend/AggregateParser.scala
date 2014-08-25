package frontend

import java.security.InvalidParameterException

import backend.scala.datatypes.{LineListObject, DataField, DataType}
import backend.scala.datatypes.options.MathComparable
import backend.scala.query._

/*
 * Created by Jackson Woodruff on 24/08/2014 
 *
 * THis contains the to List and fromString methods
 * along with all the other types of aggregation that can happen
 */
object AggregateParser {

  def toList =
    List("Aggregate Average By",
      "Aggregate Sum By",
      "Aggregate Average",
      "Aggregate Sum")

  /*
   * Here the field can be none iff Aggregate Average or
   * Aggregate sum are the string parameters.
   */
  def fromString[T <: MathComparable[T]] (s: String, field: Option[DataField[T]]) = {
    def invalidParameter =
      throw new InvalidParameterException("Field must not be None")

    s match {
      case "Aggregate Average By" => getAggregateAverageBy(field.getOrElse(invalidParameter))
      case "Aggregate Sum By" => getAggregateSumBy(field.getOrElse(invalidParameter))
      case "Aggregate Average" => getAggregateAverage
      case "Aggregate Sum" => getAggregateSum
    }
  }

  def getAggregateAverageBy[K <: MathComparable[K]](field: DataField[K]) =
    new AggregateAverageBy[K, LineListObject](field.get(_))

  def getAggregateSumBy[K <: MathComparable[K]](field: DataField[K]) =
    new AggregateSumBy[K, LineListObject](field.get(_))

  def getAggregateAverage =
    new AggregateAverage

  def getAggregateSum =
    new AggregateSum
}
