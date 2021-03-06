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
  val sortForm = mapping (
    "Field" -> text,
    "Sort Order" -> text
  ) (SortFormData.apply) (SortFormData.unapply)

  val filterForm = mapping (
    "Field" -> text,
    "Comparison Method" -> text,
    "Value" -> text,
    "Combinator" -> text
  ) (FilterFormData.apply) (FilterFormData.unapply)

  val aggregateForm = mapping (
    "Field" -> text,
    "Mode" -> text
  ) (AggregateFormData.apply) (AggregateFormData.unapply)

  //This is the master val that combines
  //the other forms
  val form = Form (
    mapping (
      "Filter" -> list(filterForm),
      "Aggregate" -> list(aggregateForm),
      "Sort" -> list(sortForm)
    ) (SearchFormParser.apply) (SearchFormParser.unapply)
  )
}

