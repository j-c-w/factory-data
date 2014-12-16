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

import org.jfree.data.statistics.Regression
import org.jfree.data.xy.{XYSeriesCollection, XYSeries}

/*
 * Created by Jackson Woodruff on 25/11/2014 
 * 
 */

object RegressionGenerator {
  val linear = "Linear"
  val polynomial = "Polynomial"
  val exponential = "Exponential"

  def fromString(string: String, power: Int) = string match {
    case linear => new RegressionGenerator(1)
    case polynomial => new RegressionGenerator(power)
    case exponential => new RegressionGenerator(-1)
  }

  def toList = List(
    linear, polynomial, exponential
  )
}

class RegressionGenerator(power: Int) {
  protected def linear(set: XYSeriesCollection, seriesNumber: Int) =
    Regression.getOLSRegression(set, seriesNumber)

  //5 is the maximum power of the polynomial
  protected def polynomial(set: XYSeriesCollection, seriesNumber: Int, power: Int) =
    Regression.getPolynomialRegression(set, seriesNumber, power)

  protected def exponential(set: XYSeriesCollection, seriesNumber: Int) =
    Regression.getPowerRegression(set, seriesNumber)

  /*
   * This returns an anonymous function that
   * parses doubles (and so can be plotted).
   *
   * For an exponential equation pass -1 as the power.
   */
  def getEquation(set: XYSeriesCollection, seriesNumber: Int): (Double => Double) =
    if (power < 0) {
      val doubles = exponential(set, seriesNumber)
      x => doubles(0) * Math.pow(x, doubles(1))
    } else {
      val doubles = linear(set, seriesNumber)
      val equationList: IndexedSeq[(Double => Double)] = ((0 until doubles.length).zip(doubles)).map {
        case(coefficient, pwr) => ((x: Double) => coefficient * Math.pow(x, pwr))
      }
      x => equationList.foldRight(0.0) (_.apply(x) + _)
    }

  /*
   * This returns a string representation of the
   * regression equation.
   *
   * I find it infuriating that I cannot reuse the code
   * above despite the similarity between the getEquation
   * and getEquationString methods
   */
  def getEquationString(set: XYSeriesCollection, seriesNumber: Int): String =
    if (power < 0) {
      val doubles = exponential(set, seriesNumber)
      "y = " + doubles(0).toString + "x^(" + doubles(1).toString + ")"
    } else {
      val doubles = linear(set, seriesNumber)
      val equationList: IndexedSeq[String] = ((0 until doubles.length).zip(doubles)).map {
        case(pwr, coefficient) => coefficient + "x^(" + pwr.toString + ")"
      }
      equationList.foldRight("y = ") ((str, equ) => str + " + " + equ)
    }

  def addEquationToSet(set: XYSeriesCollection, seriesNumber: Int) = {
    val series = new XYSeries(getEquationString(set, seriesNumber))
    val minX = set.getSeries(seriesNumber).getMinX
    val maxX = set.getSeries(seriesNumber).getMaxX

    val equation = getEquation(set, seriesNumber)
    val minY = equation.apply(minX + 0.0000001)
    val maxY = equation.apply(maxX -0.00001)

    println("Minimum: " + minX.toString + ", " + minY.toString)
    println("Maximum: " + maxX.toString + ", " + maxY.toString)

    series.add(minX, minY)
    series.add(maxX, maxY)

    set.addSeries(series)
  }
}