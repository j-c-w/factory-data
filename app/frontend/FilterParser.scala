package frontend

import backend.scala.datatypes.DataField
import backend.scala.query.FilterBuilder

/*
 * Created by Jackson Woodruff on 24/08/2014 
 *
 * This could be an object right now.
 *
 * However, to facilitate potential design additions,
 * I have left this as a class.
 */

class FilterParser[A, B](field: DataField[A], comparison: ComparisonMethod, value: A) {
  def getBuilder[A <: DataField[A]]: FilterBuilder[A] =
    new FilterBuilder[A](x => comparison.compare(value, x))
}
