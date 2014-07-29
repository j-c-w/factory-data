package backend.java;

/*
 * Created by Jackson Woodruff on 29/07/2014 
 * 
 */

import controllers.Global;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

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
}
