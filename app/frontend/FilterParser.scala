package frontend

import backend.scala.datatypes.options.MathComparable
import backend.scala.datatypes.{LineListObject, DataType, DataField}
import backend.scala.query.FilterBuilder

/*
 * Created by Jackson Woodruff on 24/08/2014 
 *
 */

object FilterParser {
  def getBuilder[A <: MathComparable[A]](field: DataField[A], comparison: ComparisonMethod, value: A): FilterBuilder[LineListObject] =
    new FilterBuilder[LineListObject](x => comparison.compare(value, field.get(x)))
}
