package com.cleverfranke.ledwall.walldriver;

import processing.core.PApplet;
import processing.core.PImage;
import processing.serial.Serial;

public class WallDriverPort {
	
	public final static int PANEL_IMAGE_WIDTH = 81;
	public final static int PANEL_IMAGE_HEIGHT = 16;
	public final static int FRAMERATE = 60;
	private final static float GAMMA = 1.7f;
	
	private boolean master;
	private Serial serialPort;
	
	private int[] gammaTable = new int[256];
	
	/**
	 * Setup port
	 * 
	 * @param applet
	 * @param portName
	 * @param isMaster
	 * @throws Exception 
	 */
	public WallDriverPort(PApplet applet, String portName, boolean isMaster) throws Exception {
		
		serialPort = new Serial(applet, portName);
		if (serialPort == null) {
			throw new Exception("Failed to open serial port " + portName);
		}
		
		master = isMaster;
		
		// Initialize gamma table
		for (int i = 0; i < 256; i++) {
			gammaTable[i] = (int) (Math.pow((float) i / 255.0, GAMMA) * 255.0 + 0.5);
		}
		
	}
	
	/**
	 * Write image to serial port
	 * @param image
	 */
	public void writeImageData(PImage image) {
		
		// Create data frame
		byte[] data = new byte[(PANEL_IMAGE_WIDTH * PANEL_IMAGE_HEIGHT * 3) + 3];
		int offset = 0;
		
		// Data frame header
		if (master) {
			int usec = (int) ((1000000.0 / FRAMERATE) * 0.75);
			data[offset++] = '*';
			data[offset++] = (byte) (usec); // request the frame sync pulse
			data[offset++] = (byte) (usec >> 8); // at 75% of the frame time
		} else {
			data[offset++] = '%';
			data[offset++] = 0;
			data[offset++] = 0;
		}
		
		// Add image data to data frame
		int x, y, xbegin, xend, xinc, mask;
		int linesPerPin = image.height / 8;
		int pixel[] = new int[8];

		for (y = 0; y < linesPerPin; y++) {
			if ((y & 1) == 0) {
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
		
		// Write to serial port
		serialPort.write(data);
		
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
	
}
