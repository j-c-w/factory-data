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

import backend.scala.graphing.data.XYDataSet
import org.jfree.chart.plot.XYPlot
import org.jfree.data.xy.XYSeriesCollection

/**
 * Created by Jackson on 1/15/2015.
 *
 * This is just a generic type for the regressions so
 * they can  passed just like the other types.
 */
trait Regression {
  protected def equation(data: XYSeriesCollection, seriesNumber: Int): (Double, Double) = {
    org.jfree.data.statistics.Regression.getOLSRegression(data, seriesNumber).toList match {
      case (m :: c :: _) => (m, c)
      case _ => (0, 0)
    }
  }

  /*
   * This preforms the regression and adds the line to the collection
   */
  def preformRegression(data: XYSeriesCollection, plot: XYPlot): Unit
}