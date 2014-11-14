package frontend.forms

/*
 * Created by Jackson Woodruff on 30/09/2014
 *
 * This is a class for holding the data entered in the form
 * At some point, the parameters that are taken
 * should be converted to lists to make multiple queries easier.
 */
case class SearchFormParser(filterData: List[FilterFormData], aggregateData: List[AggregateFormData], sortData: List[SortFormData])
              extends FormData[SearchFormParser] {
  def this() = this(List(new FilterFormData), List(new AggregateFormData), List(new SortFormData))

  def default = new SearchFormParser

  def toList = (filterData.flatMap(_.toList) ++
    aggregateData.flatMap(_.toList) ++
    sortData.flatMap(_.toList)).toList
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

  def toList = List(searchField, sortMethod)
}

case class FilterFormData(filteringField: String, filterComparator: String, filterText: String, combinator: String)
              extends FormData[FilterFormData] {
  def this() = this("", "", "", "")

  def default = new FilterFormData

  def toList = List(filteringField, filterComparator, filterText, combinator)
}

case class AggregateFormData(aggregatingField: String, aggregateMode: String)
              extends FormData[AggregateFormData] {
  def this() = this("", "")

  def default = new AggregateFormData

  def toList = List(aggregatingField, aggregateMode)
}
