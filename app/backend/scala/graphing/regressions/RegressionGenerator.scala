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
import org.jfree.data.xy.{XYDataset, XYSeries}

/*
 * Created by Jackson Woodruff on 25/11/2014 
 * 
 */

object RegressionGenerator {
  def fromString(string: String) = string match {
    case Linear.toString => Linear
    case Polynomial.toString => Polynomial
    case Exponential.toString => Exponential
    case _ => NoRegression
  }

  def toList = List(
    Linear.toString, Polynomial.toString, Exponential.toString
  )
}

abstract class RegressionGenerator {
  protected def linear(set: XYDataset, seriesNumber: Int) =
    Regression.getOLSRegression(set, seriesNumber)

  //5 is the maximum power of the polynomial
  protected def polynomial(set: XYDataset, seriesNumber: Int) =
    Regression.getPolynomialRegression(set, seriesNumber, 5)

  protected def exponential(set: XYDataset, seriesNumber: Int) =
    Regression.getPowerRegression(set, seriesNumber)

  def getEquation(equation: Array[Double]): (Double => Double)

  def getEquationString: String

  def drawLine: XYSeries

}

object NoRegression extends RegressionGenerator {
  override val toString = "No Line of Best Fit"
  def getEquation(set: Array[Double]) = ???
  def getEquationString = ???
  def drawLine = ???
}

object Linear extends RegressionGenerator {
  override val toString = "Linear"
  def getEquation(set: Array[Double]) = ???
  def getEquationString = ???
  def drawLine = ???
}

object Polynomial extends RegressionGenerator {
  override val toString = "Polynomial"
  def getEquation(set: Array[Double]) = ???
  def getEquationString = ???
  def drawLine = ???
}

object Exponential extends RegressionGenerator {
  override val toString = "Exponential"
  def getEquation(set: Array[Double]) = ???
  def getEquationString = ???
  def drawLine = ???
}