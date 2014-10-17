package backend.scala.query

import backend.scala.datatypes.{LineListObject, DataType}

/*
 * Created by Jackson Woodruff on 16/10/2014 
 *
 *
 * This is a class that is used to collect
 * several AggregateModes into a single
 * class that can be used to aggregate several
 * times in a row
 */

class AggregateBuilder[T <: DataType[T]](modes: List[AggregateMode[T]]) {
  def this() = this(new NoAggregate[LineListObject])

  def add(singleMode: AggregateMode[T]) =
    new AggregateBuilder[T](modes :+ singleMode)

  //although this is not tail recursive,
  //it would be insane for anyone to want to
  //do 1000+ aggregations (would there be any data left?)
  def aggregateData(data: List[T]): List[ResultListObject[T]] = {
    //this is the ending case
    if (modes.isEmpty) (new NoAggregate[T]).aggregate(data)
    else {
      //otherwise, recurse more
      val aggregated = modes.head.aggregate(data)
      val lineListObjects = aggregated map(_.lineObject)
      (new AggregateBuilder[T](modes.tail)).aggregateData(lineListObjects)
    }
  }
}
