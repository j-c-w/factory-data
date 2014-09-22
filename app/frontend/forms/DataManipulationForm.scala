package frontend.forms

import play.api.data.Form
import play.api.data._
import play.api.data.Forms._

/*
 * Created by Jackson Woodruff on 01/09/2014 
 *
 * This is a form and all the handlers
 * required for the form that enables
 * the manipulation of data.
 */

object DataManipulationForm {
  private val sortForm = mapping (
    "Field" -> text,
    "Sort Order" -> text
  ) (SortFormData.apply) (SortFormData.unapply)

  val filterForm = mapping (
    "Field" -> text,
    "Comparison Method" -> text,
    "Value" -> text
  ) (FilterFormData.apply) (FilterFormData.unapply)

  private val aggregateForm = mapping (
    "Field" -> text,
    "Mode" -> text
  ) (AggregateFormData.apply) (AggregateFormData.unapply)

  //This is the master val that combines
  //the other forms
  val form = Form (
    mapping (
      "Filter" -> filterForm,
      "Aggregate" -> aggregateForm,
      "Sort" -> sortForm
    ) (SearchFormParser.apply) (SearchFormParser.unapply)
  )
}

case class SearchFormParser(filterData: FilterFormData, aggregateData: AggregateFormData, sortData: SortFormData)
              extends FormData[SearchFormParser] {
  def this() = this(new FilterFormData, new AggregateFormData, new SortFormData)

  def default = new SearchFormParser
}

/*
 * A simple trait to enable the passing
 * of any of these to a single parser method,
 * enabling me to store these in a single list
 */
trait FormData[T <: FormData[T]] {
  def default: T
}

/*
 * This is a set of three classes that
 * take the data. Will potentially modify
 * so they can return the correct ...Parser
 * classes.
 */
protected case class SortFormData(searchField: String, sortMethod: String) extends FormData[SortFormData] {
  def this() = this("", "")

  def default = new SortFormData
}

case class FilterFormData(filteringField: String, filterComparator: String, filterText: String)
              extends FormData[FilterFormData] {
  def this() = this("", "", "")

  def default = new FilterFormData
}

protected case class AggregateFormData(aggregatingField: String, aggregateMode: String)
              extends FormData[AggregateFormData] {
  def this() = this("", "")

  def default = new AggregateFormData
}