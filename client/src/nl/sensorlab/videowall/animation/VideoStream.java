package nl.sensorlab.videowall.animation;

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

	public VideoStream(PApplet applet) {
		super(applet);


	}

	@Override
	protected final void drawAnimationFrame(PGraphics g) {
		g.fill(255, 0 ,0);
		g.rect(0, 0, 150, 150);

	}
}
