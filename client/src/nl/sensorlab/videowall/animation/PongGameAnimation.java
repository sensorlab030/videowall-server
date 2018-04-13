package nl.sensorlab.videowall.animation;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.net;


public class PongGameAnimation extends BaseAnimation {

	PApplet parent;
	
//	Server server;
//	Client client;

	public PongGameAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
	}


	@Override
	protected void drawAnimationFrame(PGraphics g) {

		// Fade
		g.noStroke();
		g.fill(0, 20);
		g.rect(0, 0, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y);
	}
}
