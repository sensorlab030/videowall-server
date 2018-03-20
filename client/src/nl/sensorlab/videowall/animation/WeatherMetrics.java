package nl.sensorlab.videowall.animation;

import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class WeatherMetrics extends BaseCanvasAnimation {

	public WeatherMetrics(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);	 
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		g.fill(255,0,0);
		g.rect(0, 0, 1000, 1000);
	}

}
