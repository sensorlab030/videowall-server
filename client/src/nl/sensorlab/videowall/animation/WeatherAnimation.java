package nl.sensorlab.videowall.animation;

import processing.core.PApplet;
import processing.core.PGraphics;

public class WeatherAnimation extends BaseAnimation {

	public WeatherAnimation(PApplet applet) {
		super(applet);
	}

	@Override
	protected void drawAnimationFrame(PGraphics g) {
		g.noStroke();
		for (int x = 0; x < PIXEL_RESOLUTION_X; x++ ) {
			for (int y = 0; y < PIXEL_RESOLUTION_Y; y++) {
				g.stroke(PApplet.map((float) Math.random(), 0, 1, 0, 255),PApplet.map((float) Math.random(), 0, 1, 0, 255),PApplet.map((float) Math.random(), 0, 1, 0, 255));
				g.point(x, y);
			}
		}

	}
}
