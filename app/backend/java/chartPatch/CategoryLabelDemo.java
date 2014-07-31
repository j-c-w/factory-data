package backend.java.chartPatch;/* ------------------
 * CategoryLabelDemo.java
 * ------------------
 *
 */

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardCategorySeriesLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;

public class CategoryLabelDemo extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public CategoryLabelDemo(String title) {
        super(title);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(14.0, "Sales", "Jan 2006");
        dataset.addValue(10.0, "Sales", "Feb 2006");
        dataset.addValue(10.0, "Sales", "Mar 2006");
        dataset.addValue(7.0,  "Sales", "Apr 2006");
        dataset.addValue(6.0,  "Sales", "May 2006");
        dataset.addValue(8.0,  "Sales", "Jun 2006");
        dataset.addValue(4.0,  "Sales", "Jul 2006");
        dataset.addValue(4.0,  "Sales", "Aug 2006");
        dataset.addValue(10.0, "Sales", "Sep 2006");
        dataset.addValue(11.0, "Sales", "Oct 2006");
        dataset.addValue(12.0, "Sales", "Nov 2006");
        dataset.addValue(22.0, "Sales", "Dec 2006");
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        
        setContentPane(chartPanel);

    }

    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(CategoryDataset dataset) {
        
    	JFreeChart chart = null;
    	try {
    		BarRenderer barRenderer = new BarRenderer();
	        CategoryAxis customCategoryAxis = new CategoryAxisSkipLabels();  // new CategoryAxis("Category");
	        ((CategoryAxisSkipLabels)customCategoryAxis).setTickMarksVisible(true);
	        ((CategoryAxisSkipLabels)customCategoryAxis).setDisplaySkippedTickMarks(true);
	        // ****************** set Layout Type and AlgorithmType
//	        ((CategoryAxisSkipLabels)customCategoryAxis).setLayoutType(CategoryAxisSkipLabels.STAGGER_LAYOUT);
//	        ((CategoryAxisSkipLabels)customCategoryAxis).setLayoutType(CategoryAxisSkipLabels.STANDARD_LAYOUT);	     
//	        ((CategoryAxisSkipLabels)customCategoryAxis).setAlgorithmType(CategoryAxisSkipLabels.BISECT_RECURSE_ALGO);
	        ((CategoryAxisSkipLabels)customCategoryAxis).setAlgorithmType(CategoryAxisSkipLabels.N_STEP_ALGO);
// this must be the last call to category axis as it may return CategoryAxis instead of CategoryAxisSkipLabels	        
//	        customCategoryAxis = ((CategoryAxisSkipLabels)customCategoryAxis).setTruncate(true);
	        NumberAxis numberAxis = new NumberAxis("Value");
	        CategoryPlot categoryPlot = new CategoryPlot(
	            dataset, customCategoryAxis, numberAxis, barRenderer
	        );
  	        chart = new JFreeChart("Bar Chart", categoryPlot);
    	} catch (Exception e) {
    		System.out.print("crash");
    	}
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);
        plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		LegendTitle legend = (LegendTitle) chart.getLegend();
		legend.setPosition(RectangleEdge.BOTTOM);
		legend.setHorizontalAlignment(HorizontalAlignment.LEFT);
		legend.setBorder(new BlockBorder(Color.white));
		legend.setBackgroundPaint(Color.white);
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setLegendItemToolTipGenerator(new StandardCategorySeriesLabelGenerator("Tooltip: {0}"));
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
        
    }
    
     
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

    	CategoryLabelDemo demo = new CategoryLabelDemo("Bar Chart Demo 2");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }
    public static void run(String title) throws Exception {
    	CategoryLabelDemo demo = new CategoryLabelDemo(title);
	    demo.pack();
	    RefineryUtilities.centerFrameOnScreen(demo);
	    demo.setVisible(true);    
    }

}
