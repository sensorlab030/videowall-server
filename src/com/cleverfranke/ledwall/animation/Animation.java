package com.cleverfranke.ledwall.animation;

import java.util.List;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;


public abstract class Animation {
	
	private boolean inDefaultRotation;	// Whether visual should be in default rotation
	protected PApplet applet;
	private static PGraphics graphicsContext;
	private PImage image;
	private boolean isDrawnOnGrid;
	
	public float XPANELSIDESCOORD[] = getXCoordOfPanelSides();	// X coordinates of each sides of the panels
	public float XPANELCOORD[] = getXCoordOfPanels();			// X coordinates of each panels
	public float PANEL_WIDTH[] = getPixelWidthsOfPanels();		// Width in pixels of each panels
	
	public Animation(boolean inDefaultRotation, boolean isDrawnOnGrid, PApplet applet) {
		this.inDefaultRotation = inDefaultRotation;
		this.applet = applet;
		this.isDrawnOnGrid = isDrawnOnGrid;
		
		if (this.isDrawnOnGrid) {
			graphicsContext = applet.createGraphics(WallConfiguration.COLUMNS_COUNT, WallConfiguration.ROWS_COUNT);
			image = new PImage(WallConfiguration.COLUMNS_COUNT, WallConfiguration.ROWS_COUNT * 4);
		} else {
			graphicsContext = applet.createGraphics(WallConfiguration.SOURCE_IMG_WIDTH, WallConfiguration.SOURCE_IMG_HEIGHT);
			image = new PImage(WallConfiguration.SOURCE_IMG_WIDTH, WallConfiguration.SOURCE_IMG_HEIGHT);
		}
	}
	
	
	/**
	 * Flags when animation is finished
	 * @return
	 */
	public abstract boolean isDone();
	
	/**
	 * Get if visualization is in default rotation
	 * 
	 * @return
	 */
	public boolean isInDefaultRotation() {
		return inDefaultRotation;
	}
	
	/**
	 * Draw new frame of the animation
	 * 
	 * @return the new frame
	 *
	 */
	public final PImage draw() {
		// Draw animation frame to image
		graphicsContext.beginDraw();
		drawAnimationFrame(graphicsContext);
		graphicsContext.endDraw();
		image = graphicsContext.get();
		
		if (isDrawnOnGrid) {
			image = projectGridToWall(image);
		}
		
		//Return image
		return getImage();
		
	}
	
	public PImage projectGridToWall(PImage grid){
		PImage wall = new PImage(WallConfiguration.SOURCE_IMG_WIDTH, WallConfiguration.SOURCE_IMG_HEIGHT);
		final int BEAM_WIDTH = WallConfiguration.PHYSICAL_BEAM_WIDTH_CM * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO;
		
		for (int i = 0; i< grid.width; i++){
			int x = 0;
			int panelIndex = (int) Math.ceil((float)i/2);
			
			for (int j = 0; j < grid.height; j++){
				int pixel = grid.get(i, j);
				
				if (i%2 == 0) {
					x = (int) (XPANELCOORD[panelIndex] + BEAM_WIDTH / 2);
				} else {
					x = (int) (XPANELCOORD[panelIndex]) - BEAM_WIDTH / 2 - 1;
				}
				
				wall.set(x, j*WallConfiguration.ROW_HEIGHT + 2, pixel);			
			}
		}
		return wall;
	}
	
	/**
	 * Draw animation frame on the given PGraphics context
	 * @param g
	 */
	public abstract void drawAnimationFrame(PGraphics g);


	/**
	 * Fetch the latest animation frame
	 * 
	 * @return latest animation frame
	 */
	public final PImage getImage() {
		return image;
	}
	
	/**
	 * Method that is called by the VisualizationManager just
	 * before the Visualization is shown, can be used to
	 * switch from subject, or change palette.
	 */
	public abstract void prepareForQueueRotation();
	
	/**
	 * Method that is called by the VisualizationManager just
	 * before the Visualization is shown, to check if the
	 * visualization can be put into rotation. If false is
	 * returned, the visualization is skipped
	 * 
	 * @return
	 */
	public boolean isAvailableForQueueRotation() {
		return true;
	}

	/**
	 * Method that is called by the VisualizationManager just
	 * after the Visualization transition is done.
	 */
	public void inAnimationDone() {}
	
	
	/*** UTILS ***/
	
