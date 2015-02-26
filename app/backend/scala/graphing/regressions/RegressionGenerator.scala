/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package backend.scala.graphing.regressions

import java.awt.Color
import java.rmi.server.ExportException

import org.jfree.chart.annotations.XYLineAnnotation
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.function.LineFunction2D
import org.jfree.data.general.DatasetUtilities
import org.jfree.data.xy.{XYDataset, XYSeriesCollection, XYSeries}

/*
 * Created by Jackson Woodruff on 25/11/2014 
 * 
 */

object RegressionGenerator {
  val linear = "Linear"

  def fromString(s: String, key: String): Regression = s match {
    case `linear` => Linear(key)
    case _ => NoRegression
  }

  def toList = List(
    linear
  )
}

case class Linear(key: String) extends Regression {
  override def preformRegression(data: XYSeriesCollection, plot: XYPlot): Unit = {
    try {
      val seriesNumber = data.getSeriesIndex(key)
      val (c, m) = equation(data, seriesNumber)

      val line = new LineFunction2D(c, m)
      val lineSet = DatasetUtilities.sampleFunction2D(
        line, 0, data.getSeries(seriesNumber).getMaxX,
        2, equationString(c, m)
      )
      plot.setDataset(1, lineSet)

      val renderer = new XYLineAndShapeRenderer(
        true, false)
      renderer.setSeriesPaint(0, Color.BLACK)
      plot.setRenderer(1, renderer);
    } catch {
      case _ => // There was probably not enough data
        println("Not enough data to preform regression")
    }
  }

  def equationString(c: Double, m: Double) = "y = " + round(m) +
    "x " + " + " + round(c)
}

case object NoRegression extends Regression {
  /*
   * This preforms the regression and adds the line to the collection.
   *
   * However, in this case, we don't actually want to do anything.
   */
  override def preformRegression(data: XYSeriesCollection, plot: XYPlot): Unit = {}
}