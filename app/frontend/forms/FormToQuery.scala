package frontend.forms

import backend.scala.datatypes.options.MathComparable
import backend.scala.datatypes.{NothingDataField, DataType, DataField, LineListObject}
import backend.scala.query._
import frontend.{ComparisonMethod, SortParser}
import frontend.FilterParser

import scala.util.{Failure, Success, Try}

/*
 * Created by Jackson Woodruff on 30/09/2014 
 *
 *
 * THis is an object that enables conversion from
 * the Forms that are passed to it to a QueryBuilder
 * that can be applied to the data.
 *
 * This is currently not complete -- I am still focusing
 * on sorting out just filtering a single field before I
 * even think about filtering a group of fields
 */

object FormToQuery {
  /*
   * This converts from a search form data into a FilterBuilder
   */
  def searchForm(formData: Try[FilterFormData]): Option[FilterBuilder[LineListObject]] = {
    val filledForm = formData match {
      case Failure(fail) => new FilterFormData
      case Success(parser) => parser
    }

    println(filledForm.filterComparator)
    println("_________________________________________________")

    val field = DataField.fromString(filledForm.filteringField)
    val comparator = ComparisonMethod.fromString(filledForm.filterComparator)
    val compareTo = filledForm.filterText

    Some(new FilterBuilder[LineListObject]((x: LineListObject) => {
      val comparison = field.compare(x, comparator, compareTo)
      comparison match {
        case Success(value) => value
        case Failure(error) => false
      }
    }))
  }

  def sortForm(formData: SortFormData): SortBuilder[LineListObject] = ???

  def aggregateForm(formData: AggregateFormData): AggregateMode[LineListObject] = ???

  def wholeForm(formData: SearchFormParser): QueryBuilder[LineListObject] = {
    val filterBuilder = searchForm(Try(formData.filterData))

    new QueryBuilder(filterBuilder, new NoAggregate[LineListObject], None)
  }

  //def filterForm(formData: )
}
