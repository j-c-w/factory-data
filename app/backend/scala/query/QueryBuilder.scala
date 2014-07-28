package backend.scala.query

import backend.scala.datatypes.DataType
import backend.scala.datatypes.options.{SomeData, NoData, DataOption}

/*
 * Created by Jackson Woodruff on 27/07/2014 
 *
 * This is an assisting class to help build up querries.
 *
 *
 */

class QueryBuilder[T <: DataType[T]](filterBuilder: DataOption[FilterBuilder[T]],
                                      aggregateMode: AggregateMode[T],
                                      sortBuilder: DataOption[SortBuilder[T]]) {
  def this() = this(NoData, NoAggregate[T], NoData)

  def addFilter(f: T => Boolean, combinator: (Boolean, Boolean) => Boolean) = filterBuilder match {
    case (NoData) => new QueryBuilder[T](SomeData(new FilterBuilder[T](f)), aggregateMode, sortBuilder)
    case (SomeData(builder)) => new QueryBuilder[T](SomeData(builder.add (f, combinator)),
                                      aggregateMode, sortBuilder)
  }

  def addFilterAnd(f: T => Boolean) =
    addFilter(f, _ && _)

  def addFilterOr(f: T => Boolean) =
    addFilter(f, _ || _)

  def setAggregate(mode: AggregateMode[T]) =
    new QueryBuilder[T](filterBuilder, mode, sortBuilder)

  def addSort(f: (ResultListObject[T], ResultListObject[T]) => Boolean) = sortBuilder match {
    case (NoData) => new QueryBuilder(filterBuilder, aggregateMode, SomeData(new SortBuilder[T](f, true)))
    case (SomeData(builder)) => new QueryBuilder(filterBuilder, aggregateMode, SomeData(builder.add(f)))
  }

  def processData(data: List[T]) = {
    sortData(aggregateData(filterData(data)))
  }

  private def filterData(data: List[T]) = filterBuilder match {
    case (NoData) => data
    case (SomeData(builder)) => builder.filter(data)
  }

  private def aggregateData(data: List[T]): List[ResultListObject[T]] =
    aggregateMode.aggregate(data)

  private def sortData(data: List[ResultListObject[T]]) = sortBuilder match {
    case (NoData) => data
    case (SomeData(builder)) => builder.sortBy(data)
  }



}
