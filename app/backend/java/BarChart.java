package backend.java;

/*
 * Created by Jackson Woodruff on 26/07/2014 
 *
 * A graphing class that will save a bar chart
 * as a png to a pre-specified location
 */

import controllers.Global;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;

public class BarChart {
	private JFreeChart chart;

	public BarChart(DefaultCategoryDataset dataset, String title, String xAxisTitle, String yAxisTitle) {
		chart = ChartFactory.createBarChart(title, xAxisTitle, yAxisTitle, dataset, PlotOrientation.VERTICAL, true, true, false);
	}

	/*
	 * Saves the picture in a randomly selected file and
	 * returns the file used
	 */
	public File saveAsPNG() {
		File parentDir = Global.pictureFileLocation();
		File saveDir;
		//loop until the file doesn't already exist
		do {
			String name = new String(Global.nextNRandoms(40));
			saveDir = new File(parentDir + "/" + name + ".png");
		} while (saveDir.exists());
		try {
			ChartUtilities.saveChartAsPNG(saveDir, chart, 1000, 600);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return saveDir;
	}
}
