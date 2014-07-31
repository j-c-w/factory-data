package backend.java.chartPatch;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.Effect3D;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.ui.RectangleAnchor;
import org.jfree.chart.axis.AxisState;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

import java.awt.geom.Line2D;
import java.awt.Shape;
import org.jfree.ui.RectangleEdge;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.axis.CategoryTick;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.Tick;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.TickLabelEntity;
import org.jfree.data.category.CategoryDataset;
import org.jfree.text.TextBlock;
import java.util.List;
import java.util.Iterator;
import java.util.Vector;
/*
 	Overview:
 	This class skips category axis labels. Essentially this class extends 
 	the existing Category Axis class in JFreeChart and overrides several 
 	of its methods (createLabel(), refreshTicks(), drawCategoryLabels()). 
 	Occasionally I refractored the original code form 
 	JFreeChart::CategoryAxis() into separate methods to make this fit 
 	together better and simplify testing. 

	The central �skipping� algorithm is implemented in the removeOverlays() 
	methods. CategoryAxisSkipLabels has two layout types, Standard_Layout and 
	Stagger_Layout. These layout types are exposed to the caller of 
	CategoryAxisSkipLabels through two publicly defined tags, STAGGER_LAYOUT 
	and STANDARD_LAYOUT and are selected through the setLayoutType method. 
	After the layout processing is completed the label skipping is started. 

	Currently CategoryAxisSkipLabels has two mechanisms for skipping labels,
	selected by the setAlgorithmType method. Essentially one mechanism uses 
	a Best-Fit algorithm while the other uses an N-Step approach. The main 
	removeOverlays method, which accepts LabelDefs and the algorithmType, 
	staggers labels, if requested, and then dispatches to one of the skip 
	label mechanisms, which I describe below.

	The Best Fit Mechanism:
	This method accepts a start and end position and attempts to draw the mid 
	point label. This is recursive routine. If the label does not collide with 
	its neighbors it is marked from display. In either case (display or not) 
	the current region is split and the removeOverlays() method is invoked 
	with a new region. This is a recursive mechanism, which seems to display 
	labels in an efficient and balanced way. This mechanism seems to work pretty 
	well especially for variable length labels where an N-Step seems to fall 
	apart. This solution will draw as many labels as possible in the most 
	balanced way possible but may not skip �evenly�. 

	The N-Step Mechanism:
	Unlike previous mechanisms for N-Step this is not a draw as you go system, 
	in which you have to pass in the step factor. CategoryAxisSkipLabels 
	calculates the label positions, which labels are drawn, and most importantly
	the N-Step factor for you. This is an improvement over other mechanisms. 
	This mechanism will skip evenly, but may not draw all the labels that could 
	have been drawn, especially when dealing with variable length labels. 

	Running with Default Behavior:
	To maintain compatibility with the standard category axis a switch has 
	been added to disable the methods, which CategoryAxisSkipLabels overrides. 
	This switch is set using the setTruncate() method. This method returns a 
	simple Category Axis which ignores the settings that where previously 
	selected in CategoryAxisSkipLabels.

	Companion Classes:
	A set of companion classes exists for CategoryAxisSkipLabels. The most 
	interesting is called LabelDefs. This class is constructed by the 
	gatherDrawingGeometries() when the overridden drawCategoryLabels method is 
	called. The drawCategoryLabels method is called whenever a redraw is 
	triggered. The purpose of the LabelDefs class is to hold all the required 
	data (CategoryTick , bounds , and anchorPoint) for drawing the labels. This 
	data is held in a Vector of LabelDef objects. Basically the removeOverlays()
	method uses this information to mark which labels should be drawn and which
	should be skipped. As a side note, LabelDef information is also used to 
	stagger labels. This draw/nodraw status is marked with a Boolean flag in 
	each LabelDef, which drawLabels checks. Each redraw regenerates a new 
	LabelDefs list.
	
	
	Thanks for reading this. If you have any questions feel free to contact me 
	at rlukas@pilotsoftware.com, ray_lukas@comcast.net  Thanks..

 */
public class CategoryAxisSkipLabels extends CategoryAxis  { 
	public static final int BISECT_RECURSE_ALGO = 10;
	public static final int N_STEP_ALGO = 11;
	public static final int STAGGER_LAYOUT = 20;
	public static final int STANDARD_LAYOUT = 21;
	
