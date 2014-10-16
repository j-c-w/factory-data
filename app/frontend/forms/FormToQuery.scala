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
  def searchForm(formData: List[FilterFormData]): FilterBuilder[LineListObject] = {
    val base = new FilterBuilder[LineListObject](x => true)
    formData.foldRight(base) ((form, filterBuilder) => {
      val formBuilder = searchFormToBuilder(form)

      form.combinator match {
        case ("Or") => filterBuilder.or(formBuilder.f)
        case ("And") => filterBuilder.and(formBuilder.f)
      }
    })
  }

  /*
   * This is just a private version of the above method that converts from a single
   * form into a single item long FilterBuilder. Hence, we can ignore the combinator
   * for now
   */
  private def searchFormToBuilder(form: FilterFormData) = {
    val field = DataField.fromString(form.filteringField)
    val comparator = ComparisonMethod.fromString(form.filterComparator)
    val comparisonValue = form.filterText

    new FilterBuilder[LineListObject](x => {
      field.compare(x, comparator, comparisonValue) match {
        case Success(result) => result
        //because this is called when there was a problem converting the string to the desired type,
        //it cannot possibly match the required value and so must be false.
        case Failure(error) => false
      }
    })
  }

  def sortForm(formData: SortFormData): SortBuilder[LineListObject] = ???

  def aggregateForm(formData: List[AggregateFormData]): AggregateMode[LineListObject] = {
    ???
  }

  def wholeForm(formData: SearchFormParser): QueryBuilder[LineListObject] = {
    val filterBuilder = searchForm(filterBuilder)

    new QueryBuilder(Some(filterBuilder), new NoAggregate[LineListObject], None)
  }

  //def filterForm(formData: )
}
