package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

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
	
}
