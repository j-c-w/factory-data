package backend.scala.query

import backend.scala.datatypes.DataType

/*
 * Created by Jackson Woodruff on 27/07/2014 
 *
 * This is an assisting class to help build up querries.
 *
 *
 */

class QueryBuilder[T <: DataType[T]](filterBuilder: FilterBuilder[T],
                                      aggregateBuilder: AggregateMode,
                                      sortBuilder: SortBuilder[T]) {
  def this() = this(null, NoAggregate, null)

  def addFilter(f: T => Boolean, combinator: (Boolean, Boolean) => Boolean) =
    if (filterBuilder == null) new QueryBuilder(new FilterBuilder[T](f), aggregateBuilder, sortBuilder)
    else new QueryBuilder(filterBuilder.add(f, combinator), aggregateBuilder, sortBuilder)

  def addFilterAnd(f: T => Boolean) =
    addFilter(f, _ && _)

  def addFilterOr(f: T => Boolean) =
    addFilter(f, _ || _)

  def setAggregate(mode: AggregateMode) =
    new QueryBuilder(filterBuilder, mode, sortBuilder)

  def addSort(f: (ResultListObject[T], ResultListObject[T]) => Boolean) =
    if (sortBuilder == null) new QueryBuilder(filterBuilder, aggregateBuilder, new SortBuilder[T](f, true))
    else new QueryBuilder(filterBuilder, aggregateBuilder, sortBuilder.add(f))

  def processData(data: List[T], aggregateMode: AggregateMode) = {
    sortData(aggregateData(filterData(data)))
  }

  private def filterData(data: List[T]) =
    if (filterBuilder == null) data
    else filterBuilder.filter(data)

  private def aggregateData(data: List[T]): List[ResultListObject[T]] =
    Aggregator.aggregateWith(data, aggregateBuilder)

  private def sortData(data: List[ResultListObject[T]]) = {
    if (sortBuilder == null) data
    else sortBuilder.sortBy(data)
  }



}
