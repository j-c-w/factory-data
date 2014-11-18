package controllers

import backend.scala.datatypes.DataField

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

  val defaultFields: List[String] = List(
    "Factory Code", "Line Code", "Total Operators", "Operators Absent"
  )


  val noSelection: String = "--- None ---"

  /*
   * From this point onwards, all variables are
   * used in layouts rather than in forms
   */

  //this should be a variable full of the numbers that are used in the
  //column headers. This is for plotting the y-axis of graphs on which
  //only numbers can be plotted

  val numberHeaders: List[String] = noSelection :: List(
    "Total Operators", "Operators Present", "Operators Absent", "Operators on Leave",
    "Percent Operators Absent", "Total Helpers", "Helpers Present",
    "Helpers Absent", "Helpers on Leave", "Percent Helpers Absent"
  )

  val comparisonMethods: List[String] = List(
    noSelection, "==", "!=", "<=", ">=", ">", "<"
  )

  val graphSortOptions: List[String] = List(
    "xAxis Ascending", "xAxis Descending", "No Sort", "yAxis Ascending", "yAxis Descending"
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

  val graphTypes: List[String] = List(
    "Bar Chart", "Line Graph"
  )
}
