package nl.sensorlab.videowall.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;

import processing.core.PConstants;
import processing.core.PImage;

public class UDPVideoStreamClient implements Runnable {
	
	private static UDPVideoStreamClient instance;

	// Misc constants
	private static final int PORT_IN = 10233; // Network in port
	private static final int STREAM_IMAGE_WIDTH = 280; // Expected height of the video stream image
	private static final int STREAM_IMAGE_HEIGHT = 76; // Expected width of the video stream image

	/**
	 * Length of the data buffer calculated as number of pixels * 3 (3 bytes for 3
	 * colors, RGB) + 3 bytes of the IMG start of the packet
	 */
	private final static int BUFFER_LENGTH = STREAM_IMAGE_WIDTH * STREAM_IMAGE_HEIGHT * 3 + 3;

	// Misc
	private DatagramSocket inSocket; // Receive socket
	private Object streamImageLock = new Object(); // Lock for stream image

	private InetAddress expectedSender;

	/**
	 * The image we save the stream to
	 */
	private PImage streamImage;

	/**
	 * Network buffer for incoming packets
	 */
	private byte[] buffer;

	/**
	 * Thread running flag
	 */
	private boolean running = false;

	/**
	 * Thread handle
	 */
	private Thread t;
	
	public static UDPVideoStreamClient getInstance() {
		if (instance == null) {
			instance = new UDPVideoStreamClient();
		}
		return instance;
	}

	private UDPVideoStreamClient() {
		streamImage = new PImage(STREAM_IMAGE_WIDTH, STREAM_IMAGE_HEIGHT, PConstants.ARGB);
		buffer = new byte[BUFFER_LENGTH];
	}

	/**
	 * Stop receiving packet and disconnect the socket
	 */
	public void stop() {
		running = false;
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set expected sender host
	 * 
	 * @param senderHost
	 */
	public void setExpectedSender(String senderHost) {

		// Set expected host
		try {
			expectedSender = InetAddress.getByName(senderHost);
		} catch (UnknownHostException e) {
			expectedSender = null;
			System.err.println("Invalid expected sender address: '" + senderHost + "'");
		}

	}

	public void start() {

		try {
			
			// Check for valid expected sender
			if (expectedSender == null) {
				throw new Exception("No valid expected sender set");
			}

			// Initialize receiving socket
			inSocket = new DatagramSocket(PORT_IN);
			inSocket.setSoTimeout(100);

			// Start deamon thread for listening
			t = new Thread(this);
			t.setDaemon(true);
			t.start();

			// Enable client
			running = true;
			System.out.println("UDP Video Stream Client / Listening to port: " + PORT_IN
					+ ". Expecting packets from IP : " + expectedSender.getHostAddress());

		} catch (Exception e) {
			System.err.println("Error starting UDP client: " + e.getMessage());
		}

	}

	/**
	 * Get the last received stream image
	 * 
	 * @return
	 */
	public PImage getImage() {
		synchronized (streamImageLock) {
			return streamImage;
		}
	}

	@Override
	public void run() {

		// Continuously receive image datagram packets and update the streamImage to new
		// image
		while (running && !t.isInterrupted()) {

			try {

				// Receive packet on socket
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				inSocket.receive(packet);
				
				// Confirm sender
				if (!packet.getAddress().equals(expectedSender)) {
					throw new Exception("Invalid sender: " + packet.getAddress().getHostAddress());
				}

				// Confirm expected packet size
				if (packet.getLength() != BUFFER_LENGTH) {
					throw new Exception("Invalid packet size");
				}

				// Confirm header
				String header = new String(Arrays.copyOfRange(buffer, 0, 3));
				if (!header.equals("IMG")) {
					throw new Exception("Invalid packet header");
				}

				// Update buffered image with RGB image data and replace image with tmp image
				int bufferIndex = 3;
				synchronized (streamImageLock) {
					for (int i = 0; i < streamImage.pixels.length; i++) {
						// int alpha = inBuffer[bufferIndex++] & 0xff; // We don't send alpha values to
						// save bandwidth)
						int alpha = 255 & 0xff;
						int red = buffer[bufferIndex++] & 0xff;
						int green = buffer[bufferIndex++] & 0xff;
						int blue = buffer[bufferIndex++] & 0xff;

						streamImage.pixels[i] = (alpha << 24) + (red << 16) + (green << 8) + blue;

					}
					streamImage.updatePixels();
				}

			} catch (SocketTimeoutException e) {
				// Doesn't matter, had no RX
			} catch (SocketException e) {
				System.err.println(e.toString());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("Ignoring packet: " + e.getMessage());
			}

		}

		inSocket.disconnect();
		inSocket.close();

		System.out.println("UPD Video Stream Client stopped");

	}
}
