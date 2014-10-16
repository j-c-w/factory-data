package frontend.forms

import backend.scala.datatypes.options.MathComparable
import backend.scala.datatypes.{NothingDataField, DataType, DataField, LineListObject}
import backend.scala.query._
import frontend._

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

  /*
   * This converts a list of sort form data into a single SortBuilder
   */
  def sortForm(formData: List[SortFormData]): SortBuilder[LineListObject] = formData match {
    case Nil => new SortBuilder[LineListObject]()
    case data :: Nil => sortForm(formData)
    case _ => formData.tail.foldRight (sortFormToBuilder(formData.head)) ( { case(data, builder) => builder.add(sortFormToBuilder(data)) } )
  }

  /*
   * This is once again like the above method, but it is for a single sort form
   */
  private def sortFormToBuilder(formData: SortFormData): SortBuilder[LineListObject] = {
    val comparator = formData.sortMethod match {
      case "Ascending" => GreaterThan
      case "Descending" => LessThan
    }
    val field = DataField.fromString(formData.searchField)
    //after getting everything out of the form we put it all together using that beautiful method defined in
    //field.compare()
    new SortBuilder[LineListObject]({ case (resultOne, resultTwo) => field.compare(resultOne, resultTwo, comparator) })
  }

  def aggregateForm(formData: AggregateFormData): AggregateMode[LineListObject] = {
    ???
  }

  def wholeForm(formData: SearchFormParser): QueryBuilder[LineListObject] = {
    val filterBuilder = searchForm(formData.filterData)
    val sortBuilder = sortForm(formData.sortData)

    new QueryBuilder(Some(filterBuilder), new NoAggregate[LineListObject], Some(sortBuilder))
  }

  //def filterForm(formData: )
}
