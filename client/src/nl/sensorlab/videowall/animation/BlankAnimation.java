package nl.sensorlab.videowall.animation;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Animation that just draws black
 */
public class BlankAnimation extends BaseAnimation {

	public BlankAnimation(PApplet applet) {
		super(applet);
	}

	@Override
	protected void drawAnimationFrame(PGraphics g) {
		g.background(0);
	}

}
