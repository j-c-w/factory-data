package backend.scala.query

/*
 * Created by Jackson Woodruff on 27/07/2014 
 *
 * This is a class to help the QueryBuilder determine
 * whether to sum or to average values in a type safe
 * object oriented and easily extensible way.
 */

abstract class AggregateMode {
  def aggregateData[T](data: List[T]) = {
    Aggregator.aggregateWith(data, this)
  }
}

case object AggregateSum extends AggregateMode
case object AggregateAverage extends AggregateMode
case object NoAggregate extends AggregateMode
case class AggregateAverageBy[K, T](f: T => K)extends AggregateMode
case class AggregateSumBy[K, T](f:  T => K) extends AggregateMode
