/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package backend.java;

import backend.scala.graphing.regressions.Regression;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Created by Jackson on 1/15/2015.
 *
 * This is like the other types of graph, but it puts the regressions
 * to use.
 */
public class ScatterPlot extends Graph {
	public ScatterPlot(XYSeriesCollection dataset, String title, String xAxisTitle, String yAxisTitle, Regression[] regression) {
		chart = getChart(dataset, title, xAxisTitle, yAxisTitle, regression);
	}

	private JFreeChart getChart(XYSeriesCollection dataset, String title, String xAxisTitle, String yAxisTitle, Regression[] regressions) {
		JFreeChart chart = ChartFactory.createScatterPlot(title, xAxisTitle, yAxisTitle, dataset, PlotOrientation.VERTICAL, true, false, false);
		for (Regression regression : regressions) {
			regression.preformRegression(dataset, chart.getXYPlot());
		}

		return chart;
	}
}
