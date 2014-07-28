package backend.scala.graphing.data

/*
 * Created by Jackson Woodruff on 26/07/2014 
 *
 * A holder for x y data that relate to one another.
 * xData(n) is the x point for yData(n)
 */

class XYData[A, B](val xData: List[A], val yData: List[B], val series: String)