	/**
	 * Generate a random RGB color
	 * @return PColor
	 */
	protected static int generateRandomRGBColor() {
		int r = (int) (Math.random() * 255);
		int g = (int) (Math.random() * 255);
		int b = (int) (Math.random() * 255);
		return PColor.color(r, g, b);
	}
	
	/**
	 * Generate a random RGBA color
	 * @return PColor
	 */
	protected static int generateRandomRGBAColor(int alpha) {
		int r = (int) (Math.random() * 255);
		int g = (int) (Math.random() * 255);
		int b = (int) (Math.random() * 255);
		return PColor.color(r, g, b, alpha);
	}
	
	
	/**
	 * Return an array that contains the X coordinates of the panel boundaries.
	 * @return xPos
	 */
	protected static float[] getXCoordOfPanels() {
		// Number of panels
		int PANEL_COUNT = WallConfiguration.PANEL_COUNT;
		
		// Array the will contain the x coordinates of all the panels
		float xPos[] = new float[PANEL_COUNT + 1];
		
		// Initial left x position
		xPos[0] = 0;
			
		for(int i = 1; i < PANEL_COUNT; i++) {
			// Get x right position of each panel
		    xPos[i] = WallConfiguration.PHYSICAL_PANEL_WIDTH_CM[i-1] * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO + xPos[i-1];
		}

		// Initial right end position
		xPos[PANEL_COUNT] = WallConfiguration.PHYSICAL_PANEL_WIDTH_CM[PANEL_COUNT-1] * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO + xPos[PANEL_COUNT-1];
		
		return xPos;
	}
	
	/**
	 * Return an array that contains the width in pixels of the panels
	 * @return xWidth
	 */
	protected static float[] getPixelWidthsOfPanels() {
		// Number of panels
		int PANEL_COUNT = WallConfiguration.PANEL_COUNT;
		
		// Array the will contain the x coordinates of all the panels
		float xWidth[] = new float[PANEL_COUNT];
			
		for(int i = 0; i < PANEL_COUNT; i++) {
			// Get x width
			xWidth[i] = WallConfiguration.PHYSICAL_PANEL_WIDTH_CM[i] * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO;
		}

		return xWidth;
	}

	/**
	 * Return an array that contains the width in pixels of the panels
	 * @return xWidth
	 */
	protected static float[] getPixelWidthsOfPanelSides() {
		// Number of panels
		int PANEL_COUNT = WallConfiguration.PANEL_COUNT;
		
		// Array the will contain the x coordinates of all the panels
		float xWidth[] = new float[PANEL_COUNT * 2];
			
		for(int i = 0; i < PANEL_COUNT * 2; i++) {
			int panelIndex = (int) Math.floor(i/2);
			// Get x width			
			xWidth[i] = (WallConfiguration.PHYSICAL_PANEL_WIDTH_CM[panelIndex] / 2) * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO;
		}

		return xWidth;
	}
	
	/**
	 * Return an array that contains the width in pixels of the panels
	 * @return xWidth
	 */
	protected static float[] getXCoordOfPanelSides() {
		// Number of panels
		int PANEL_COUNT = WallConfiguration.PANEL_COUNT;
		
		// Array the will contain the x coordinates of all the panels
		float xPos[] = new float[PANEL_COUNT * 2 + 1];
		
		// Array that contains the width of each panel sides
		float[] xWidth = getPixelWidthsOfPanelSides();
		
		xPos[0] = 0;
		
		for(int i = 1; i < PANEL_COUNT * 2 + 1; i++) {
			// Get x width			
			xPos[i] = xPos[i-1] + xWidth[i-1];
		}
		
		return xPos;
	}
	/**
	 * Draws a square, the width of panel
	 * @param panelId The id of the panel you want to draw the square in (from 0 to 12)
	 * @param panelCoord The y coordinate of the top left corner of the square
	 */
	public static void drawSquare(PGraphics g, int panelId, float panelCoord, int color) {
		float[] xPos = getXCoordOfPanels();
		float[] xWidth = getPixelWidthsOfPanels();
		
		g.fill(color);

		// Create shape
		PShape square = g.createShape();
		square.beginShape();
		
		// Add the 4 points to form a rectangle
		square.vertex(xPos[panelId], panelCoord);
		square.vertex(xPos[panelId+1], panelCoord);
		square.vertex(xPos[panelId+1], panelCoord + xWidth[panelId]);
		square.vertex(xPos[panelId], panelCoord + xWidth[panelId]);
	
		// Draw shape
		square.endShape();
		g.shape(square);
	}
	
