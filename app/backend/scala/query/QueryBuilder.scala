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
                                      aggregateMode: AggregateMode,
                                      sortBuilder: SortBuilder[T]) {
  def this() = this(null, NoAggregate[Nothing, Nothing], null)

  def addFilter(f: T => Boolean, combinator: (Boolean, Boolean) => Boolean) =
    if (filterBuilder == null) new QueryBuilder(new FilterBuilder[T](f), aggregateMode, sortBuilder)
    else new QueryBuilder(filterBuilder.add(f, combinator), aggregateMode, sortBuilder)

  def addFilterAnd(f: T => Boolean) =
    addFilter(f, _ && _)

  def addFilterOr(f: T => Boolean) =
    addFilter(f, _ || _)

  def setAggregate(mode: AggregateMode) =
    new QueryBuilder[T](filterBuilder, mode, sortBuilder)

  def addSort(f: (ResultListObject[T], ResultListObject[T]) => Boolean) =
    if (sortBuilder == null) new QueryBuilder(filterBuilder, aggregateMode, new SortBuilder[T](f, true))
    else new QueryBuilder(filterBuilder, aggregateMode, sortBuilder.add(f))

  def processData(data: List[T]) = {
    sortData(aggregateData(filterData(data)))
  }

  private def filterData(data: List[T]) =
    if (filterBuilder == null) data
    else filterBuilder.filter(data)

  private def aggregateData(data: List[T]): List[ResultListObject[T]] =
    aggregateMode.aggregate(data)

  private def sortData(data: List[ResultListObject[T]]) = {
    if (sortBuilder == null) data
    else sortBuilder.sortBy(data)
  }



}
