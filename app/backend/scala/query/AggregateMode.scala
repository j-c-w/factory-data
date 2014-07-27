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

abstract class AggregateMode {
  protected def converter[T <: DataType[T]](list: List[T]): List[ResultListObject[T]] =
    list map (x => new ResultListObject[T](x))

  def aggregate[T <: DataType[T]](data: List[T]): List[ResultListObject[T]]
}

case object AggregateSum extends AggregateMode {
  def aggregate[T <: DataType[T]](data: List[T]): List[ResultListObject[T]] = List({
    val functionApplied = converter(data)
    (functionApplied.tail fold functionApplied.head)((x, y) => {
      x mergeSum y
    })
  })
}
case object AggregateAverage extends AggregateMode {
  def aggregate[T <: DataType[T]](data: List[T]): List[ResultListObject[T]] = {
    //doing a head operation w/out checking is fine because the
    //aggregatesum is (at the moment) guaranteed to return a list with
    //one item
    List(AggregateSum.aggregate(data).head.averageBy)
  }
}
case object NoAggregate extends AggregateMode {
  def aggregate[T <: DataType[T]](data: List[T]): List[ResultListObject[T]] =
    data map (x => new ResultListObject(x))
}
case class AggregateAverageBy[K, T <: DataType[T]](f: T => K)extends AggregateMode {
  def aggregate[G <: T](data: List[G]): List[ResultListObject[G]] = {
    (new AggregateSumBy(f).aggregate(data)) map (_.averageBy)
  }

}
case class AggregateSumBy[K, T <: DataType[T]](f:  T => K) extends AggregateMode {
  def aggregate[G <: T](data: List[G]): List[ResultListObject[G]] = {
    val groupedDataSet = (data groupBy f) map { case(_, groupedData) => groupedData}
    //once again, it is worth nothing that the .head call is perfectly safe --
    //aggregateSum will always return a single item in a list
    (groupedDataSet map (AggregateSum.aggregate(_).head)).toList
  }
}
