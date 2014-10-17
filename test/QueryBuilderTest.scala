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
      val queryBuilder = new QueryBuilder[LineListObject](Some(filterBuilder), new NoAggregate[LineListObject], None)

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
}
