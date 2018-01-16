package nl.sensorlab.videowall.animation;

import java.awt.Point;
import java.awt.Rectangle;

import nl.sensorlab.videowall.walldriver.WallGeometry;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Animation class that allows for drawing on a normal canvas, that mimicks the actual wall, abstracting away
 * all the pixel mapping and the non-regular grid. This is the most convenient class to subclass when
 * making a 'raster' based animation.
 */
public abstract class BaseCanvasAnimation extends BaseAnimation {
	
	public static final String CANVAS_MODE_2D = PConstants.P2D;		// 2D Mode
	public static final String CANVAS_MODE_3D = PConstants.P3D;		// 3D Mode
	public static final float DEFAULT_SCALE = 0.5f; 				// Wall scale from cm to pixels
	
	private Rectangle canvasGeometry;	// Geometry of the canvas to draw on
	private PGraphics canvasContext;	// Graphics context of the canvas
	private PImage canvasImage;			// Image that is the result of the canvas context
	private int[] canvasPixelMapping;	// Mapping from led pixel index to canvas pixel index 
	
	/**
	 * Construct Canvas Animation
	 * 
	 * @param applet (
	 * @param scale 	use BaseCanvasAnimation.DEFAULT_SCALE by default
	 * @param mode		Canvas mode, use either BaseCanvasAnimation.CANVAS_MODE_2D or BaseCanvasAnimation.CANVAS_MODE_3D
	 */
	public BaseCanvasAnimation(PApplet applet, float scale, String mode) {
		super(applet);
		
		// Create canvas
		canvasGeometry = WallGeometry.scaleRectangleRounded(WallGeometry.getInstance().getWallGeometry(), scale);
		canvasContext = applet.createGraphics(canvasGeometry.width, canvasGeometry.height, mode); 
		canvasImage = applet.createImage(canvasGeometry.width, canvasGeometry.height, PConstants.RGB); 
		
		// Create pixel mapping
		canvasPixelMapping = new int[WallGeometry.getInstance().getPixelCount()];
		for (int i = 0; i < canvasPixelMapping.length; i++) {
			Point canvasCoordinate = WallGeometry.scalePointRounded(WallGeometry.getInstance().getPixelCoordinates(i), DEFAULT_SCALE);
			int canvasIndex = canvasCoordinate.y * canvasGeometry.width + canvasCoordinate.x;
			canvasPixelMapping[i] = canvasIndex;
		}
		
	}

	@Override
	protected final void drawAnimationFrame(PGraphics g) {
		
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
	
	/**
	 * Implementation of generating the animation frame by child classes. Classes
	 * extending BaseCanvasAnimation are required to implement this method and use
	 * the supplied PGraphics context to draw the animation frame
	 * 
	 * @param g
	 */
	protected abstract void drawCanvasAnimationFrame(PGraphics g);
	
	/**
	 * Fetch canvas image for preview purposes
	 * 
	 * @return
	 */
	public final PImage getCanvasImage() {
		return canvasImage;
	}
	
	/**
	 * Get canvas geometry in pixels
	 * 
	 * @return
	 */
	protected final Rectangle getGeometry() {
		return canvasGeometry;
	}
	
}
