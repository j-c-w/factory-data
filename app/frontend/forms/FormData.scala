package frontend.forms

/*
 * Created by Jackson Woodruff on 30/09/2014
 *
 * A simple trait to enable the passing
 * of any of these to a single parser method,
 * enabling me to store these in a single list
 */

trait FormData[T <: FormData[T]] {
  def default: T

  /*
   * This method should return a list of all the
   * elements in the form
   */
  def toList: List[String]
}
