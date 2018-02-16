package nl.sensorlab.videowall.animation;

import java.awt.image.BufferedImage;

import nl.sensorlab.videowall.udp.UDPVideoStreamClient;

import com.cleverfranke.util.Settings;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Animation class that starts a UDP Client of video streams and display the received images on the wall
 */
public class VideoStream extends BaseAnimation {

	// Constants
	private final float BRIGHTNESS_FACTOR = Float.parseFloat(Settings.getValue("videoStreamBrightnessBoost"));
	private final float SATURATION_FACTOR = Float.parseFloat(Settings.getValue("videoStreamSaturationBoost"));

	// Images and stream
	private UDPVideoStreamClient udpClient;
	private BufferedImage buffImage;
	private PImage frame;



	public VideoStream(PApplet applet) {
		super(applet);

		// Create frame placeholder and init UDP Video Stream Client
		frame = new PImage(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y, PConstants.ARGB);
		udpClient = new UDPVideoStreamClient(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y, BRIGHTNESS_FACTOR, SATURATION_FACTOR);
	}



	@Override
	protected final void drawAnimationFrame(PGraphics g) {
		g.background(0);

		if (udpClient.running && !Thread.currentThread().isInterrupted()) {

			// Receive image packet, get the stream image and convert as PImage
			udpClient.receivePacket();
			buffImage = udpClient.getStreamImage();
			frame = udpClient.bufferedImageToPImage(buffImage);

		} else {
			// Stop UDP Client
			udpClient.stop();
		}

		// Draw image on canvas
		g.image(frame, 0, 0);
	}
}
