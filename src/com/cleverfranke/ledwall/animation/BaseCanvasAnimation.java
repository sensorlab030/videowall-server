package com.cleverfranke.ledwall.animation;

import java.awt.Point;
import java.awt.Rectangle;

import com.cleverfranke.ledwall.walldriver.WallGeometry;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Animation that allows for drawing on a normal canvas, that mimicks the actual wall, abstracting away
 * all the pixel mapping and the non-regular grid. This is the most convenient class to subclass when
 * making a 'raster' based animation.
 */
public abstract class BaseCanvasAnimation extends BasePixelAnimation {
	
	public static final float SCALE = 0.5f; // Scale from cm to pixels
	
	private Rectangle canvasGeometry;
	private PGraphics canvasContext;
	private PImage canvasImage;
	
	private int[] canvasPixelMapping;
	
	public BaseCanvasAnimation(PApplet applet) {
		super(applet);
		
		// Create canvas
		canvasGeometry = WallGeometry.scaleRectangleRounded(WallGeometry.getInstance().getWallGeometry(), SCALE);
		canvasContext = applet.createGraphics(canvasGeometry.width, canvasGeometry.height); 
		canvasImage = applet.createImage(canvasGeometry.width, canvasGeometry.height, PConstants.RGB); 
		
		// Create pixel mapping
		canvasPixelMapping = new int[WallGeometry.getInstance().getPixelCount()];
		for (int i = 0; i < canvasPixelMapping.length; i++) {
			Point canvasCoordinate = WallGeometry.scalePointRounded(WallGeometry.getInstance().getPixelCoordinates(i), SCALE);
			int canvasIndex = canvasCoordinate.y * canvasGeometry.width + canvasCoordinate.x;
			canvasPixelMapping[i] = canvasIndex;
		}
		
		
	}

	@Override
	final protected void drawAnimationFrame(PGraphics g) {
		
		// Draw canvas animation on context
		canvasContext.beginDraw();
		drawCanvasAnimationFrame(canvasContext);
		canvasContext.endDraw();
		canvasImage = canvasContext.get();
		
		// Convert canvas context to pixel context
		canvasImage.loadPixels();
		g.loadPixels();
		for (int i = 0; i < WallGeometry.getInstance().getPixelCount(); i++) {
			g.pixels[i] = canvasImage.pixels[canvasPixelMapping[i]];
		}
		g.updatePixels();
		
	}
	
	abstract protected void drawCanvasAnimationFrame(PGraphics g);
	
	/**
	 * Fetch canvas image for preview purposes
	 * 
	 * @return
	 */
	public PImage getCanvasImage() {
		return canvasImage;
	}
	
	/**
	 * Get canvas geometry in pixels
	 * 
	 * @return
	 */
	protected Rectangle getGeometry() {
		return canvasGeometry;
	}
	
}
