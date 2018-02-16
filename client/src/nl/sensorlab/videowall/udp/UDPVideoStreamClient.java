package nl.sensorlab.videowall.udp;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import com.cleverfranke.util.PColor;

public class UDPVideoStreamClient implements Runnable {

	// Misc constants
	private static final int PORT_IN = 10233;							// Network in port
	private static final int imageType = BufferedImage.TYPE_INT_ARGB;	// Buffered image type
	private int capture_width, capture_height;							// Dimensions of received image
	private float brigthnessFac, saturationFac;							// Factors to boost brightness and saturation

	// Misc
	private DatagramSocket inSocket;					// Receive socket
	private byte[] inBuffer = new byte[65508];			// In buffer (max 65508b)
	private volatile BufferedImage streamImage; 		// Stream image
	private Object streamImageLock = new Object();		// Lock for stream image
	public boolean running = true;						// Looping while true


	public UDPVideoStreamClient(int captureWidth, int captureHeight, float brigthnessFac, float saturationFac) {
		this.capture_width = captureWidth;
		this.capture_height = captureHeight;
		this.brigthnessFac = brigthnessFac;
		this.saturationFac = saturationFac;

		try {

			// Initialize receiving socket
			inSocket = new DatagramSocket(PORT_IN);
			inSocket.setSoTimeout(100);

			System.out.println("UDP Video Stream Client / Listening to port:" + inSocket.getLocalPort());

			// Initialize image
			streamImage = new BufferedImage(capture_width, capture_height, imageType);

			// Start deamon thread for listening
			Thread t = new Thread(this);
			t.setDaemon(true);
			t.start();

		} catch (SocketException e) {
			e.printStackTrace();
			System.exit (1);
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
	public BufferedImage getStreamImage() {

		synchronized (streamImageLock) {
			return streamImage;
		}

	}



	/**
	 * Set ARGB value of pixels based on buffered image data
	 * ARGB image type
	 * @param tmpImage: Buffered image containing ARGB bytes data
	 */
	public void setARGBData(BufferedImage tmpImage) {
		int bufferIndex = 3;

		for (int y = 0; y < tmpImage.getHeight(); y++) {
			for (int x = 0; x < tmpImage.getWidth(); x++) {
				int alpha = inBuffer[bufferIndex++] & 0xff;
				int red = inBuffer[bufferIndex++] & 0xff;
				int green = inBuffer[bufferIndex++] & 0xff;
				int blue = inBuffer[bufferIndex++] & 0xff;


				tmpImage.setRGB(x, y, boostColors(alpha, red, green, blue));
//				tmpImage.setRGB(x, y, (alpha << 24) + (red << 16) + (green << 8) + blue);
			}
		}
	}


	/**
	 * Takes ARGB values, convert them to HSB format, multiply brightness and saturation values
	 * by the constants brigthnessFac and saturationFac which are set in settings.json
	 * and return an ARGB int value
	 *
	 * @param alpha
	 * @param red
	 * @param green
	 * @param blue
	 * @return int hex code in ARGB
	 */
	public int boostColors(int alpha, int red, int green, int blue) {
		float[] hsbColors = null;
		int[] rgbColor = null;

		hsbColors = PColor.RGBtoHSB(red, green, blue, null);
		hsbColors[1] = (hsbColors[1] *  brigthnessFac > 1) ? 1 : hsbColors[1] * brigthnessFac;
		hsbColors[2] = (hsbColors[2] * saturationFac > 1) ? 1 : hsbColors[2] * saturationFac;

		rgbColor = PColor.HSBtoRGB(hsbColors[0], hsbColors[1], hsbColors[2]);

		int RGB = alpha;
		RGB = (RGB << 8) + rgbColor[0];
		RGB = (RGB << 8) + rgbColor[1];
		RGB = (RGB << 8) + rgbColor[2];

		return RGB;
	}


	/**
	 * Receives image datagram packet and updates the streamImage to new image
	 */
	public void receivePacket() {
		try {

			// Receive packet header
			DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
			inSocket.receive(packet);

			// Extract header and update index
			String header = new String(Arrays.copyOfRange(inBuffer, 0, 3));

			// Parse content
			if (header.equals("IMG")) { // Image packet

				// Create buffered image
				BufferedImage tmpImage = new BufferedImage(capture_width, capture_height, imageType);

				// Update buffered image with RGB image data
				setARGBData(tmpImage);

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

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
}
