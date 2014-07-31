package backend.java;

/*
 * Created by Jackson Woodruff on 26/07/2014 
 *
 * A graphing class that will save a bar chart
 * as a png to a pre-specified location
 */

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarChart extends Graph {
	public BarChart(DefaultCategoryDataset dataset, String title, String xAxisTitle, String yAxisTitle) {
		chart = createChart(dataset, title, xAxisTitle, yAxisTitle);
	}


    public JFreeChart createChart(DefaultCategoryDataset dataset, String title, String xAxisTitle, String yAxisTitle) {
        CategoryPlot categoryPlot = toCategoryPlot(dataset);
        JFreeChart chart = new JFreeChart("Bar Chart", categoryPlot);
        chart.setTitle(title);
        return chart;
    }
}
