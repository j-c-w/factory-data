package backend.java;

/*
 * Created by Jackson Woodruff on 29/07/2014 
 * 
 */

import backend.java.chartPatch.CategoryAxisSkipLabels;
import controllers.Global;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;

public class Graph {
    protected JFreeChart chart;

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

    /*
     * takes a default category dataset and converts it into a
     * category plot in such a way that the uneeded x-axis values
     * are skipped
     */

    public CategoryPlot toCategoryPlot(DefaultCategoryDataset dataset) {
        BarRenderer barRenderer = new BarRenderer();
        CategoryAxis customCategoryAxis = new CategoryAxisSkipLabels();
        ((CategoryAxisSkipLabels)customCategoryAxis).setTickMarksVisible(true);
        ((CategoryAxisSkipLabels)customCategoryAxis).setDisplaySkippedTickMarks(true);
        ((CategoryAxisSkipLabels)customCategoryAxis).setAlgorithmType(CategoryAxisSkipLabels.N_STEP_ALGO);
        NumberAxis numberAxis = new NumberAxis("Value");
        return new CategoryPlot(
                dataset, customCategoryAxis, numberAxis, barRenderer
        );
    }
}