	private boolean displaySkippedTickMarks = false;
	//private boolean truncate = false; // ONLY USE SUPER FUNCTIONS !!
	private int layoutType = STANDARD_LAYOUT;
	private int algorithmType = N_STEP_ALGO;
	
  public CategoryAxis setTruncate(boolean truncate){
	  CategoryAxis categoryAxis = null;
	  if (truncate) {
		  categoryAxis = new CategoryAxis();
	  } else {
		  categoryAxis = this;
	  }
	  return categoryAxis;
  }
  //
  // 	The normal channels for calling this are to trucate the test string. 
  //	We need to display the full text string. So we do not want this 
  //	truncating to happen.. refer to TextUtilities.createTextBlock()
  //	called by CategoryAxis.createLabel()
  //	
  protected TextBlock createLabel(Comparable category,
          RectangleEdge edge, Graphics2D g2) {
	  TextBlock label = createTextBlock(category.toString(), getTickLabelFont(category), 
            getTickLabelPaint(category));
	  return label;
  }
  public void setDisplaySkippedTickMarks(boolean displaySkippedTickMarks){
	  this.displaySkippedTickMarks  = displaySkippedTickMarks;
  }
  public void setLayoutType(int layoutType)throws Exception{
  	if ((layoutType==CategoryAxisSkipLabels.STANDARD_LAYOUT) || (layoutType==CategoryAxisSkipLabels.STAGGER_LAYOUT)) {
  		this.layoutType = layoutType;
  	} else {
  		notifyInvalidLayoutType("[CategoryAxisSkipLabels::setLayoutType]", layoutType);
  	}
  }
  public void setAlgorithmType(int algorithmType){
	  this.algorithmType = algorithmType;
  }
  //	we are also changing the way refresh ticks works so that we 
  //	are now calling our 
  public List refreshTicks(Graphics2D g2, 
          AxisState state,
          Rectangle2D dataArea,
          RectangleEdge edge) {

	List ticks = new java.util.ArrayList();
	
	// sanity check for data area...
	if (dataArea.getHeight() <= 0.0 || dataArea.getWidth() < 0.0) {
		return ticks;
	}
	
	CategoryPlot plot = (CategoryPlot) getPlot();
	List categories = plot.getCategories();
	double max = 0.0;
	
	if (categories != null) {
		CategoryLabelPosition position = super.getCategoryLabelPositions().getLabelPosition(edge);

		int categoryIndex = 0;
		Iterator iterator = categories.iterator();
		while (iterator.hasNext()) {
			Comparable category = (Comparable) iterator.next();
			
			TextBlock label = createLabel(category, edge, g2);
			if (edge == RectangleEdge.TOP || edge == RectangleEdge.BOTTOM) {
			 max = Math.max(max, 
			         calculateTextBlockHeight(label, position, g2));
			}
			else if (edge == RectangleEdge.LEFT 
			     || edge == RectangleEdge.RIGHT) {
			 max = Math.max(max, 
			         calculateTextBlockWidth(label, position, g2));
			}
			Tick tick = new CategoryTick(category, label, 
			     position.getLabelAnchor(), position.getRotationAnchor(), 
			     position.getAngle());
			ticks.add(tick);
			categoryIndex = categoryIndex + 1;
		}
	}
	state.setMax(max);
	return ticks;
	
  }

  

  //	------------------------------------------------------- BEST FIT 
  // get centerTickID from start and ened
  // get cetner bound from all bound called current bound
  // does current bound conflict if so return drawnBound else 
  // add curerntbound to derawnbound
  // recurse(startTickId, centerTickId)
  // recurse(centerTickId, endTickId)
  // return drawnBunds
  private LabelDefs removeOverlays(LabelDefs labelDefs, int startTickID, int endTickID){
	  //System.out.println("removeOverlays(labelDefs, " + startTickID + ", " + endTickID + ")");
	  int candidatePosition = ((endTickID - startTickID)/2) + startTickID;
	  //System.out.println("evaluating candidatePosition [" + candidatePosition + "]");
	  Shape candidateShape = labelDefs.getCandidateShape(candidatePosition);
	  if (labelDefs.intersectsAny(candidateShape)) {
		  //System.out.println("intersectAny returned true, break out");
		  return labelDefs;
	  } else {
		  //System.out.println("mark candidatePosition [" + candidatePosition + "] as drawable");
		  labelDefs.markForDraw(candidatePosition);
	  }
	  labelDefs = removeOverlays(labelDefs, startTickID, candidatePosition);	
	  labelDefs = removeOverlays(labelDefs, candidatePosition, endTickID);
	  return labelDefs;
  }
  
