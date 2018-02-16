package nl.sensorlab.videowall.animation;

import nl.sensorlab.videowall.udp.UDPVideoStreamClient;

import java.awt.Rectangle;

import com.cleverfranke.util.Settings;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Animation class that starts a UDP Client of video streams and display the received images on the wall
 */
public class VideoStream extends BaseCanvasAnimation {

	// Constants
	private final float BRIGHTNESS_FACTOR = Float.parseFloat(Settings.getValue("videoStreamBrightnessBoost"));
	private final float SATURATION_FACTOR = Float.parseFloat(Settings.getValue("videoStreamSaturationBoost"));
	private static final String IPSERVER = Settings.getValue("IPSERVER"); // IP address of the server machine

	// Aspect ratio sent by the UDP video stream server
	private final int CAPTURE_WIDTH = 108;
	private final int CAPTURE_HEIGHT = 81;

	// Images and stream
	private UDPVideoStreamClient udpClient;
	private PImage frame;
	private Rectangle canvasGeometry;


	public VideoStream(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);

		// Get canvas geometry for future resize
		canvasGeometry = getGeometry();

		// Init UDP Video Stream Client
		udpClient = new UDPVideoStreamClient(CAPTURE_WIDTH, CAPTURE_HEIGHT, BRIGHTNESS_FACTOR, SATURATION_FACTOR, IPSERVER);
	}



	@Override
	protected final void drawCanvasAnimationFrame(PGraphics g) {
		g.background(0);

		// Create placeholder image
		frame = new PImage(CAPTURE_WIDTH, CAPTURE_HEIGHT, PConstants.ARGB);

		if (udpClient.running && !Thread.currentThread().isInterrupted()) {

			// Get image from stream
			frame = udpClient.getImage();
			frame.resize(canvasGeometry.width, canvasGeometry.height);

		} else {

			// Stop UDP Client
			udpClient.stop();

		}

		// Draw image on canvas
		g.image(frame, 0, 0);
	}
}
