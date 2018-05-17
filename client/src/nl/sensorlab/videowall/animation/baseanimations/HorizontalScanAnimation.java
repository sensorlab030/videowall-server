package nl.sensorlab.videowall.animation.baseanimations;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;

public class HorizontalScanAnimation extends BaseAnimation {

	private float x = 0;
	private float speed = 0.0075f;
	private int direction = 1;
	
	public HorizontalScanAnimation(PApplet applet) {
		super(applet);
	}
	
	@Override
	protected void drawAnimationFrame(PGraphics g, double dt) {
		// Fade effect
		g.noStroke();
		g.fill(0,5);
		g.rect(0, 0, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y);
		
		// Update the x position
		x += (speed * dt) * direction;
		
		// Toggle direction
		if(x > PIXEL_RESOLUTION_X ) {
			direction = -1;
		}else if(x < 0) {
			direction  = 1;
		}
		
		// Map color
		float c = PApplet.map(x, 0, PIXEL_RESOLUTION_X, 0, 255);
		
		g.strokeWeight(2);
		g.stroke(c, 200, 150, 128);
		g.line(x, 0, x, PIXEL_RESOLUTION_Y);

	}
	
}
