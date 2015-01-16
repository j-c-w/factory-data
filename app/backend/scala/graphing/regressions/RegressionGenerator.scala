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

import java.rmi.server.ExportException

import org.jfree.chart.annotations.XYLineAnnotation
import org.jfree.chart.plot.XYPlot
import org.jfree.data.xy.{XYSeriesCollection, XYSeries}

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

      val min = data.getSeries(seriesNumber).getMinX
      val max = data.getSeries(seriesNumber).getMaxX

      def f(x: Double) =
        m * x + c

      val annotation = new XYLineAnnotation(min, f(min), max, f(max))
      plot.addAnnotation(annotation)
    } catch {
      case _ => // There was probably not enough data
        println("Not enough data to preform regression")
    }
  }

  def equationString(c: Double, m: Double) = "y = " + m + "x " + " + " + c

}

case object NoRegression extends Regression {
  /*
   * This preforms the regression and adds the line to the collection.
   *
   * However, in this case, we don't actually want to do anything.
   */
  override def preformRegression(data: XYSeriesCollection, plot: XYPlot): Unit = {}
}