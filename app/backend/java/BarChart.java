package backend.java;

/*
 * Created by Jackson Woodruff on 26/07/2014 
 *
 * A graphing class that will save a bar chart
 * as a png to a pre-specified location
 */

import backend.scala.graphing.regressions.RegressionGenerator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;

public class BarChart extends Graph {
	public BarChart(DefaultCategoryDataset dataset, String title, String xAxisTitle, String yAxisTitle) {
		chart = createChart(dataset, title, xAxisTitle, yAxisTitle);
	}


    public JFreeChart createChart(DefaultCategoryDataset dataset, String title, String xAxisTitle, String yAxisTitle) {
        CategoryPlot categoryPlot = toCategoryPlot(dataset);
		//todo -- implement the regression so it actually does stuff
        JFreeChart chart = ChartFactory.createBarChart(title, xAxisTitle, yAxisTitle, categoryPlot.getDataset(), PlotOrientation.VERTICAL, true, false, false);
		//this line sets the x axis to the exact x axis from the
		//category plot (which fits the values to size)
		chart.getCategoryPlot().setDomainAxis(categoryPlot.getDomainAxis());
	    chart.getCategoryPlot().getDomainAxis().setLabel(xAxisTitle);
        chart.setTitle(title);
        return chart;
    }
}
