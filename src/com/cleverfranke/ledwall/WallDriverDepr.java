package com.cleverfranke.ledwall;

import java.awt.Rectangle;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.serial.*;

/**
 * @deprecated
 */
public class WallDriverDepr {

	private final static int FRAMERATE = 30;
	private final static float GAMMA = 1.7f;
	
	private final static int PORT_COUNT = 2;
	private final static int PANEL_IMAGE_WIDTH = 81;
	private final static int PANEL_IMAGE_HEIGHT = 16;

	private PApplet applet;
	
	// Port members
	private int portCount; 						// Port count
	private Serial[] serialPorts; 				// The ports
	private Rectangle[] ledArea; 				// The area of the movie each port gets, in % (0-100)
	private PImage[] ledImage; 					// Image for each port
	private boolean[] ledLayout; 				// Layout of rows, true = even is left->right
	private boolean allPortsInitialized = false;	// Whether all serial ports are initialized

	// Gamma table
	int[] gammaTable = new int[256];

	public WallDriverDepr(PApplet applet) {
		this.applet = applet;

		// Initialize gamma table
		for (int i = 0; i < 256; i++) {
			gammaTable[i] = (int) (Math.pow((float) i / 255.0, GAMMA) * 255.0 + 0.5);
		}

	}

	/**
	 * Initialize the wall driver
	 * 
	 * @param ports
	 * @return true on success
	 */
	public boolean initialize(String ports[]) {

		portCount = ports.length;
		serialPorts = new Serial[portCount];
		ledArea = new Rectangle[portCount];
		ledImage = new PImage[portCount];
		ledLayout = new boolean[portCount];

		// Configure ports
		allPortsInitialized = true;
		for (int i = 0; i < portCount; i++) {
			allPortsInitialized = allPortsInitialized && configureSerialPort(i, ports[i]);
		}

		return allPortsInitialized;
	}
	
	/**
	 * Push this image to the display
	 * 
	 * @param image
	 */
	public void displayImage(PImage image) {
		
		if (!allPortsInitialized) {
			System.err.println("Ports are not initialized");
			return;
		}

		for (int i = 0; i < portCount; i++) {

			// copy a portion of the movie's image to the LED image
			int xoffset = percentage(image.width, ledArea[i].x);
			int yoffset = percentage(image.height, ledArea[i].y);
			int xwidth = percentage(image.width, ledArea[i].width);
			int yheight = percentage(image.height, ledArea[i].height);

			ledImage[i].copy(image, xoffset, yoffset, xwidth, yheight, 0, 0, ledImage[i].width, ledImage[i].height);
			// convert the LED image to raw data
			byte[] ledData = new byte[(ledImage[i].width * ledImage[i].height * 3) + 3];

			image2data(ledImage[i], ledData, ledLayout[i]);
			if (i == 0) {
				ledData[0] = '*'; // first Teensy is the frame sync master
				int usec = (int) ((1000000.0 / FRAMERATE) * 0.75);
				ledData[1] = (byte) (usec); // request the frame sync pulse
				ledData[2] = (byte) (usec >> 8); // at 75% of the frame time
			} else {
				ledData[0] = '%'; // others sync to the master board
				ledData[1] = 0;
				ledData[2] = 0;
			}

			// Send the raw data to the LEDs
			serialPorts[i].write(ledData);

		}
	}

	/**
	 * This method converts an image to OctoWS2811's raw data format. The number of
	 * vertical pixels in the image must be a multiple of 8. The data array must be
	 * the proper size for the image.
	 * 
	 * @param image
	 * @param data
	 * @param layout
	 */
	private void image2data(PImage image, byte[] data, boolean layout) {
		int offset = 3;
		int x, y, xbegin, xend, xinc, mask;
		int linesPerPin = image.height / 8;
		int pixel[] = new int[8];

		for (y = 0; y < linesPerPin; y++) {
			if ((y & 1) == (layout ? 0 : 1)) {
				// even numbered rows are left to right
				xbegin = 0;
				xend = image.width;
				xinc = 1;
			} else {
				// odd numbered rows are right to left
				xbegin = image.width - 1;
				xend = -1;
				xinc = -1;
			}
			for (x = xbegin; x != xend; x += xinc) {
				for (int i = 0; i < 8; i++) {
					// fetch 8 pixels from the image, 1 for each pin
					pixel[i] = image.pixels[x + (y + linesPerPin * i) * image.width];
					pixel[i] = colorWiring(pixel[i]);
				}
				// convert 8 pixels to 24 bytes
				for (mask = 0x800000; mask != 0; mask >>= 1) {
					byte b = 0;
					for (int i = 0; i < 8; i++) {
						if ((pixel[i] & mask) != 0)
							b |= (1 << i);
					}
					data[offset++] = b;
				}
			}
		}
	}

