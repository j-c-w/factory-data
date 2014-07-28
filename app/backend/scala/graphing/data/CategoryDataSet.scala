package backend.scala.graphing.data

import org.jfree.data.category.DefaultCategoryDataset

/*
 * Created by Jackson Woodruff on 26/07/2014 
 *
 * Holds many XY data and can add them to the
 * datasets that are used in bar charts only.
 *
 * Might be extendible to pie charts in the
 * future
 *
 * Each XYData stored is considered to be one
 * series on the resulting data
 */

class CategoryDataSet[B <: Comparable[_]] (data: List[XYData[B, Double]]){
  def this() = this(Nil)

  def addTo(chartData: DefaultCategoryDataset) =
    for (xyData <- data) {
      (xyData.xData zip xyData.yData) map {
        case (x, y) => chartData.addValue(y, xyData.series, x)
      }
    }

  def add(newData: XYData[B, Double]) =
    new CategoryDataSet(newData :: data)
}
