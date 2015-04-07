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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class Graph {
    protected JFreeChart chart;

    /*
	 * Saves the picture in a randomly selected file and
	 * returns the file used
	 */
    public void saveAsPNG(File saveDir) {
        try {
            ChartUtilities.saveChartAsPNG(saveDir, chart, 900, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	/*
	 * This is to reduce the dependence of this app on the file system.
	 *
	 * Instead of writing the chart to the file system it returns a base 64 string
	 * of its contents.
	 */
	public String toBase64() {
		try {
			BufferedImage image = chart.createBufferedImage(900, 600);
			return imageTo64(image);
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
	}

	private static String imageTo64(BufferedImage image) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "png", out);
		byte[] bytes = out.toByteArray();
		return Base64.getEncoder().encodeToString(bytes);
	}

	public static String errorBase64() {
		try {
			BufferedImage image = ImageIO.read(Global.errorPictureLocation());
			return imageTo64(image);
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
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
        ((CategoryAxisSkipLabels)customCategoryAxis).setDisplaySkippedTickMarks(false);
        ((CategoryAxisSkipLabels)customCategoryAxis).setTruncate(true);
        //((CategoryAxisSkipLabels)customCategoryAxis).setTickLabelsVisible(false);
        ((CategoryAxisSkipLabels)customCategoryAxis).setAlgorithmType(CategoryAxisSkipLabels.N_STEP_ALGO);
        NumberAxis numberAxis = new NumberAxis("Value");
        return new CategoryPlot(
                dataset, customCategoryAxis, numberAxis, barRenderer
        );
    }
}
