package backend.scala.graphing

import backend.scala.datatypes.DataType
import backend.scala.graphing.data.{XYData, CategoryDataSet, DataParser}
import backend.scala.query.ResultListObject
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.data.general.DefaultKeyedValues2DDataset
import org.jfree.data.xy.{XYDataItem, XYDataset, XYSeries, XYSeriesCollection}

/*
 * Created by Jackson Woodruff on 26/07/2014 
 *
 * This class stores data for multiple series on a single
 * bar chart.
 *
 *
 */

class BarChartData[A <: Comparable[_], T <: DataType[T]](dataParser: List[DataParser[A, T]]) extends ChartData {
  private val dataSet = (dataParser.map(x => x.parse) foldLeft new CategoryDataSet[A]) (_ add _)

  def toCategorySet: DefaultCategoryDataset = {
    val set = new DefaultCategoryDataset()
    dataSet.addTo(set)
    set
  }

  def toXYSeriesCollection: XYSeriesCollection = {
    val set = new XYSeriesCollection()
    val parsedList = dataParser map (x => x.parseXY)
    val filteredList = autoFilter(parsedList)
    val xySeries = filteredList map (parsed => {
      val singleSeries = new XYSeries(parsed.series)
      for((x, y) <- parsed.xData zip parsed.yData) {
        singleSeries.add(x, y)
      }
      singleSeries
    })
    for (series <- xySeries) {
      set.addSeries(series)
    }
    set
  }

  def autoFilter(xyData: List[XYData[Double, Double]]) = {
    xyData filter (_.xData.nonEmpty)
  }

}

