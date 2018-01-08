package com.cleverfranke.ledwall.animation;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.video.Movie;

public class OpeningAnimation extends BaseCanvasAnimation {
	
	private float currentSoundIntensity;	// Current sound intensity [0, 1]
	private Movie videoWallMovie;

	public OpeningAnimation(PApplet applet) {
		super(applet);
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		
	}
	
}
