package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;

public abstract class Animation {
	
	protected PApplet applet;
	private PGraphics graphicsContext;
	private PImage image;
	
	public Animation(PApplet applet) {
		this.applet = applet;
		graphicsContext = applet.createGraphics(WallConfiguration.SOURCE_IMG_WIDTH, WallConfiguration.SOURCE_IMG_HEIGHT);
		image = new PImage(WallConfiguration.SOURCE_IMG_WIDTH, WallConfiguration.SOURCE_IMG_HEIGHT);
		
		
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
		
		//Return image
		return getImage();
		
	}
	
	/**
	 * Fetch the latest animation frame
	 * 
	 * @return latest animation frame
	 */
	public final PImage getImage() {
		return image;
	}
	
	/**
	 * Prepare animation class
	 */
	public void prepare() {}
	
	/**
	 * Cleanup animation class
	 */
	public void cleanUp() {}
	
	/**
	 * Draw animation frame on the given PGraphics context
	 * @param g
	 */
	abstract protected void drawAnimationFrame(PGraphics g);
	
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
	 * Draws a square, the width of panel
	 * @param panelId The id of the panel you want to draw the square in (from 0 to 12)
	 * @param panelCoord The y coordinate of the top left corner of the square
	 */
	protected void drawSquare(int panelId, float panelCoord) {
		float[] xPos = getXCoordOfPanels();
		float[] xWidth = getPixelWidthsOfPanels();

		// Create shape
		PShape square = graphicsContext.createShape();
		square.beginShape();
		
		// Add the 4 points to form a rectangle
		square.vertex(xPos[panelId], panelCoord);
		square.vertex(xPos[panelId+1], panelCoord);
		square.vertex(xPos[panelId+1], panelCoord + xWidth[panelId]);
		square.vertex(xPos[panelId], panelCoord + xWidth[panelId]);
	
		// Draw shape
		square.endShape();
		graphicsContext.shape(square);
	}
	
	/**
	 * Draws a bar that starts from the bottom, the width of panel
	 * @param panelId The id of the panel you want to draw the bar in (from 0 to 12)
	 * @param panelCoord The y coordinate of the top left corner of the bar
	 */
	protected void drawBottomBar(int panelId, float panelCoord) {
		float[] xPos = getXCoordOfPanels();

		// Create shape
		PShape rect = graphicsContext.createShape();
		rect.beginShape();
		
		// Add the 4 points to form a rectangle
		rect.vertex(xPos[panelId], panelCoord);
		rect.vertex(xPos[panelId+1], panelCoord);
		rect.vertex(xPos[panelId+1], graphicsContext.height);
		rect.vertex(xPos[panelId], graphicsContext.height);
	
		// Draw shape
		rect.endShape();
		graphicsContext.shape(rect);
	}
	
	/**
	 * Draws a bar that starts from the top, the width of panel
	 * @param panelId The id of the panel you want to draw the bar in (from 0 to 12)
	 * @param panelCoord The y coordinate of the bottom left corner of the bar
	 */
	protected void drawTopBar(int panelId, float panelCoord) {
		float[] xPos = getXCoordOfPanels();

		// Create shape
		PShape rect = graphicsContext.createShape();
		rect.beginShape();
		
		// Add the 4 points to form a rectangle
		rect.vertex(xPos[panelId], 0);
		rect.vertex(xPos[panelId+1], 0);
		rect.vertex(xPos[panelId+1], panelCoord);
		rect.vertex(xPos[panelId], panelCoord);
	
		// Draw shape
		rect.endShape();
		graphicsContext.shape(rect);
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
}
