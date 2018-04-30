package nl.sensorlab.videowall.walldriver;

import java.util.Arrays;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import processing.core.PApplet;
import processing.core.PImage;

public class WallDriverPort {

	/**
	 * The width of the 'screen' this port is driving (also the with of the supplied
	 * image);
	 */
	public final static int PORT_IMAGE_WIDTH = 81;

	/**
	 * The height of the 'screen' this port is driving (also the with of the
	 * supplied image);
	 */
	public final static int PORT_IMAGE_HEIGHT = 16;

	/**
	 * Gamma adjustment value for the leds
	 */
	private final static float GAMMA = 1.7f;

	/**
	 * How often the keepAlive function should check for a connected cable (in
	 * milliseconds)
	 */
	private final static int KEEPALIVE_INTERVAL = 1000;

	/**
	 * The system name of the port (e.g. COM3, /dev/ttyS0). If this value
	 * is null it means that there's no expected port to be connected (e.g.
	 * on a development machine)
	 */
	private String portName;

	/**
	 * Connection to the port to read/write from and to
	 */
	private SerialPort port;

	/**
	 * 
	 */
	private byte[] dataframeHeader;

	/**
	 * Gamma adjustment table for the leds
	 */
	private int[] gammaTable = new int[256];

	/**
	 * Last time the keepAlive check was done (System milliseconds)
	 */
	private long lastKeepAliveTime = 0;

	/**
	 * Setup port
	 * 
	 * @param applet
	 * @param portName
	 * @param isMaster
	 * @throws Exception
	 */
	public WallDriverPort(PApplet applet, String portName, int framerate, boolean isMaster) {

		this.portName = !portName.isEmpty() ? portName : null;
		this.port = new SerialPort(portName);

		// Initialize data frame header
		if (isMaster) {
			int usec = (int) ((1000000.0 / framerate) * 0.75);
			dataframeHeader = new byte[] { '*', ((byte) (usec)), ((byte) (usec >> 8)) };
		} else {
			dataframeHeader = new byte[] { '%', 0, 0 };
		}

		// Initialize gamma table @TODO: make static
		for (int i = 0; i < 256; i++) {
			gammaTable[i] = (int) (Math.pow((float) i / 255.0, GAMMA) * 255.0 + 0.5);
		}

	}

	/**
	 * Write image to serial port
	 * 
	 * @param image
	 */
	public void writeImageData(PImage image) {

		if (!port.isOpened()) {
			return;
		}

		// Create data frame
		byte[] data = new byte[(PORT_IMAGE_WIDTH * PORT_IMAGE_HEIGHT * 3) + 3];
		int offset = 0;

		// Add data frame header
		for (int i = 0; i < dataframeHeader.length; i++) {
			data[offset++] = dataframeHeader[i];
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
		try {
			port.writeBytes(data);
		} catch (SerialPortException e) {
			System.err.println("Failed to write to port " + portName + ": " + e.getMessage());
		}

		// Perform keepalive check
		long currentTime = System.currentTimeMillis();
		if (currentTime > lastKeepAliveTime + KEEPALIVE_INTERVAL) {
			keepAlive();
			lastKeepAliveTime = currentTime;
		}

	}

	/**
	 * Attempt to open the serial port for communication
	 * 
	 * @return true on successfully opening the port
	 */
	public void openPort() {
		
		// Skip if no port name is supplied
		if (portName == null) {
			System.out.println("No port supplied, will not try to connect");
			return;
		}
		
		try {

			// Close port if flagged as open
			if (port.isOpened()) {
				System.out.println("Closing port " + portName);
				port.closePort();
			}

			// Open the port
			System.out.println("Opening port " + portName);
			port.openPort();

		} catch (SerialPortException e) {
			System.err.println("Failed to connect to port " + portName + ": " + e.getMessage());
		}
	}

	/**
	 * Check for open connection; if not connected, attempt to reconnect
	 */
	private void keepAlive() {
		
		System.out.println("Keepalive on port " + portName);
		boolean portExists = Arrays.stream(SerialPortList.getPortNames()).anyMatch(str -> str.trim().equals(portName));
		if (!portExists) {
			System.err.println("Cable unplugged for port " + portName);
			openPort();
		}
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
