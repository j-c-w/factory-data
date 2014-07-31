package backend.java.chartPatch;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;

import org.jfree.chart.axis.CategoryTick;

public class LabelDefs extends Vector {
	public static int CATEGORY_TICK = 0;
	public static int BOUNDS = 1;
	public static int ANCHOR_POINT = 2;
	public static int DRAWN = 3;
	public static int TICK_MARK_LINE = 4;
	
	private static double CATEGORY_GAP_SIZE = 8.0;
	private static double CATEGORY_STAGGER_SIZE = 14.0;
	
	
	public void add(String name, CategoryTick tick, Line2D mark, Shape bounds, Point2D anchorPoint){
		this.add(new LabelDef(name, tick, mark, bounds, anchorPoint));
	}
	public Shape getCandidateShape(int candidatePosition){
		return ((LabelDef)this.get(candidatePosition)).getBounds();
	}
	public void staggerLabelDefAt(int candidatePosition){
		((LabelDef)this.get(candidatePosition)).stagger();
	}
	public Object getField(Object labelDef, int fieldID) throws Exception {
		Object returnObj = null;
		if (labelDef instanceof LabelDef){
			if (fieldID == CATEGORY_TICK) {
				returnObj = ((LabelDef)labelDef).getTick();
			} else if (fieldID == BOUNDS) {
				returnObj = ((LabelDef)labelDef).getBounds();
			} else if (fieldID == ANCHOR_POINT) {
				returnObj = ((LabelDef)labelDef).getAnchorPoint();
			} else if (fieldID == DRAWN) {
				returnObj = ((LabelDef)labelDef).drawn;
			} else if (fieldID == TICK_MARK_LINE) {
				returnObj = ((LabelDef)labelDef).mark;
			}
			else {
				notifyInvalidFieldIDReuquested("[LabelDefs::getField]", labelDef, fieldID);
			}
		}
		else {
			notifyInternalCastError("[LabelDefs::getField]", labelDef, LabelDef.class);
		}
		return returnObj;
	}
	public boolean drawThisLabel(Object labelDef) throws Exception {
		boolean shouldIDrawThisLabel = false;
		if (labelDef instanceof LabelDef) {
			shouldIDrawThisLabel = ((LabelDef)labelDef).isDrawn();
		} else {
			notifyInternalCastError("[LabelDefs::drawThisLabel]", labelDef, LabelDef.class);
		}
		return shouldIDrawThisLabel;
	}
	

	// look through all drawn lables
	public boolean intersectsAny(final Shape candidateShape){
		boolean intersects = false;
		LabelDef labelDef = null;
		Rectangle2D currentBounds = gappedRectange(candidateShape.getBounds2D());
	
		Iterator LabelDefIter = this.iterator();
		while ((LabelDefIter.hasNext()) && (!intersects) ){
			labelDef = (LabelDef)LabelDefIter.next();
			if (labelDef.isDrawn()) {
				intersects = gappedRectange(labelDef.getBounds().getBounds2D()).intersects(currentBounds);
			}
		}
		return intersects;
	}
	
	
	
	  
	private Rectangle2D gappedRectange(Rectangle2D rectToGap){
		double newMaxX = rectToGap.getBounds2D().getX() + CATEGORY_GAP_SIZE;
		double newMaxY = rectToGap.getBounds2D().getY();
		Rectangle2D currentBoundsWithGap = new Rectangle2D.Double(newMaxX, newMaxY, 
				rectToGap.getBounds2D().getWidth() + CATEGORY_GAP_SIZE, 
				rectToGap.getBounds2D().getHeight());
		return currentBoundsWithGap;
	}
	public void markForDraw(int candidatePosition){
		((LabelDef)this.get(candidatePosition)).markAsDrawn();
	}
	public void unMarkAllForDraw(){
		Iterator LabelDefIter = this.iterator();
		while (LabelDefIter.hasNext()){
			((LabelDef)LabelDefIter.next()).setDrawn(false);
		}
	}
	private void notifyInternalCastError(String caller, Object unknown, Class expectedClass) throws Exception {
		String errorMessage = caller + "InternalCastError" +
				"\n\tThe system tried to invoke this method with a " + unknown.getClass().getName()+ 
				"\n\ttype object when a " + expectedClass.getName() + " type object was expected. This " +
				"\n\tis an internal error and should be reported to the development team. In addition " +
				"\n\tyou could also report this error to the author of this code at ray_lukas@comcast.net.";
		throw new Exception(errorMessage);
	}
	private void notifyInvalidFieldIDReuquested(String caller, Object unknown, int fieldID) throws Exception {
		String errorMessage = caller + "InvalidFieldIDReuquested" +
		"\n\tThe system tried to request a filed from a LabelDef object, which is not supported. " +
		"\n\tThis is an internal error and should be reported to the development team. In addition " +
		"\n\tyou could also report this error to the author of this code at ray_lukas@comcast.net. " +
		"\n\tIn general you should only invoke this method using one of the shared public constants " +
		"\n\texposed by this class.\n\t\tOffending Class=>" + unknown.getClass().getName() +
		"\n\t\tOffending Field ID=>" + fieldID;
		throw new Exception(errorMessage);
	}
	
