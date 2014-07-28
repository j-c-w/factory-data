package backend.scala.graphing

import backend.scala.datatypes.DataType
import backend.scala.graphing.data.{XYData, CategoryDataSet, DataParser}
import backend.scala.query.ResultListObject
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.data.general.DefaultKeyedValues2DDataset

/*
 * Created by Jackson Woodruff on 26/07/2014 
 *
 * This class stores data for multiple series on a single
 * bar chart.
 *
 *
 */

class BarChartData[A <: Comparable[_], T <: DataType[T]](dataParser: List[DataParser[A, T]]) extends ChartData {
  def this(xAxis: ResultListObject[T] => A, yAxis: ResultListObject[T] => Double, series: String, data: List[ResultListObject[T]]) = this(List(new DataParser[A, T](
    data, ((x) => (xAxis(x), yAxis(x))), series
  )))

  private val dataSet = (dataParser.map(x => x.parse) foldLeft new CategoryDataSet[A]) (_ add _)

  def toCategorySet: DefaultCategoryDataset = {
    val set = new DefaultCategoryDataset()
    dataSet.addTo(set)
    set
  }

}

