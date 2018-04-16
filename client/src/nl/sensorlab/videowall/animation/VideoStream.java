package nl.sensorlab.videowall.animation;

import java.awt.Rectangle;

import nl.sensorlab.videowall.udp.UDPVideoStreamClient;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Animation class that starts a UDP Client of video streams and display the received images on the wall
 */
public class VideoStream extends BaseCanvasAnimation {

	// Images and stream
	private UDPVideoStreamClient udpClient;
	private PImage frame;
	private Rectangle canvasGeometry;


	public VideoStream(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);

		// Get canvas geometry for future resize
		canvasGeometry = getGeometry();

		udpClient = new UDPVideoStreamClient();
	}



	@Override
	protected final void drawCanvasAnimationFrame(PGraphics g, double dt) {

		g.background(0);

		// Get image from stream
		frame = udpClient.getImage();
		frame.resize(canvasGeometry.width, canvasGeometry.height);

		// Draw image on canvas
		g.image(frame, 0, 0);
	}


	/**
	 *  Init UDP Video Stream Client
	 */
	@Override
	public void isStarting() {
		udpClient.start();
	}



	/**
	 *  Stop UDP Video Stream Client
	 */
	@Override
	public void isStopping() {
		udpClient.stop();
	}
}
