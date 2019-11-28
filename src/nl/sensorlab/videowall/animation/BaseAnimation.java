package nl.sensorlab.videowall.animation;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nl.sensorlab.videowall.property.Property;
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
		setup(graphicsContext);
	}
	
	/**
	 * Draw (and return) animation frame. The generated image will be 
	 * PIXEL_RESOLUTION_X px wide by PIXEL_RESOLUTION_Y px high
	 * 
	 * @param dt the time in ms between the current and previous draw call
	 * @return the generated animation frame
	 */
	final public PImage draw(double dt) {
		
		// Draw animation frame
		graphicsContext.beginDraw();
		drawAnimationFrame(graphicsContext, dt);
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
	 * @param dt the time in ms between the current and previous draw call
	 */
	abstract protected void drawAnimationFrame(PGraphics g, double dt);
	
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
	 * Setup method that is called directly after the animation is
	 * constructed. It can be used to setup variables that are dependents
	 * on the graphics context
	 */
	public void setup(PGraphics g) {}
	
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
	public void isStopping() {}
	
	/**
	 * Method that is called to animation that can have data attached to it, 
	 * such as filepaths to video or audio files. Each animation is supposed
	 * to handle the supplied data themselves (e.g. parsing and using the data)
	 * 
	 * @param data
	 */
	public void setData(String data) {}
	
	/**
	 * Get file path from a compiled resource in the nl.sensorlab.videowall.resources package
	 * @return
	 */
	public String getResource(String relativePath) {
		
		URL url = getClass().getClassLoader().getResource("nl/sensorlab/videowall/resources/" + relativePath);
		if (url == null) {
			return null;
		} else if (url.toString().startsWith("jar:")) {
			// Run from jar: Return path in jar
			return url.toString();
		} else {
			// Return file path
			return url.getFile().toString();
		}
		
	}
	
	public List<Property> getProperties() {
		return new ArrayList<Property>();
	}

}
