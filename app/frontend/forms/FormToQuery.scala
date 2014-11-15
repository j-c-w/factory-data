package frontend.forms

import backend.scala.datatypes.options.MathComparable
import backend.scala.datatypes.{NothingDataField, DataType, DataField, LineListObject}
import backend.scala.query._
import frontend._
import backend.scala.query.QueryBuilder

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
   * This is basically the mother method that takes all the
   * other methods here and puts them together
   */
  def parse(formData: (List[FilterFormData], List[SortFormData], List[AggregateFormData])) = {
    val (filters, sorting, aggregating) = formData

    val filterBuilder = searchForm(filters)
    val sortBuilder = sortForm(sorting)
    val aggregateBuilder = aggregateForm(aggregating)

    new QueryBuilder[LineListObject](Option(filterBuilder), aggregateBuilder, Option(sortBuilder))
  }

  /*
   * This converts from a search form data into a FilterBuilder
   */
  def searchForm(formData: List[FilterFormData]): FilterBuilder[LineListObject] = {
    def recurse(data: List[FilterFormData], builder: FilterBuilder[LineListObject]): FilterBuilder[LineListObject] = data match {
      case Nil => builder
      case x :: xs => {
        val f = searchFormToBuilder(x)
        x.combinator match {
          case "And" => recurse(xs, builder.and(f.f))
          case "Or" => recurse(xs, builder.or(f.f))
        }
      }
    }
    recurse(formData, new FilterBuilder[LineListObject]())
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
    case data :: Nil => sortFormToBuilder(data)
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

    def compareFields(o1: ResultListObject[LineListObject], o2: ResultListObject[LineListObject]): Boolean =
      field.compare(o1.lineObject, o2.lineObject, comparator)

    //after getting everything out of the form we put it all together using that beautiful method defined in
    //field.compare()
    new SortBuilder[LineListObject](compareFields, true)
  }

  def aggregateForm(formData: List[AggregateFormData]): AggregateBuilder[LineListObject] = {
    formData.foldRight(new AggregateBuilder[LineListObject]) ({
      case (data, builder) => builder.add(aggregateFormToMode(data))
    })
  }

  private def aggregateFormToMode(form: AggregateFormData) = {
    val field = DataField.fromString(form.aggregatingField)
    AggregateMode.fromString(form.aggregateMode, field)
  }

  def wholeForm(formData: SearchFormParser): QueryBuilder[LineListObject] = {
    val filterBuilder = searchForm(formData.filterData)
    val sortBuilder = sortForm(formData.sortData)
    val aggregateBuilder = aggregateForm(formData.aggregateData)

    new QueryBuilder(Some(filterBuilder), aggregateBuilder, Some(sortBuilder))
  }

  //def filterForm(formData: )
  //
}
