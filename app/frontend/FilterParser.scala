package frontend

import backend.scala.datatypes.options.MathComparable
import backend.scala.datatypes.{LineListObject, DataType, DataField}
import backend.scala.query.FilterBuilder

/*
 * Created by Jackson Woodruff on 24/08/2014 
 *
 * This could be an object right now.
 *
 * However, to facilitate potential design additions,
 * I have left this as a class.
 */

class FilterParser[A <: MathComparable[A]](field: DataField[A], comparison: ComparisonMethod, value: A) {
  def getBuilder: FilterBuilder[LineListObject] =
    new FilterBuilder[LineListObject](x => comparison.compare(value, field.get(x)))
}
