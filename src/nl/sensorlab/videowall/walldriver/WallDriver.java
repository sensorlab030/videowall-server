package nl.sensorlab.videowall.walldriver;

import nl.sensorlab.videowall.property.IntProperty;
import nl.sensorlab.videowall.property.Property;
import nl.sensorlab.videowall.property.Property.PropertyValueListener;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class WallDriver implements PropertyValueListener {

	private WallDriverPort port1, port2; // Wall ports
	private PGraphics imageBufferContext; // Buffer context used to rotate the incoming image
	private PImage imageBuffer; // Buffer image used by imageBufferContext
	private PImage port1Image, port2Image; // Separate images to be sent to the two UC's driving the wall
	private boolean blackoutEnabled = false; // Flag to enable/disable blackout
	private int dimming = 0; // Dimming [0, 255] 0 = full brightness
	
	private IntProperty brightness;

	/**
	 * Initialize wall driver
	 * 
	 * @param applet
	 * @param portName1
	 * @param portName2
	 */
	public WallDriver(PApplet applet, String portName1, String portName2, int framerate) {
		
		// Setup properties
		brightness = IntProperty.wallProperty("brightness");
		brightness.setValue(255);
		brightness.addValueListener(this);

		// Setup ports
		port1 = new WallDriverPort(applet, portName1, framerate, true);
		port2 = new WallDriverPort(applet, portName2, framerate, false);

		// Create buffer
		imageBufferContext = applet.createGraphics(81, 26);
		imageBuffer = applet.createImage(81, 26, PConstants.RGB);

		// Create port images
		port1Image = applet.createImage(WallDriverPort.PORT_IMAGE_WIDTH, WallDriverPort.PORT_IMAGE_HEIGHT,
				PConstants.RGB);
		port2Image = applet.createImage(WallDriverPort.PORT_IMAGE_WIDTH, WallDriverPort.PORT_IMAGE_HEIGHT,
				PConstants.RGB);

		// Open ports
		port1.openPort();
		port2.openPort();

	}

	/**
	 * Send an image to be displayed on the led wall. The image is expected to be
	 * 26px wide and 81px high in RGB. Brightness of the image will be controlled by
	 * the WallDriver.
	 * 
	 * @param sourceImage
	 */
	public void displayImage(PImage sourceImage) {
		
		// Check for valid image
		if (sourceImage == null) {
			System.err.println("No image");
			return;
		}
		if (sourceImage.width != 26 || sourceImage.height != 81) {
			System.err.println("Invalid image dimensions");
			return;
		}

		// Create image buffer image by rotating the image 90 degrees clockwise. Image
		// buffer
		// is thus 81px wide and 26px high
		imageBufferContext.beginDraw();
		imageBufferContext.translate(81, 0);
		imageBufferContext.rotate(PApplet.HALF_PI);

		if (blackoutEnabled) {

			// Paint it black
			imageBufferContext.background(0);

		} else {

			// Place source image
			imageBufferContext.image(sourceImage, 0, 0);

			// Apply brightness
			if (dimming > 0) {
				imageBufferContext.noStroke();
				imageBufferContext.fill(0, 0, 0, dimming);
				imageBufferContext.rect(0, 0, sourceImage.width, sourceImage.height);
			}
		}

		imageBufferContext.endDraw();
		imageBuffer = imageBufferContext.get();

		// Update led images by copying relevant parts of the images
		// to an image that is 81px wide and 16px high
		port1Image.copy(imageBuffer, 0, 0, 81, 14, 0, 0, 81, 14);
		port2Image.copy(imageBuffer, 0, 15, 81, 12, 0, 0, 81, 12);

		// Write image to ports
		port1.writeImageData(port1Image);
		port2.writeImageData(port2Image);

	}

	/**
	 * Black out the led wall (turn all leds off)
	 */
	public void setBlackOutEnabled(boolean blackoutEnabled) {
		this.blackoutEnabled = blackoutEnabled;
	}

	/**
	 * Adjust the brightness of the wall
	 * 
	 * @param brightness
	 *            Desired brightness [0, 255] is [off, max brightness]
	 */
	public void setBrightness(int brightness) {
		this.dimming = (255 - Math.max(Math.min(255, brightness), 0));

	}

	/**
	 * Retrieve buffer image for debugging purposes
	 * 
	 * @return
	 */
	public PImage getBuffer() {
		return imageBuffer;
	}

	/**
	 * Retrieve led image for port 1 for debugging purposes
	 * 
	 * @return PImage that is 81x16 pixels
	 */
	public PImage getPort1Image() {
		return port1Image;
	}

	/**
	 * Retrieve led image for port 2 for debugging purposes
	 * 
	 * @return PImage that is 81x16 pixels
	 */
	public PImage getPort2Image() {
		return port2Image;
	}

	@Override
	public void onPropertyChange(Property property) {
		if (property == brightness) {
			setBrightness(brightness.getValue());
		}
	}

}
