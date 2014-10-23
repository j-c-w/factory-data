package backend.java;

/*
 * Created by Jackson Woodruff on 29/07/2014 
 * 
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineGraph extends Graph {
    public LineGraph(DefaultCategoryDataset dataset, String title, String xAxisTitle, String yAxisTitle) {
        chart = createChart(dataset, title, xAxisTitle, yAxisTitle);
    }

    public JFreeChart createChart(DefaultCategoryDataset dataset, String title, String xAxisTitle, String yAxisTitle) {
        CategoryPlot categoryPlot = toCategoryPlot(dataset);
        //this sets the data (and only the data) to the chart.
        // in this line, categoryPlot.getDataset could well be replaced by
        //just dataset (the parameter)
        JFreeChart chart = ChartFactory.createLineChart(title, xAxisTitle, yAxisTitle, categoryPlot.getDataset(), PlotOrientation.VERTICAL, true, false, false);
        //this line sets the x axis to the exact x axis from the
        //category plot (which fits the values to size)
        chart.getCategoryPlot().setDomainAxis(categoryPlot.getDomainAxis());
        chart.setTitle(title);
        return chart;
    }


}
