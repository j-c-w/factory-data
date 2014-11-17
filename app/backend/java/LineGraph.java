package backend.java;

/*
 * Created by Jackson Woodruff on 29/07/2014 
 * 
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

public class LineGraph extends Graph {
    public LineGraph(XYSeriesCollection dataset, String title, String xAxisTitle, String yAxisTitle) {
        chart = createChart(dataset, title, xAxisTitle, yAxisTitle);
    }

    public JFreeChart createChart(XYSeriesCollection collection, String title, String xAxisTitle, String yAxisTitle) {
        JFreeChart chart = ChartFactory.createXYLineChart(title, xAxisTitle, yAxisTitle, collection, PlotOrientation.VERTICAL, true, false, false);
        chart.setTitle(title);
        return chart;
    }


}
