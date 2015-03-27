package controllers

import backend.scala.datatypes._
import backend.scala.graphing.regressions.RegressionGenerator
import frontend.ComparisonMethod

/*
 * Created by Jackson Woodruff on 13/11/2014 
 *
 * This is basically a lists class that is used to
 * store the lists of variable names etc. that are
 * used in the forms on the frontend.
 */

object Static {

  val tableHeaders: List[String] = DataField.asList

  val defaultFields: List[String] = List()


  val noSelection: String = "--- None ---"

  /*
   * From this point onwards, all variables are
   * used in layouts rather than in forms
   */

  //this should be a variable full of the numbers that are used in the
  //column headers. This is for plotting the y-axis of graphs on which
  //only numbers can be plotted

  val numberHeaders: List[String] = noSelection :: DataField.asDoublesList

  val comparisonMethods: List[String] =
    ComparisonMethod.asList

  val graphSortOptions: List[String] = List(
    "xAxis Ascending", "xAxis Descending", "No Sort", "yAxis Ascending", "yAxis Descending"
  )

  val fieldNames: List[String] = noSelection :: tableHeaders

  val aggregateModes: List[String] = List(
    "Average", "Sum"
  )

  val sortModes: List[String] = List(
    "Ascending", "Descending"
  )

  val logicalConnectors: List[String] = List(
    "And", "Or"
  )

  val graphTypes: List[String] = List(
    "Bar Chart", "Line Graph", "Scatter Plot"
  )

  val regressions: List[String] = noSelection :: RegressionGenerator.toList

  object Assets {
    val filterHelp = ("Filter", "A description of help")
    val aggregateHelp = ("Aggregate", "Help")
    val sortHelp = ("Sort", "help")
    val graphHelp = ("Graph", "help")
    val columnHelp = ("Column", "help")

    val helpList = List(
      filterHelp, aggregateHelp, sortHelp, graphHelp, columnHelp
    )
  }
}
