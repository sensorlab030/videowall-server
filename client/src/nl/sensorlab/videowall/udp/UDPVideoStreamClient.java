package nl.sensorlab.videowall.udp;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import com.cleverfranke.util.Settings;

import processing.core.PConstants;
import processing.core.PImage;

public class UDPVideoStreamClient implements Runnable {

	// Misc constants
	private static final int PORT_IN = 10233;								// Network in port
	private static final int IMAGE_TYPE = BufferedImage.TYPE_INT_ARGB;		// Buffered image type
	private static final String IPSERVER = Settings.getValue("ipServer"); 	// IP address of the server machine
	private static final int CAPTURE_WIDTH = 108;							// Aspect ratio (width) sent by the UDP video stream server
	private static final int CAPTURE_HEIGHT = 81;							// Aspect ratio (height) sent by the UDP video stream server


	// Misc
	private DatagramSocket inSocket;																				// Receive socket
	private volatile BufferedImage streamImage = new BufferedImage(CAPTURE_WIDTH, CAPTURE_HEIGHT, IMAGE_TYPE); 		// Stream image
	private Object streamImageLock = new Object();																	// Lock for stream image

	// Public status
	public boolean running = true;
	public Thread t;


	public UDPVideoStreamClient() {

		try {

			// Initialize receiving socket
			inSocket = new DatagramSocket(PORT_IN);
			inSocket.setSoTimeout(100);

			// Start deamon thread for listening
			t = new Thread(this);
			t.setDaemon(true);
			t.start();

		} catch (SocketException e) {
			e.printStackTrace();
		}

		// Check if IPSERVER has been provided
		if (IPSERVER.equals("")) {
			System.out.println("Please provide an IP address (ipServer) for the server in settings.json, and then restart the application!");
			running = false;
		} else {
			System.out.println("UDP Video Stream Client / Listening to port: " + inSocket.getLocalPort() + ". Expecting packets from IP : " + IPSERVER);
		}
	}



	/**
	 * Stop receiving packet and disconnect the socket
	 */
	public void stop() {

		running = false;
		inSocket.disconnect();
		inSocket.close();
		System.out.println("UDP Video Stream Client thread exited");
	}



	/**
	 * Synchronize streamImageLock to current streamImage
	 * @return streamImage
	 */
	private BufferedImage getStreamImage() {

		synchronized (streamImageLock) {
			return streamImage;
		}

	}



	/**
	 * Set ARGB value of pixels based on buffered image data
	 * ARGB image type
	 * @param tmpImage: Buffered image containing ARGB bytes data
	 */
	private void setARGBData(BufferedImage tmpImage, byte[] inBuffer) {
		int bufferIndex = 3;

		for (int y = 0; y < tmpImage.getHeight(); y++) {
			for (int x = 0; x < tmpImage.getWidth(); x++) {
				int alpha = inBuffer[bufferIndex++] & 0xff;
				int red = inBuffer[bufferIndex++] & 0xff;
				int green = inBuffer[bufferIndex++] & 0xff;
				int blue = inBuffer[bufferIndex++] & 0xff;

				tmpImage.setRGB(x, y, (alpha << 24) + (red << 16) + (green << 8) + blue);
			}
		}
	}


	/**
	 * Receives image datagram packet and updates the streamImage to new image
	 */
	private void receivePacket() {
		byte[] inBuffer = new byte[65508];			// In buffer (max 65508b)

		try {

			// Receive packet header
			DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
			inSocket.receive(packet);

			// Check origin of the packet
			String originAddress = packet.getAddress().toString();

			if (!originAddress.equals("/" + IPSERVER)) {
				System.out.println("Received a packet from another IP than " + IPSERVER);
				return;
			}

			// Extract header and update index
			String header = new String(Arrays.copyOfRange(inBuffer, 0, 3));

			// Parse content
			if (header.equals("IMG")) { // Image packet

				// Create buffered image
				BufferedImage tmpImage = new BufferedImage(CAPTURE_WIDTH, CAPTURE_HEIGHT, IMAGE_TYPE);

				// Update buffered image with RGB image data
				setARGBData(tmpImage, inBuffer);

				// Replace image with tmp image
				synchronized (streamImageLock) {
					streamImage = tmpImage;
				}

			} else {
				System.err.println("Invalid packet received (invalid header)");
			}

		} catch (SocketTimeoutException e) {
			// Doesn't matter, had no RX
		} catch (SocketException e) {
			System.err.println (e.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Transform a Buffered image data to a Pimage
	 * @param bimg, buffered image
	 */
    private PImage bufferedImageToPImage(BufferedImage bimg) {
    	PImage frame = new PImage(CAPTURE_WIDTH, CAPTURE_HEIGHT, PConstants.ARGB);

		try {

			// Get buffered image as Pimage
			bimg.getRGB(0, 0, frame.width, frame.height, frame.pixels, 0, frame.width);

		}
		catch(Exception e) {

			System.err.println("Can't create image from buffer");
		    e.printStackTrace();

		}

		return frame;
    }


    /**
     * Receive image packet, get the stream image and convert as PImage
     * @return
     */
    public PImage getImage() {

		receivePacket();
		BufferedImage buffImage = getStreamImage();

		return bufferedImageToPImage(buffImage);
    }


	@Override
	public void run() {}
}
