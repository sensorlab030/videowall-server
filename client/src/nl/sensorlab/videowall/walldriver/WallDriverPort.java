package nl.sensorlab.videowall.walldriver;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import processing.core.PApplet;
import processing.core.PImage;

public class WallDriverPort implements SerialPortEventListener {

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
	 * How many milliseconds the scheduled reconnect should be delayed after
	 * finding a live connection or attempting to reconnect. Effectively set this
	 * to how often (in ms) you want to attempt to reconnect
	 */
	private final static int SCHEDULED_RECONNECT_DELAY = 2000;

	/**
	 * The system name of the port (e.g. COM3, /dev/ttyS0). If this value
	 * is null it means that there's no expected port to be connected (e.g.
	 * on a development machine)
	 */
	private String portName;

	/**
	 * Whether this port is configured as master or slave (for video frame
	 * syncing)
	 */
	private boolean isMaster;
	
	/**
	 * Connection to the port to read/write from and to, if null it means 
	 * there's no expected port to be connected (e.g. on a development machine)
	 */
	private SerialPort port;
	
	/**
	 * Header of an image data frame
	 */
	private byte[] dataframeHeader;

	/**
	 * Gamma adjustment table for the leds
	 */
	private static int[] gammaTable = null;

	/**
	 * The time (System milliseconds) when the port should be reconnected. In normal operation
	 * this time is always postponed by RECONNECT_SCHEDULE_INTERVAL as a live connection is detected. If we don't
	 * detect a live connecting, we attempt to reconnect after RECONNECT_SCHEDULE_INTERVAL ms.
	 */
	private long scheduledReconnectTime;
	
	/**
	 * Setup port
	 * 
	 * @param applet
	 * @param portName
	 * @param isMaster
	 * @throws Exception
	 */
	public WallDriverPort(PApplet applet, String portName, int framerate, boolean isMaster) {

		if (!portName.isEmpty()) {
			this.portName = portName;
			port = new SerialPort(portName);
		}
		
		this.isMaster = isMaster;

		// Initialize data frame header
		if (isMaster) {
			int usec = (int) ((1000000.0 / framerate) * 0.75);
			dataframeHeader = new byte[] { '*', ((byte) (usec)), ((byte) (usec >> 8)) };
		} else {
			dataframeHeader = new byte[] { '%', 0, 0 };
		}

	}

	/**
	 * Write image to serial port
	 * 
	 * @param image
	 */
	public void writeImageData(PImage image) {
		
		// Skip if no port is enabled
		if (port == null) {
			return;
		}
		
		// Attempt to reconnect if we are past the scheduled reconnect time
		if (System.currentTimeMillis() >= scheduledReconnectTime) {
			reconnect();
		}
		
		// Process frame and send
		if (port.isOpened()) {
			
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
						pixel[i] = WallDriverPort.colorWiring(pixel[i]);
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
			
		}

	}

	/**
	 * Attempt to open the serial port for communication, if the port is already open, it will
	 * be closed before opening new port
	 * 
	 * @return true on successfully opening the port
	 */
	public void openPort() {
		
		// Skip if no port name is supplied
		if (port == null) {
			System.out.println("No serial port name supplied, will not try to connect");
			return;
		}
		
		try {

			// Close port if flagged as open
			if (port.isOpened()) {
				closePort();
			}

			// Open the port
			try {
				port.openPort();
			} catch (SerialPortException e) {
				throw new Exception("Failed to open port");
			}
			
			// Add event listener for events with which we can detect a live connection 
			try {
				port.addEventListener(this, SerialPort.MASK_RXCHAR + SerialPort.MASK_TXEMPTY);
			} catch (SerialPortException e) {
				throw new Exception("Failed to set event listener");
			}
			
			// As we are just connected, we can postpone the reconnect attempt
			postponeScheduledReconnect();
			
			System.out.println("Opened serial port " + getDisplayName());
			
		}catch (Exception e) {
			System.err.println("Exception on serial port " + getDisplayName() + ": " + e.getMessage());
		}
	}
	
	/**
	 * Attempt to close the port
	 */
	public void closePort() {
		if (port != null && port.isOpened()) {
			System.out.println("Closing serial port " + portName);	
			try {
				port.removeEventListener();
				port.closePort();
			} catch (SerialPortException e) {
				System.err.println("Failed to close serial port " + portName + ": " + e.getMessage());
			}
		}
	}
	
	
	/**
	 * Attempt to reconnect the port. Warning: This does a very rough disconnect by completely destroying 
	 * the port object and recreating it (workaround for a known bug)
	 */
	private void reconnect() {
		
		System.out.println("Port " + getDisplayName() + " appears disconnected, attempting to reconnect");
		
		// Attempt to close the port graceffully
		closePort();
		
		// Close the port the rough way (work-around as closing the port often throws unclear exceptions,
		// see: https://github.com/scream3r/java-simple-serial-connector/issues/107)
		port = null;
		port = new SerialPort(portName);
		
		// Attempt to open the port
		openPort();
		
		// Reset reconnect timer (otherwise we continuosly try to reopen the port)
		postponeScheduledReconnect();
		
	}
	
	/**
	 * Get the port name and whether the port is configured as master or slave (e.g. "COM7 (master)")
	 * 
	 * @return
	 */
	public String getDisplayName() {
		return portName + " (" + (isMaster ? "master" : "slave") + ")";
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		
		// We had contact with the serial port, so postpone the reconnect
		postponeScheduledReconnect();
	}
	
	/**
	 * Postpone a reconnect attempt, because we either just detected a live connection, or just
	 * attempted to connect
	 */
	private void postponeScheduledReconnect() {
		scheduledReconnectTime = System.currentTimeMillis() + SCHEDULED_RECONNECT_DELAY;
	}

	/**
	 * Translate the 24 bit color from RGB to the actual order used by the LED
	 * wiring. GRB is the most common.
	 * 
	 * @param c
	 * @return
	 */
	private static int colorWiring(int c) {
		
		// Initialize gamma table 
		if (gammaTable == null) {
			gammaTable = new int[256];
			for (int i = 0; i < 256; i++) {
				gammaTable[i] = (int) (Math.pow((float) i / 255.0, GAMMA) * 255.0 + 0.5);
			}
		}
		
		int red = (c & 0xFF0000) >> 16;
		int green = (c & 0x00FF00) >> 8;
		int blue = (c & 0x0000FF);
		
		
		red = gammaTable[red];
		green = gammaTable[green];
		blue = gammaTable[blue];
		
		return (green << 16) | (red << 8) | (blue); // GRB - most common wiring
	}

}