	private class LabelDef {
		private String name = null;
		private Boolean drawn = new Boolean(false);
		private CategoryTick tick = null;
		private Line2D mark = null;
		private Shape bounds = null;  
		private Point2D anchorPoint =  null;
		public LabelDef(String name, CategoryTick tick, Line2D mark, Shape bounds, Point2D anchorPoint){
			this.name = name;
			this.tick = tick;
			this.mark = mark;
			this.anchorPoint = anchorPoint;
			this.bounds = bounds;
		}
		public boolean isDrawn() {
			return drawn.booleanValue();
		}
		public void markAsDrawn() {
			this.drawn = new Boolean(true);
		}	
		public void setDrawn(boolean markAsDrawnFlag) {
			this.drawn = new Boolean(markAsDrawnFlag);
		}
		public void stagger(){
			//System.out.println("(xPos=>" + this.bounds.getBounds2D().getX() + ", YPos=>" + this.bounds.getBounds2D().getY() + ")" + 
			//		" width=>" + this.bounds.getBounds2D().getWidth() + " height=>" + this.bounds.getBounds2D().getHeight());
			this.anchorPoint.setLocation(this.anchorPoint.getX(), this.anchorPoint.getY() + CATEGORY_STAGGER_SIZE);
			double xPos = this.bounds.getBounds2D().getX();
			double newYPos = this.bounds.getBounds2D().getY() + CATEGORY_STAGGER_SIZE;
			double height = this.bounds.getBounds2D().getHeight();
			double width = this.bounds.getBounds2D().getWidth();
			this.bounds = new Rectangle2D.Double(xPos, newYPos, width,	height);
			//System.out.println("(xPos=>" + this.bounds.getBounds2D().getX() + ", YPos=>" + this.bounds.getBounds2D().getY() + ")" + 
			//		" width=>" + this.bounds.getBounds2D().getWidth() + " height=>" + this.bounds.getBounds2D().getHeight());
		}
		public CategoryTick getTick() {
			return tick;
		}
		public Line2D getMark() {
			return mark;
		}
		public Point2D getAnchorPoint() {
			return anchorPoint;
		}
		public Shape getBounds() {
			return bounds;
		}
		public String toString(){
			  return "LabelPoint=>" +  name +
				  "\n\tanchorPoint=>(" + anchorPoint.getX() + ", " + anchorPoint.getY() + ")" + 
				  "\n\tgetMinX()=>" + (bounds.getBounds2D()).getMinX() + " getMaxX()=>" + (bounds.getBounds2D()).getMaxX();
		  }
	  }	  
}
