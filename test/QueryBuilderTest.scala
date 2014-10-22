import backend.scala.datatypes.LineListObject
import backend.scala.datatypes.options.DoubleOption
import backend.scala.query._
import controllers.Global
import org.specs2.mutable.Specification

/*
 * Created by Jackson Woodruff on 17/10/2014 
 *
  */

class QueryBuilderTest extends Specification {
  "FilterBuilder" should {
    "Return an empty list" in {
      val filterBuilder = new FilterBuilder[LineListObject](_.lineCode == 0)
      val queryBuilder = new QueryBuilder[LineListObject](Some(filterBuilder), new AggregateBuilder[LineListObject], None)

      val data = queryBuilder.processData(Global.baseData)
      data.length mustEqual 0
    }
  }

  "AggregateBuilder" should {
    "Return a singleton list" in {
      val aggregateBuilder = new AggregateBuilder[LineListObject]
      aggregateBuilder.add(new AggregateSumBy[DoubleOption, LineListObject](_.getTotalSupervisors))
      aggregateBuilder.add(new AggregateAverageBy[DoubleOption, LineListObject](_.getTotalProductionWorkersAbsent))
      aggregateBuilder.add(new AggregateAverage[LineListObject])

      val data = aggregateBuilder.aggregateData(Global.baseData)
      data.length mustEqual 1

    }
  }

  "SortBuilder" should {
    "Return a list where the total number of production workers is 0 in the top item" in {
      val sortBuilder = new SortBuilder[LineListObject]()
      sortBuilder.add({ case (d1, d2) => d1.lineObject.getTotalProductionWorkers > d1.lineObject.getTotalProductionWorkers})

      val newData = sortBuilder.sortBy(new NoAggregate[LineListObject].aggregate(Global.baseData))
      newData.head.lineObject.getTotalProductionWorkers == DoubleOption(0) must equalTo(true)

    }
  }
}