  //	------------------------------------------------------- N STEP 
  private LabelDefs removeOverlays(LabelDefs labelDefs){
  	int stepCount = 0;

  	boolean solutionFound = false;
  	while ((++stepCount<labelDefs.size()) && (!solutionFound)){
	  	boolean intersectFound = false;
	  	for (int candidatePosition=0; (candidatePosition<labelDefs.size() && !intersectFound); candidatePosition++) {
	  		if ((candidatePosition % stepCount) == 0) {
	  			Shape candidateShape = labelDefs.getCandidateShape(candidatePosition);
	  		  if (labelDefs.intersectsAny(candidateShape)) {
	  		  	intersectFound = true;
	  		  	labelDefs.unMarkAllForDraw();
	  		  } else {
	  		  	labelDefs.markForDraw(candidatePosition);
	  		  }
	  		}
	  	}
	  	if (!intersectFound){
	  		solutionFound = true;
	  	}
  	}
  	return labelDefs;
  }
  
  private LabelDefs removeOverlays(LabelDefs labelDefs, int algorithmType)throws Exception {
	  LabelDefs processedLabelDefs = null;
	  if (algorithmType == CategoryAxisSkipLabels.BISECT_RECURSE_ALGO){		 
		  processedLabelDefs = removeOverlays(labelDefs, 0, labelDefs.size());
	  } else if (algorithmType == CategoryAxisSkipLabels.N_STEP_ALGO) {
		  processedLabelDefs = removeOverlays(labelDefs);
	  }
	  else {
		  notifyInvalidAlgorithRequest("[CategoryAxisSkipLabels::removeOverlays]", labelDefs, algorithmType);
	  }
	  return processedLabelDefs;
  }
  
  
  /** 
   * Draws the category labels and returns the updated axis state. 
   * NOTE: This method redefines the corresponding one in <code>CategoryAxis</code>, 
   * and is a copy of that, with added control to skip some labels to be printed. 
   * 
   * @param g2 the graphics device (<code>null</code> not permitted). 
   * @param dataArea the area inside the axes (<code>null</code> not 
   *          permitted). 
   * @param edge the axis location (<code>null</code> not permitted). 
   * @param state the axis state (<code>null</code> not permitted). 
   * @param plotState collects information about the plot (<code>null</code> 
   *          permitted). 
   * 
   * @return The updated axis state (never <code>null</code>). 
   */ 
  protected AxisState drawCategoryLabels(Graphics2D g2, Rectangle2D dataArea, 
                                         RectangleEdge edge, AxisState state, 
                                         PlotRenderingInfo plotState) {
    if (state == null) { 
      throw new IllegalArgumentException("Null 'state' argument."); 
    } 
    
    if (isTickLabelsVisible()) { 
      g2.setFont(getTickLabelFont()); 
      g2.setPaint(getTickLabelPaint()); 
      //System.out.println("refresh ticks");
      List ticks = refreshTicks(g2, state, dataArea, edge); 

      try {
    	  state = drawTheChart(g2, dataArea, edge, state, plotState, ticks);
      }
      catch (Exception excep) {
    	  throw new IllegalArgumentException(excep.getMessage()); 
      }
    }
    return state;
  } 
  private AxisState drawTheChart(Graphics2D g2, Rectangle2D dataArea, 
          RectangleEdge edge, AxisState state, 
          PlotRenderingInfo plotState, List ticks) throws Exception {

	  LabelDefs labelDefs = gatherDrawingGeometries(g2, dataArea, edge, state, ticks);
	  labelDefs = layoutLabels(labelDefs, this.layoutType);
	  labelDefs = removeOverlays(labelDefs, CategoryAxisSkipLabels.N_STEP_ALGO); 

	  state = drawLabels(g2, edge, state, plotState, dataArea, labelDefs);
	  
	  return state; 
  }
  
