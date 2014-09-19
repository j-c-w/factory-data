package backend.scala.datatypes.experimental

import backend.scala.datatypes.DataType

/*
 * Created by Jackson Woodruff on 19/09/2014 
 *
 *
 * This class contains a list of cell data items
 * which are passed as parameters.
 *
 * This is effectively one row in a database.
 *
 * To make this conform the already implemented
 * LineListObject model, this class also contains
 * a list of the column headers.
 */

class RowData(val params: CellData*) extends DataType[RowData]{

  def mergeSum(other: RowData) = {
    val newParams = (this.params zip other.params) map {
      case (thisData, otherData) => (thisData mergeSum otherData)
    }
    new RowData(newParams : _*)
  }

  def averageBy(number: Int) = {
    val newParams = params map {
      x => x.averageBy(number)
    }
    new RowData(newParams : _*)
  }

  def toBuilder = ???

  def toHtml =

  def get = this
}
