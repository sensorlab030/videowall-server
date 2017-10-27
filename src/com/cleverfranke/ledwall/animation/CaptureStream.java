package com.cleverfranke.ledwall.animation;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.video.*;

/**
 * Projects each frame of the video on a the pixel grid
 */
public class CaptureStream extends CanvasAnimation {
	Capture cam;


	public CaptureStream(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, applet);
		String[] cameras = Capture.list();

		if (cameras.length == 0) {
		    System.out.println("There are no cameras available for capture.");
		  } else {
			 cam = new Capture(applet, cameras[0]);
			 cam.start();
		}
	}


	@Override
	public boolean isDone() {
		return false;
	}


	@Override
	public void drawAnimationFrame(PGraphics g) {
		if (cam.available() == true) {
		    cam.read();
		}
		g.image(cam, 0, 0);
	}


	@Override
	public void prepareForQueueRotation() {

	}

}
