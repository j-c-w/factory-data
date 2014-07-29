package backend.java;

/*
 * Created by Jackson Woodruff on 26/07/2014 
 *
 * A graphing class that will save a bar chart
 * as a png to a pre-specified location
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChart extends Graph {
	public BarChart(DefaultCategoryDataset dataset, String title, String xAxisTitle, String yAxisTitle) {
		chart = ChartFactory.createBarChart(title, xAxisTitle, yAxisTitle, dataset, PlotOrientation.VERTICAL, true, true, false);
	}
}
