package backend.java;

/*
 * Created by Jackson Woodruff on 29/07/2014 
 * 
 */

import backend.scala.graphing.regressions.RegressionGenerator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

public class LineGraph extends Graph {
    public LineGraph(XYSeriesCollection dataset, RegressionGenerator regression, String title, String xAxisTitle, String yAxisTitle) {
        chart = createChart(dataset, regression, title, xAxisTitle, yAxisTitle);
    }

    public JFreeChart createChart(XYSeriesCollection collection, RegressionGenerator regression, String title, String xAxisTitle, String yAxisTitle) {
		collection.addSeries(regression.drawLine());
        JFreeChart chart = ChartFactory.createXYLineChart(title, xAxisTitle, yAxisTitle, collection, PlotOrientation.VERTICAL, true, false, false);
        return chart;
    }


}