  private LabelDefs layoutLabels(LabelDefs labelDefs, int layoutType){
  	if (layoutType == CategoryAxisSkipLabels.STAGGER_LAYOUT){
  		labelDefs = staggerLabels(labelDefs);
  	}
  	return labelDefs;
  }
  
  private LabelDefs staggerLabels(LabelDefs labelDefs){
  	for (int labelPosition=0; labelPosition<labelDefs.size(); labelPosition++) {
  		if ((labelPosition % 2) == 0){
    		labelDefs.staggerLabelDefAt(labelPosition);
  		}
  	}
  	return labelDefs;
  }
  // this code derived from Tobi's example
  private Line2D createTickMarkLine(Graphics2D g2, int tickPositon, Rectangle2D dataArea, 
			   RectangleEdge edge, AxisState state){
      float xx = (float) translateValueToJava2D(tickPositon, dataArea, edge); 
      
      Line2D mark = null; 
      double ol = getTickMarkOutsideLength(); 
      double il = getTickMarkInsideLength(); 
      g2.setStroke(getTickMarkStroke()); 
      g2.setPaint(getTickMarkPaint()); 
      if (edge == RectangleEdge.LEFT) { 
          mark = new Line2D.Double(state.getCursor() - ol, xx, state.getCursor() + il, xx); 
      } 
      else if (edge == RectangleEdge.RIGHT) { 
          mark = new Line2D.Double(state.getCursor() + ol, xx, state.getCursor() - il, xx); 
      } 
      else if (edge == RectangleEdge.TOP) { 
          mark = new Line2D.Double(xx, state.getCursor() - ol, xx, state.getCursor() + il); 
      } 
      else if (edge == RectangleEdge.BOTTOM) { 
          mark = new Line2D.Double(xx, state.getCursor() + ol, xx, state.getCursor() - il); 
      } 
      return mark;
  }
  
  private double translateValueToJava2D(int count, 
          Rectangle2D area, 
          RectangleEdge edge) { 
  
     CategoryPlot plot = (CategoryPlot)getPlot(); 
     CategoryAnchor anchor = plot.getDomainGridlinePosition(); 
      RectangleEdge domainAxisEdge = edge;//plot.getDomainAxisEdge(); 
      CategoryDataset data = plot.getDataset(); 
          if (data != null) { 
              CategoryAxis axis = plot.getDomainAxis(); 
              if (axis != null) { 
                  int columnCount = data.getColumnCount(); 
                  return axis.getCategoryJava2DCoordinate( 
                          anchor, count, columnCount, area, domainAxisEdge 
                      ); 
              } 
          } 
  
          return 0.0d; 
  } 

