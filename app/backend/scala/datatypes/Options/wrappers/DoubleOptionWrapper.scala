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

package backend.scala.datatypes.Options.wrappers

import backend.scala.datatypes.options.{NoDouble, SomeDouble, DoubleOption}

/*
 * Created by Jackson Woodruff on 26/11/2014 
 *
 * This is a class that holds doubles in a wrapper.
 *
 * It was introduced in order to allow aggregation by
 * different values depending on the number actual
 * not None values there are in the dataset.
 *
 * This is the only Wrapper that I need right now
 * because the number of useful observations doesn't
 * matter for the mergeEqual types
 */

class DoubleOptionWrapper(option: DoubleOption, numberOfObservations: Int) extends OptionWrapper[DoubleOption, DoubleOptionWrapper](numberOfObservations) {
  def this(option: DoubleOption) = this(option, 1)

  override def get: DoubleOption = option

  override def average: DoubleOptionWrapper =
    new DoubleOptionWrapper(option / SomeDouble(numberOfObservations), numberOfObservations)

  override def mergeSum(other: DoubleOptionWrapper): DoubleOptionWrapper = (this.get, other.get) match {
    case (NoDouble, SomeDouble(x)) => other
    case (SomeDouble(x), NoDouble) => this
    case (_, _) => new DoubleOptionWrapper(this.get + other.get, this.numberOfObservations + other.numberOfObservations)
  }

  override def mergeEqual(other: DoubleOptionWrapper): DoubleOptionWrapper =
    new DoubleOptionWrapper(this.get mergeEqual other.get, this.numberOfObservations + other.numberOfObservations)
}
