package backend.scala.graphing

/*
 * Created by Jackson Woodruff on 26/07/2014 
 *
 * A holder class for xAxis an yAxis values
 */

class GraphDataSet[A, B](private val xAxis: List[List[A]], private val yAxis: List[List[B]]) {
  def add(x: List[A], y: List[B]) = new GraphDataSet(
    x :: xAxis, y :: yAxis
  )

  def add(other: GraphDataSet[A, B]) = new GraphDataSet(
    other.xAxis ++ xAxis, other.yAxis ++ yAxis
  )

  //def to
}
