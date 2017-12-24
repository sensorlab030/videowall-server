package com.cleverfranke.ledwall.animation;

import com.cleverfranke.util.FileSystem;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.video.Movie;

/**
 * Animation that displays a video file on the wall
 */
public class VideoAnimation extends BaseCanvasAnimation {
	
	private Movie movie;
	
	public VideoAnimation(PApplet applet) {
		super(applet);

		// Load image
		// @TODO make source movie an argument to the constructor
		movie = new Movie(applet, FileSystem.getApplicationPath("resources/sample.mp4"));
		movie.loop();
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		g.background(0);
		g.imageMode(PConstants.CENTER);
		g.translate(g.width / 2, g.height / 2);
		
		if (movie == null) {
			return;
		}
		
		// Draw rotated image 
		// @TODO scale image to size 
		g.image(movie, 0, 0);

	}
	
}
