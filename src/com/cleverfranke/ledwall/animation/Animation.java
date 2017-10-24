package com.cleverfranke.ledwall.animation;

import java.util.List;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public abstract class Animation {
	
	private boolean inDefaultRotation;	// Whether visual should be in default rotation
	protected PApplet applet;
	protected PGraphics graphicsContext;
	protected PImage image;

	
	public Animation(boolean inDefaultRotation, PApplet applet) {
		this.inDefaultRotation = inDefaultRotation;
		this.applet = applet;

		this.graphicsContext = applet.createGraphics(WallConfiguration.COLUMNS_COUNT, WallConfiguration.ROWS_COUNT);
		this.image = new PImage(WallConfiguration.COLUMNS_COUNT, WallConfiguration.ROWS_COUNT * 4);
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
	public PImage draw() {
		
		// Draw animation frame to image
		graphicsContext.beginDraw();
		drawAnimationFrame(graphicsContext);		
		graphicsContext.endDraw();
		image = graphicsContext.get();
		
		//Return image
		return getImage();
		
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
	public PImage getImage() {
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
	public int generateRandomRGBColor() {
		int r = (int) (Math.random() * 255);
		int g = (int) (Math.random() * 255);
		int b = (int) (Math.random() * 255);
		return PColor.color(r, g, b);
	}
	
	/**
	 * Generate a random RGBA color
	 * @return PColor
	 */
	public int generateRandomRGBAColor(int alpha) {
		int r = (int) (Math.random() * 255);
		int g = (int) (Math.random() * 255);
		int b = (int) (Math.random() * 255);
		return PColor.color(r, g, b, alpha);
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
	
	
	public int[] mapPanelWidth() {
		int[] mapped = new int[WallConfiguration.PANEL_COUNT];
		for (int i=0; i < WallConfiguration.PANEL_COUNT; i++) {
			mapped[i] = (int) PApplet.map(WallConfiguration.PANEL_WIDTH[i], 0, WallConfiguration.SOURCE_IMG_HEIGHT, 0, WallConfiguration.ROWS_COUNT);
		}
		return mapped;
	}
}
