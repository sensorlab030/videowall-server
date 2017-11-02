package com.cleverfranke.ledwall.animation;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.video.*;

/**
 * Projects each frame of the video on a the pixel grid
 */
public class VideoStream extends CanvasAnimation {
	Movie myMovie;

	public VideoStream(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, applet);
		// Fetch movie
		/** Feed with a movie **/
		myMovie = new Movie(applet, "./src/data/test.mov");

	}


	@Override
	public boolean isDone() {
		return (myMovie.time() == myMovie.duration());
	}


	@Override
	public void drawAnimationFrame(PGraphics g) {
		if (myMovie.available()) {
			// Read current frame
		    myMovie.read();
		    g.image(myMovie, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
		}
	}


	@Override
	public void prepareForQueueRotation() {
		// Go to movie beginning and mute sound
		myMovie.jump(0);
		myMovie.playbin.setVolume(0);
		myMovie.play();
	}

}
