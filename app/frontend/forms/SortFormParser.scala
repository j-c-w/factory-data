package frontend.forms

/*
 * Created by Jackson Woodruff on 30/09/2014
 *
 * This is a class for holding the data entered in the form
 * At some point, the parameters that are taken
 * should be converted to lists to make multiple queries easier.
 */
case class SearchFormParser(filterData: FilterFormData, aggregateData: AggregateFormData, sortData: SortFormData)
              extends FormData[SearchFormParser] {
  def this() = this(new FilterFormData, new AggregateFormData, new SortFormData)

  def default = new SearchFormParser
}



/*
 * This is a set of three classes that
 * take the data. Will potentially modify
 * so they can return the correct ...Parser
 * classes.
 */
case class SortFormData(searchField: String, sortMethod: String) extends FormData[SortFormData] {
  def this() = this("", "")

  def default = new SortFormData
}

case class FilterFormData(filteringField: String, filterComparator: String, filterText: String)
              extends FormData[FilterFormData] {
  def this() = this("", "", "")

  def default = new FilterFormData
}

case class AggregateFormData(aggregatingField: String, aggregateMode: String)
              extends FormData[AggregateFormData] {
  def this() = this("", "")

  def default = new AggregateFormData
}
