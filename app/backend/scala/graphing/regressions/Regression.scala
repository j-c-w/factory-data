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
import java.text.DecimalFormat

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
   * This is a utility rounding function. It is stored in here to ensure
   * that the rounding on the numbers for the regressions is consistent
   * and easy to change
   */
  protected def round(n: Number): String = {
    val formatter = new DecimalFormat("#.####")
    formatter.format(n)
  }

  /*
   * This is another utility function that yeilds the appropriate color
   * for repeated regressions
   */
  protected def getLineColor(seriesNumber: Int) = seriesNumber % 7 match {
    case 0 => Color.GREEN
    case 1 => Color.BLACK // This is the first color that will be used.
    case 2 => Color.BLUE
    case 3 => Color.RED
    case 4 => Color.CYAN
    case 5 => Color.MAGENTA
    case 6 => Color.WHITE
  }

  /*
   * This preforms the regression and adds the line to the collection
   */
  def preformRegression(data: XYSeriesCollection, plot: XYPlot): Unit
}