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

/*
 * Created by Jackson Woodruff on 26/11/2014 
 *
 * This is just for the standardisation of
 * the option wrappers.
 *
 * At the time of creation at least, I was
 * not intending to use this for type convenience.
 */

abstract class OptionWrapper[T, B <: OptionWrapper[T, B]] (val numberOfObservations: Int) {
  def get: T

  def mergeSum(other: B): B

  def mergeEqual(other: B): B

  def average: B
}
