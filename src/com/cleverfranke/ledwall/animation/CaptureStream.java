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
		// Fetch movie
		/** Feed with a movie **/
		String[] cameras = Capture.list();
		System.out.println(cameras);
	}


	@Override
	public boolean isDone() {
		return false;
	}


	@Override
	public void drawAnimationFrame(PGraphics g) {

	}


	@Override
	public void prepareForQueueRotation() {

	}

}
