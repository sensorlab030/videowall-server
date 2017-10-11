package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;

import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public abstract class Animation {
	
	protected PApplet applet;
	private PGraphics graphicsContext;
	private PImage image;
	public static Animation self;
	
	public Animation(PApplet applet) {
		this.applet = applet;
		graphicsContext = applet.createGraphics(WallConfiguration.SOURCE_IMG_WIDTH, WallConfiguration.SOURCE_IMG_HEIGHT);
		image = new PImage(WallConfiguration.SOURCE_IMG_WIDTH, WallConfiguration.SOURCE_IMG_HEIGHT);
		self = this;
		applet.registerMethod("keyEvent", this);

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
	
}
