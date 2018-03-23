package nl.sensorlab.videowall.animation;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class Alphabet extends BaseAnimation {

	/**
	 * Initialize a PixelAnimation
	 *
	 * @param applet
	 */
	public Alphabet(PApplet applet) {
		super(applet);
	}

	/**
	 * Implementation of generating the animation frame by child classes. Classes
	 * extending BaseAnimation are required to implement this method and use
	 * the supplied PGraphics context to draw the animation frame
	 *
	 * @param g
	 */
	@Override
	protected final void drawAnimationFrame(PGraphics g) {
		g.fill(255, 0 ,0);
		g.rect(0, 0, 150, 81);
	}

}
