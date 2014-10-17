import backend.scala.datatypes.LineListObject
import backend.scala.query.{NoAggregate, FilterBuilder, QueryBuilder}
import controllers.Global
import org.fluentlenium.core.filter.FilterBuilder
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
}