  private Rectangle2D calcAdjustedDataArea(Rectangle2D dataArea, RectangleEdge edge){
      // added by ray to draw tick marks ray_lukas@comcast.net
      CategoryPlot plot = (CategoryPlot) getPlot(); 
      
      Rectangle2D adjustedDataArea = new Rectangle2D.Double(); 
      if (plot.getRenderer() instanceof Effect3D) { 
          Effect3D e3D = (Effect3D) plot.getRenderer(); 
          double adjustedX = dataArea.getMinX(); 
          double adjustedY = dataArea.getMinY(); 
          double adjustedW = dataArea.getWidth() - e3D.getXOffset(); 
          double adjustedH = dataArea.getHeight() - e3D.getYOffset(); 

          if (edge == RectangleEdge.LEFT || edge == RectangleEdge.BOTTOM) { 
              adjustedY += e3D.getYOffset(); 
          } 
          else if (edge == RectangleEdge.RIGHT || edge == RectangleEdge.TOP) { 
              adjustedX += e3D.getXOffset(); 
          } 
          adjustedDataArea.setRect(adjustedX, adjustedY, adjustedW, adjustedH); 
      } 
      else { 
          adjustedDataArea.setRect(dataArea); 
      } 
      return adjustedDataArea;
      //-------------------- end of add by ray lukas  ray_lukas@comcast.net 	  
  }
  private LabelDefs gatherDrawingGeometries(Graphics2D g2, Rectangle2D dataArea, 
          							   RectangleEdge edge, AxisState state, List ticks){
	  int labelCount = 0;
	  LabelDefs labelDefs = new LabelDefs();
	  for (int categoryIndex=0; categoryIndex<ticks.size(); categoryIndex++) {
	      CategoryTick tick = (CategoryTick) ticks.get(categoryIndex); 
	      g2.setPaint(getTickLabelPaint()); 
	
		  CategoryLabelPosition position = getCategoryLabelPositions().getLabelPosition(edge); 
	      Rectangle2D area = calculateNewDrawingRegion(categoryIndex, ticks, dataArea, state, edge);       
	      Point2D anchorPoint = RectangleAnchor.coordinates(area, position.getCategoryAnchor()); 
	
	      TextBlock block = tick.getLabel(); 
	      
	      Shape bounds = block.calculateBounds(g2, (float) anchorPoint.getX(), 
	                                         (float) anchorPoint.getY(), 
	                                         position.getLabelAnchor(), 
	                                         (float) anchorPoint.getX(), 
	                                         (float) anchorPoint.getY(), 
	                                         position.getAngle()); 
	      Line2D mark = createTickMarkLine(g2, categoryIndex, dataArea, edge, state);
	      labelDefs.add("label" + labelCount, tick, mark, bounds, anchorPoint);
	  }
	  return labelDefs;
  }

 
  private AxisState drawLabels(Graphics2D g2, RectangleEdge edge, AxisState state, PlotRenderingInfo plotState, Rectangle2D dataArea, LabelDefs labelDefs)throws Exception {
	  state.setTicks(gatherTicks(labelDefs)); 
	  Object labelDef = null;
	  Iterator labelDefsIter = labelDefs.iterator();
	  
	  while (labelDefsIter.hasNext()) {
		  labelDef = labelDefsIter.next();
		  
		  TextBlock block = ((CategoryTick)labelDefs.getField(labelDef, LabelDefs.CATEGORY_TICK)).getLabel(); 
		  Line2D mark = (Line2D)labelDefs.getField(labelDef, LabelDefs.TICK_MARK_LINE);
		  Point2D anchorPoint = (Point2D)labelDefs.getField(labelDef, LabelDefs.ANCHOR_POINT);
		  CategoryLabelPosition position = getCategoryLabelPositions().getLabelPosition(edge);
		  
		  if ((this.displaySkippedTickMarks) && (super.isTickMarksVisible())){
			  g2.draw(mark);
		  }
		  
		  if (labelDefs.drawThisLabel(labelDef)) {
			  if ((!this.displaySkippedTickMarks) && (super.isTickMarksVisible())){
				  g2.draw(mark);
			  }
		      block.draw(g2, (float) anchorPoint.getX(), (float) anchorPoint.getY(), 
		                 position.getLabelAnchor(), (float) anchorPoint.getX(), 
		                 (float) anchorPoint.getY(), position.getAngle()); 
		  }
	      updatePlotState(plotState, (Shape)labelDefs.getField(labelDef, LabelDefs.BOUNDS));
	  }
	  return updateAxisState(state, edge);
  }


  private List gatherTicks(LabelDefs labelDefs) throws Exception {
	  List ticks = new Vector();
	  Iterator labelDefsIter = labelDefs.iterator();
	  while (labelDefsIter.hasNext()) {
		  ticks.add(labelDefs.getField(labelDefsIter.next(), LabelDefs.CATEGORY_TICK));
	  }
	  return ticks;
  }
  
  private void updatePlotState(PlotRenderingInfo plotState, Shape labelBounds){
		if (plotState != null) { 
			
			ChartRenderingInfo chartRendInfo = plotState.getOwner();
			if (chartRendInfo != null) {
				// seems to be used for processing image maps
				EntityCollection entities = chartRendInfo.getEntityCollection(); 
				if (entities != null) { 
				      //String tooltip = (String) categoryLabelToolTips.get(tick.getCategory()); 
					String tooltip = null; 
					entities.add(new TickLabelEntity(labelBounds, tooltip, null)); 
				} 
			}
		} 
  }
  
