package nl.sensorlab.videowall.animation;

import java.awt.image.BufferedImage;

import nl.sensorlab.videowall.udp.UDPVideoStreamClient;

import com.cleverfranke.util.Settings;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Animation class that allows for drawing on a normal canvas, that mimicks the actual wall, abstracting away
 * all the pixel mapping and the non-regular grid. This is the most convenient class to subclass when
 * making a 'raster' based animation.
 */
public class VideoStream extends BaseAnimation {

	// Images and stream
	UDPVideoStreamClient udpClient;
	BufferedImage buffImage;
	PImage frame;

	// Constants
	static final int CAPTURE_WIDTH = PIXEL_RESOLUTION_X;		// Capture width (256x192 is max UDP can easily handle)
	static final int CAPTURE_HEIGHT = PIXEL_RESOLUTION_Y;		// Capture height
	static final String IPCLIENT = Settings.getValue("IPCLIENT"); // IP address of the client machine
	static final float brightnessFac = Float.parseFloat(Settings.getValue("videoStreamBrightnessBoost"));
	static final float saturationFac = Float.parseFloat(Settings.getValue("videoStreamSaturationBoost"));

	public VideoStream(PApplet applet) {
		super(applet);

		// Create frame placeholder and init UDP Video Stream Client
		frame = new PImage(CAPTURE_WIDTH, CAPTURE_HEIGHT, PConstants.ARGB);
		udpClient = new UDPVideoStreamClient(IPCLIENT, CAPTURE_WIDTH, CAPTURE_HEIGHT, brightnessFac, saturationFac);
	}

	@Override
	protected final void drawAnimationFrame(PGraphics g) {
		g.background(0);

		if (udpClient.running && !Thread.currentThread().isInterrupted()) {

			// Receive image packet, get the stream image and convert as PImage
			udpClient.receivePacket();
			buffImage = udpClient.getStreamImage();
			getAsPImage(buffImage);

		} else {

			// Stop UDP Client and close socket
			udpClient.stop();
			udpClient.inSocket.close();

			System.out.println("UDP Video Stream Client thread exited");
		}

		// Draw image on canvas
		g.image(frame, 0, 0);
	}

	/**
	 * Transform a Buffered image data to a Pimage
	 * @param bimg, buffered image
	 */
    public void getAsPImage(BufferedImage bimg) {

      try {

    	// Get buffered image as Pimage
        bimg.getRGB(0, 0, frame.width, frame.height, frame.pixels, 0, frame.width);
        // Update pixels of current frame
        frame.updatePixels();

      }
      catch(Exception e) {

        System.err.println("Can't create image from buffer");
        e.printStackTrace();

      }
    }


}