	/**
	 * Draws a bar that starts from the bottom, the width of panel
	 * @param panelId The id of the panel you want to draw the bar in (from 0 to 12)
	 * @param panelCoord The y coordinate of the top left corner of the bar
	 */
	public void drawBottomBar(PGraphics g, int panelId, float panelCoord) {
		float[] xPos = getXCoordOfPanels();

		// Create shape
		PShape rect = g.createShape();
		rect.beginShape();
		
		// Add the 4 points to form a rectangle
		rect.vertex(xPos[panelId], panelCoord);
		rect.vertex(xPos[panelId+1], panelCoord);
		rect.vertex(xPos[panelId+1], g.height);
		rect.vertex(xPos[panelId], g.height);
	
		// Draw shape
		rect.endShape();
		g.shape(rect);
	}
	
	/**
	 * Draws a bar that starts from the top, the width of panel
	 * @param panelId The id of the panel you want to draw the bar in (from 0 to 12)
	 * @param panelCoord The y coordinate of the bottom left corner of the bar
	 */
	public void drawTopBar(PGraphics g, int panelId, float panelCoord) {
		float[] xPos = getXCoordOfPanels();

		// Create shape
		PShape rect = g.createShape();
		rect.beginShape();
		
		// Add the 4 points to form a rectangle
		rect.vertex(xPos[panelId], 0);
		rect.vertex(xPos[panelId+1], 0);
		rect.vertex(xPos[panelId+1], panelCoord);
		rect.vertex(xPos[panelId], panelCoord);
	
		// Draw shape
		rect.endShape();
		g.shape(rect);
	}
	
	/**
	 * Moves the coordinates of the drawing, per panel, one panel to the right
	 * @param panelCoord : An array containing the coordinates per panel. Index of the array is index of the panel
	 * @return : An array containing the same coordinates but offset of one to the right.
	 */
	public float[] movePatternRight(float[] panelCoord){
		float[] movedpanelCoord = new float[WallConfiguration.PANEL_COUNT + 1];
		
		movedpanelCoord[0] = panelCoord[WallConfiguration.PANEL_COUNT];
		for (int i = 0; i < WallConfiguration.PANEL_COUNT; i++) {
			movedpanelCoord[i + 1] = panelCoord[i];
		}
		
		return movedpanelCoord;
	}
	
	
	public float[] movePatternLeft(float[] panelCoord){
		float[] movedpanelCoord = new float[WallConfiguration.PANEL_COUNT + 1];
		
		movedpanelCoord[WallConfiguration.PANEL_COUNT] = panelCoord[0];
		for (int i = 1; i < WallConfiguration.PANEL_COUNT; i++) {
			movedpanelCoord[i - 1] = panelCoord[i];
		}
		
		return movedpanelCoord;
	}
	
	/**
	 * Test if a boolean arrayList is all true
	 * @param array
	 * @return boolean
	 */
	public static boolean areAllTrue(List<Boolean> array) {
	    for(Boolean b : array) if(!b) return false;
	    return true;
	}
	
	/**
	 * Test if a boolean array is all true
	 * @param array
	 * @return boolean
	 */
	public static boolean areAllTrue(boolean[] array) {
	    for(boolean b : array) if(!b) return false;
	    return true;
	}
	
	/**
	 * Generate an array of random values
	 * @param length: the number of random values wanted
	 * @return an array of random values, of length 'length'
	 */
	public float[] generateRandomValues(int length){
		float[] VALUES = new float[length];
		for(int i = 0; i < length; i++) {
			VALUES[i] = (float)(Math.random());
		}	
		
		return VALUES;
	}
	
	/**
	 * Find min and max in VALUES.
	 * Here we also use this function to generate random values at first, but the animation could be provided with real VALUES.
	 */
	public float[] findMinMaxValues(float[] VALUES) {
		float max = Integer.MIN_VALUE;	// Maximum of bar values
		float min = Integer.MAX_VALUE;	// Minimum of bar values
		
		for(int i = 0; i < VALUES.length; i++) {
		    if (VALUES[i] < min) { min = VALUES[i]; }
		    if (VALUES[i] > max) { max = VALUES[i]; }
		}	
		
		return new float[]{min, max};
	}
}
