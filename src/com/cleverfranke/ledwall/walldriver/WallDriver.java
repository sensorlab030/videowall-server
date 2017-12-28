package com.cleverfranke.ledwall.walldriver;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class WallDriver {
	
	private WallDriverPort port1, port2;		// Wall ports
	private PGraphics imageBufferContext;		// Buffer context used to rotate the incoming image
	private PImage imageBuffer;					// Buffer image used by imageBufferContext
	private PImage port1Image, port2Image;		// Separate images to be sent to the two UC's driving the wall
	private boolean portsConnected;				// Flag to determine if ports are connected
	private PImage blackOutImage;				// Image that can is used to black out
	
	/**
	 * Initialize wall driver
	 * 
	 * @param applet
	 * @param portName1
	 * @param portName2
	 */
	public WallDriver(PApplet applet, String portName1, String portName2) {
		
		// Setup ports
		try {
			port1 = new WallDriverPort(applet, portName1, true);
			port2 = new WallDriverPort(applet, portName2, false);
			portsConnected = true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			portsConnected = false;
		}
		
		// Create buffer
		imageBufferContext = applet.createGraphics(81, 26);
		imageBuffer = applet.createImage(81, 26, PConstants.RGB);
		
		// Create port images
		port1Image = applet.createImage(WallDriverPort.PANEL_IMAGE_WIDTH, WallDriverPort.PANEL_IMAGE_HEIGHT, PConstants.RGB);
		port2Image = applet.createImage(WallDriverPort.PANEL_IMAGE_WIDTH, WallDriverPort.PANEL_IMAGE_HEIGHT, PConstants.RGB);
		
		// Create blackout image (black image)
		blackOutImage = applet.createImage(26, 81, PConstants.RGB);
		
	}
	
	/**
	 * Send an image to be displayed on the led wall. The image is expected to be 26px wide and 81px high in RGB. 
	 * Brightness of the image will be controlled by the WallDriver. 
	 * 
	 * @param image
	 */
	public void displayImage(PImage image) {
		
		// Check for valid image
		if (image == null) {
			System.err.println("No image");
			return;
		}
		if (image.width != 26 || image.height != 81) {
			System.err.println("Invalid image dimensions");
			return;
		}
		
		// Create image buffer image by rotating the image 90degrees clockwise. Image buffer 
		// is thus 81px wide and 26px high 
		imageBufferContext.beginDraw();
		imageBufferContext.translate(81, 0);
		imageBufferContext.rotate(PApplet.radians(90));
		imageBufferContext.image(image, 0, 0);
		imageBufferContext.endDraw();
		imageBuffer = imageBufferContext.get();
		
		// Update led images by copying relevant parts of the images 
		// to an image that is 81px wide and 16px high
		port1Image.copy(imageBuffer, 0, 0, 81, 14, 0, 0, 81, 14);
		port2Image.copy(imageBuffer, 0, 15, 81, 12, 0, 0, 81, 12);
		
		// Write image to ports
		if (portsConnected) {
			port1.writeImageData(port1Image);
			port2.writeImageData(port2Image);
		}
		
	}
	
	/**
	 * Black out the led wall (all leds off)
	 */
	public void blackOut() {
		displayImage(blackOutImage);
	}
	
	/** 
	 * Retrieve buffer image for debugging purposes
	 * @return
	 */
	public PImage getBuffer() {
		return imageBuffer;
	}
	
	/**
	 * Retrieve led image for port 1 for debugging purposes
	 * @return PImage that is 81x16 pixels
	 */
	public PImage getPort1Image() {
		return port1Image;
	}
	
	/**
	 * Retrieve led image for port 2 for debugging purposes
	 * @return PImage that is 81x16 pixels
	 */
	public PImage getPort2Image() {
		return port2Image;
	}
	
}
