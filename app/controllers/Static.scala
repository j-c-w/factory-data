package controllers

/*
 * Created by Jackson Woodruff on 13/11/2014 
 *
 * This is basically a lists class that is used to
 * store the lists of variable names etc. that are
 * used in the forms on the frontend.
 */

object Static {

  val tableHeaders: List[String] = List(
    "Number of Observations", "Factory Code", "Line Code", "Date", "Total Operators",
    "Operators Present", "Operators Absent", "Operators on Leave",
    "Percent Operators Absent", "Total Helpers", "Helpers Present",
    "Helpers Absent", "Helpers on Leave", "Percent Helpers Absent"
  )

  //this should be a variable full of the numbers that are used in the
  //column headers. This is for plotting the y-axis of graphs on which
  //only numbers can be plotted
  val numberHeaders: List[String] = List(
    ""
  )

  val noSelection: String = "--- None ---"

  /*
   * From this point onwards, all variables are
   * used in layouts rather than in forms
   */
  val comparisonMethods: List[String] = List(
    noSelection, "==", "!=", "<=", ">=", ">", "<"
  )

  val fieldNames: List[String] = noSelection :: tableHeaders

  val aggregateModes: List[String] = noSelection :: List(
    "Average", "Sum"
  )

  val sortModes: List[String] = noSelection :: List(
    "Ascending", "Descending"
  )

  val logicalConnectors: List[String] = List(
    "And", "Or"
  )
}
