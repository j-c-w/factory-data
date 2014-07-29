package backend.java;

/*
 * Created by Jackson Woodruff on 29/07/2014 
 * 
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineGraph extends Graph {
    public LineGraph(DefaultCategoryDataset dataset, String title, String xAxisTitle, String yAxisTitle) {
        chart = ChartFactory.createLineChart(title, xAxisTitle, yAxisTitle, dataset, PlotOrientation.VERTICAL, true, true, false);
    }
}