  private AxisState updateAxisState(AxisState state, RectangleEdge edge){
      if (edge.equals(RectangleEdge.TOP)) { 
          double h = state.getMax(); 
          state.cursorUp(h); 
        } 
        else if (edge.equals(RectangleEdge.BOTTOM)){ 
          double h = state.getMax(); 
          state.cursorDown(h); 
        } 
        else if (edge == RectangleEdge.LEFT){ 
          double w = state.getMax(); 
          state.cursorLeft(w); 
        } 
        else if (edge == RectangleEdge.RIGHT){ 
          double w = state.getMax(); 
          state.cursorRight(w); 
        } 
      return state;
  }

  
  private Rectangle2D calculateNewDrawingRegion(int categoryIndex, List ticks, Rectangle2D dataArea, 
		  										   AxisState state, RectangleEdge edge) {
      double x0 = 0.0; 
      double x1 = 0.0; 
      double y0 = 0.0; 
      double y1 = 0.0; 
      if (edge == RectangleEdge.TOP) { 
        x0 = getCategoryStart(categoryIndex, ticks.size(), dataArea, edge); 
        x1 = getCategoryEnd(categoryIndex, ticks.size(), dataArea, edge); 
        y1 = state.getCursor() - getCategoryLabelPositionOffset(); 
        y0 = y1 - state.getMax(); 
      } 
      else if (edge == RectangleEdge.BOTTOM) { 
        x0 = getCategoryStart(categoryIndex, ticks.size(), dataArea, edge); 
        x1 = getCategoryEnd(categoryIndex, ticks.size(), dataArea, edge); 
        y0 = state.getCursor() + getCategoryLabelPositionOffset(); 
        y1 = y0 + state.getMax(); 
      } 
      else if (edge == RectangleEdge.LEFT) { 
        y0 = getCategoryStart(categoryIndex, ticks.size(), dataArea, edge); 
        y1 = getCategoryEnd(categoryIndex, ticks.size(), dataArea, edge); 
        x1 = state.getCursor() - getCategoryLabelPositionOffset(); 
        x0 = x1 - state.getMax(); 
      } 
      else if (edge == RectangleEdge.RIGHT) { 
        y0 = getCategoryStart(categoryIndex, ticks.size(), dataArea, edge); 
        y1 = getCategoryEnd(categoryIndex, ticks.size(), dataArea, edge); 
        x0 = state.getCursor() + getCategoryLabelPositionOffset(); 
        x1 = x0 - state.getMax(); 
      } 
      return new Rectangle2D.Double(x0, y0, (x1 - x0), (y1 - y0)); 
  }
  public static TextBlock createTextBlock(final String text, final Font font,
	      final Paint paint) {
	  
		final TextBlock result = new TextBlock();
		result.addLine(text, font, paint);
	
	  return result;
	}

  private void notifyInvalidAlgorithRequest(String caller, LabelDefs labelDefs, int algorithmType) throws Exception {
	  String errorString = caller + "CategoryAxisSkipLabels requested an invalid algorithm type\n" +
	  		"when attempting to remove overlapping labels. This is an internal error and should\n" +
	  		"have never happened. This error needs to be reported to the development team at\n" +
	  		"Pilot Software. " +
	  		"\n\tThe offending algorithm id tag was =>" + algorithmType +
	  		"\n\tWe where attempting to draw the following labels=>" + stringifyLabels(labelDefs);
	  throw new Exception(errorString);
  }
  private void notifyInvalidLayoutType(String caller, int layoutType) throws Exception{
  	String errorString = caller + "Invaild Layout Request: In the process of creating an Axis for\n" +
  			"this chart you requested an invalid layout. Currently this Axis supports two layout type,\n" +
  			"Standard and Staggered. To solve this problem go to the creator of this axis. \n" +
  			"In this creator, chose from one of the public static layout constants that\n" +
  			"CategoryAxisSkipLabels provides. " +
  			"\n\tThe offending type that you passed in was=>" + layoutType ;
  	throw new Exception(errorString);
  }
  private String stringifyLabels(List labelDefs){
	  String strinifiedLabels = "";
	  Iterator labelDefsIter = labelDefs.iterator();
	  while (labelDefsIter.hasNext()) {
		  strinifiedLabels = strinifiedLabels.concat((labelDefsIter.next()).toString());
	  }
	  return strinifiedLabels;
  }
} 