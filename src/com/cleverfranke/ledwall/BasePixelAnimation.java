package com.cleverfranke.ledwall;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public abstract class BasePixelAnimation {
	
	// Pixel canvas resolution
	public static final int PIXEL_RESOLUTION_X = 81;	// Width in pixels
	public static final int PIXEL_RESOLUTION_Y = 32;	// Height in pixels
	
	// Animation members
	protected PApplet applet;						// The parent applet
	protected PGraphics graphicsContext;			// Graphics context to draw animation frames
	private PImage resultImage;						// Storage for the last drawn animation frame
	
	/**
	 * Initialize a PixelAnimation
	 * 
	 * @param applet
	 */
	public BasePixelAnimation(PApplet applet) {
		this.applet = applet;
		this.graphicsContext = applet.createGraphics(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
	}
	
	/**
	 * Draw (and return) animation frame. The generated image will be 
	 * PIXEL_RESOLUTION_X by PIXEL_RESOLUTION_Y pixels
	 * 
	 * @return the generated animation frame
	 */
	final public PImage draw() {
		
		// Draw animation
		graphicsContext.beginDraw();
		doDraw(graphicsContext);
		graphicsContext.endDraw();
		
		// Capture frame
		resultImage = graphicsContext.get();
		return resultImage;
		
	}
	
	/**
	 * Implementation of generating the animation frame by child classes. Classes
	 * extending BasePixelAnimation are required to implement this method and use
	 * the supplied PGraphics context to draw the animation frame
	 * 
	 * @param g
	 */
	abstract protected void doDraw(PGraphics g);
	
	/**
	 * Fetch the image created by draw method. The image will be 
	 * PIXEL_RESOLUTION_X by PIXEL_RESOLUTION_Y pixels
	 * 
	 * @return
	 */
	public PImage getImage() {
		return resultImage;
	}

}
