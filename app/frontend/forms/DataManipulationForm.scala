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
  val searchForm = Form(
    mapping (
      "Field" -> text,
      "Comparison Method" -> text,
      "Value" -> text
    ) (SearchFormData.apply) (SearchFormData.unapply)
  )

  val filterForm = Form(
    mapping (
      "Field" -> text,
      "Comparison Method" -> text,
      "Value" -> text
    ) (FilterFormData.apply) (FilterFormData.unapply)
  )

  val aggregateForm = Form(
    mapping (
      "Field" -> text,
      "Comparison Method" -> text,
      "Mode" -> text
    ) (AggregateFormData.apply) (AggregateFormData.unapply)
  )
}

/*
 * A simple trait to enable the passing
 * of any of these to a single parser method,
 * enabling me to store these in a single list
 */
trait FormData

/*
 * This is a set of three classes that
 * take the data. Will potentially modify
 * so they can return the correct ...Parser
 * classes.
 */
case class SearchFormData(searchField: String, searchComparator: String, searchText: String) extends FormData {

}

case class FilterFormData(filteringField: String, filterComparator: String, filterText: String) extends FormData {

}

case class AggregateFormData(aggregatingField: String, aggregatingComparator: String, aggregateMode: String) extends FormData {

}