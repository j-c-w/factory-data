package backend.scala.query

import backend.scala.datatypes.DataType

/*
 * Created by Jackson Woodruff on 27/07/2014 
 *
 * This is an assisting class to help build up querries.
 *
 *
 */

class QueryBuilder[T <: DataType[T]](filterBuilder: Option[FilterBuilder[T]],
                                      aggregateMode: AggregateMode[T],
                                      sortBuilder: Option[SortBuilder[T]]) {
  def this() = this(None, NoAggregate[T], None)

  def addFilter(f: T => Boolean, combinator: (Boolean, Boolean) => Boolean) = filterBuilder match {
    case (None) => new QueryBuilder[T](Some(new FilterBuilder[T](f)), aggregateMode, sortBuilder)
    case (Some(builder)) => new QueryBuilder[T](Some(builder.add (f, combinator)),
                                      aggregateMode, sortBuilder)
  }

  def addFilterAnd(f: T => Boolean) =
    addFilter(f, _ && _)

  def addFilterOr(f: T => Boolean) =
    addFilter(f, _ || _)

  def setAggregate(mode: AggregateMode[T]) =
    new QueryBuilder[T](filterBuilder, mode, sortBuilder)

  def addSort(f: (ResultListObject[T], ResultListObject[T]) => Boolean) = sortBuilder match {
    case (None) => new QueryBuilder(filterBuilder, aggregateMode, Some(new SortBuilder[T](f, true)))
    case (Some(builder)) => new QueryBuilder(filterBuilder, aggregateMode, Some(builder.add(f)))
  }

  def processData(data: List[T]) = {
    sortData(aggregateData(filterData(data)))
  }

  private def filterData(data: List[T]) = filterBuilder match {
    case (None) => data
    case (Some(builder)) => builder.filter(data)
  }

  private def aggregateData(data: List[T]): List[ResultListObject[T]] =
    aggregateMode.aggregate(data)

  private def sortData(data: List[ResultListObject[T]]) = sortBuilder match {
    case (None) => data
    case (Some(builder)) => builder.sortBy(data)
  }



}
