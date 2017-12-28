package com.cleverfranke.ledwall.animation;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * BaseAnimation class is the parent class of all the animations. It draws an 
 * animation frame on the graphicsContext which has as many pixels as the number 
 * of leds on the wall.
 * 
 * This is the most 'low-level' way to display content on the wall and does not
 * take the spacing between the pixel columns into account. For a more convenient
 * interface to draw content see CanvasAnimation.
 */
public abstract class BaseAnimation {
	
	// Pixel canvas resolution
	public static final int PIXEL_RESOLUTION_X = 26;	// Image width in pixels (number of pixels on the wall)
	public static final int PIXEL_RESOLUTION_Y = 81;	// Image height in pixels (number of pixels on the wall)
	
	// Animation members
	protected PApplet applet;						// The parent applet
	protected PGraphics graphicsContext;			// Graphics context to draw animation frames on
	private PImage lastAnimationFrame;				// Storage for the last drawn animation frame
	
	/**
	 * Initialize a PixelAnimation
	 * 
	 * @param applet
	 */
	public BaseAnimation(PApplet applet) {
		this.applet = applet;
		this.graphicsContext = applet.createGraphics(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
	}
	
	/**
	 * Draw (and return) animation frame. The generated image will be 
	 * PIXEL_RESOLUTION_X px wide by PIXEL_RESOLUTION_Y px high
	 * 
	 * @return the generated animation frame
	 */
	final public PImage draw() {
		
		// Draw animation frame
		graphicsContext.beginDraw();
		drawAnimationFrame(graphicsContext);
		graphicsContext.endDraw();
		
		// Capture (and return) frame
		lastAnimationFrame = graphicsContext.get();
		return lastAnimationFrame;
		
	}
	
	/**
	 * Implementation of generating the animation frame by child classes. Classes
	 * extending BaseAnimation are required to implement this method and use
	 * the supplied PGraphics context to draw the animation frame
	 * 
	 * @param g
	 */
	abstract protected void drawAnimationFrame(PGraphics g);
	
	/**
	 * Fetch the image created by draw method. The image will be 
	 * PIXEL_RESOLUTION_X px wide by PIXEL_RESOLUTION_Y px high
	 * 
	 * @return
	 */
	final public PImage getImage() {
		return lastAnimationFrame;
	}
	
	/**
	 * Method that is called just before an animation is started. THis
	 * can happen multiple times in the application lifecycle, as other
	 * animations are ran and then switched back to this application. 
	 * 
	 * This method can be used to (re-)seed random values of the animation
	 * and (re)load assets or to start processes such as audio or network
	 * processing
	 */
	public void isStarting() {}
	
	/**
	 * Method that is called after the animation has ended (for example when
	 * the user starts another animation). 
	 * 
	 * This method can be used to stop intensive tasks the animation depends
	 * on such as audio or network processing
	 */
	public void isStopping() {
		
	}
	
	/**
	 * Method that is called to animation that can have data attached to it, 
	 * such as filepaths to video or audio files. Each animation is supposed
	 * to handle the supplied data themselves (e.g. parsing and using the data)
	 * 
	 * @param data
	 */
	public void setData(String data) {
		
	}
	
}
