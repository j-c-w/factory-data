package backend.scala.query

import backend.scala.datatypes.DataType

/*
 * Created by Jackson Woodruff on 27/07/2014 
 *
 * These classes were originally put together into
 * one big class called the aggregator. However,
 * thier functions, although related, are all
 * independant of one another, and this format
 * provides easier passing of data.
 */

abstract class AggregateMode[T <: DataType[T]] {
  protected def converter(list: List[T]): List[ResultListObject[T]] =
    list map (x => new ResultListObject[T](x))

  def aggregate(data: List[T]): List[ResultListObject[T]]
}

case class AggregateSum[T <: DataType[T]]() extends AggregateMode[T] {
  def aggregate(data: List[T]): List[ResultListObject[T]] = List({
    val functionApplied = converter(data)
    (functionApplied.tail fold functionApplied.head)((x, y) => {
      x mergeSum y
    })
  })
}
case class AggregateAverage[T <: DataType[T]]() extends AggregateMode[T] {
  def aggregate(data: List[T]): List[ResultListObject[T]] = {
    //doing a head operation w/out checking is fine because the
    //aggregatesum is (at the moment) guaranteed to return a list with
    //one item
    List(new AggregateSum[T].aggregate(data).head.averageBy)
  }
}
case class NoAggregate[T <: DataType[T]]() extends AggregateMode[T] {
  def aggregate(data: List[T]): List[ResultListObject[T]] =
    data map (x => new ResultListObject(x))
}
case class AggregateAverageBy[K, T <: DataType[T]](f: T => K)extends AggregateMode[T] {
  def aggregate(data: List[T]): List[ResultListObject[T]] = {
    (new AggregateSumBy[K, T](f).aggregate(data)) map (_.averageBy)
  }

}
case class AggregateSumBy[K, T <: DataType[T]](f:  T => K) extends AggregateMode[T] {
  def aggregate(data: List[T]): List[ResultListObject[T]] = {
    val groupedDataSet = (data groupBy f) map { case(_, groupedData) => groupedData}
    //once again, it is worth nothing that the .head call is perfectly safe --
    //aggregateSum will always return a single item in a list
    (groupedDataSet map (new AggregateSum[T].aggregate(_).head)).toList
  }
}