	// scale a number by a percentage, from 0 to 100
	int percentage(int num, int percent) {
		double mult = percentageFloat(percent);
		double output = num * mult;
		return (int) output;
	}

	// scale a number by the inverse of a percentage, from 0 to 100
	int percentageInverse(int num, int percent) {
		double div = percentageFloat(percent);
		double output = num / div;
		return (int) output;
	}

	// convert an integer from 0 to 100 to a float percentage
	// from 0.0 to 1.0. Special cases for 1/3, 1/6, 1/7, etc
	// are handled automatically to fix integer rounding.
	double percentageFloat(int percent) {
		if (percent == 33)
			return 1.0 / 3.0;
		if (percent == 17)
			return 1.0 / 6.0;
		if (percent == 14)
			return 1.0 / 7.0;
		if (percent == 13)
			return 1.0 / 8.0;
		if (percent == 11)
			return 1.0 / 9.0;
		if (percent == 9)
			return 1.0 / 11.0;
		if (percent == 8)
			return 1.0 / 12.0;
		return (double) percent / 100.0;
	}

	/**
	 * Translate the 24 bit color from RGB to the actual order used by the LED
	 * wiring. GRB is the most common.
	 * 
	 * @param c
	 * @return
	 */
	private int colorWiring(int c) {
		int red = (c & 0xFF0000) >> 16;
		int green = (c & 0x00FF00) >> 8;
		int blue = (c & 0x0000FF);
		red = gammaTable[red];
		green = gammaTable[green];
		blue = gammaTable[blue];
		return (green << 16) | (red << 8) | (blue); // GRB - most common wiring
	}

	/**
	 * Configure one serial port with given index and portname
	 * 
	 * @param portIndex
	 * @param portName
	 * @return true on successful configuration
	 */
	private boolean configureSerialPort(int portIndex, String portName) {

		try {

			// Connect to the port
			serialPorts[portIndex] = new Serial(applet, portName);
			if (serialPorts[portIndex] == null) {
				throw new Exception("Failed to open port " + portName);
			}

			// Query the Teensy for the configuration
			serialPorts[portIndex].write("?");
			applet.delay(250);

			// Fetch config response from the Teensy
			String line = serialPorts[portIndex].readStringUntil(10);
			if (line == null) {
				throw new Exception("Failed to query configuration on port " + portName);
			}
			
			/**
			 * 0 => Led width
			 * 1 => led height
			 * 2 => led layout
			 * 5 => video x offset (percent)
			 * 6 => video 7 offset (percent)
			 * 7 => video width (percent)
			 * 8 => video height (percent)
			 */
			
			// Extract configuration parameters
			String param[] = line.split(",");
			if (param.length != 12) {
				throw new Exception("Failed to query configuration parameters on port" + portName);
			}
			
			// Setup port related members
			ledArea[portIndex] = new Rectangle(
					Integer.parseInt(param[5]), 
					Integer.parseInt(param[6]),
					Integer.parseInt(param[7]), 
					Integer.parseInt(param[8]));
			ledImage[portIndex] = new PImage(Integer.parseInt(param[0]), Integer.parseInt(param[1]), PConstants.RGB);
			ledLayout[portIndex] = (Integer.parseInt(param[2]) == 0);
			
			return true;

		} catch (Exception e) {
			System.err.println(e);
			System.err.println("Error connecting port " + e.getMessage());
			return false;
		}

	}

	public static void printPortList() {
		for (String port : Serial.list()) {
			System.out.println(port);
		}
	}

}
