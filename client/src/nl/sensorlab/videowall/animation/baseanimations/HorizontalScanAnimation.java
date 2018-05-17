package nl.sensorlab.videowall.animation.baseanimations;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;

public class HorizontalScanAnimation extends BaseAnimation {

	private float barX = 0;
	private float barSpeed = 0.0035f;
	private int barDirection = 1;
	
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
		barX += (barSpeed * dt) * barDirection;
		
		// Toggle direction
		if(barX > PIXEL_RESOLUTION_X ) {
			barDirection = -1;
		}else if(barX < 0) {
			barDirection  = 1;
		}
		
		// Map color
		float colorBar = PApplet.map(barX, 0, PIXEL_RESOLUTION_X, 0, 255);
		
		g.strokeWeight(2);
		g.stroke(colorBar, 200, 150, 128);
		g.line(barX, 0, barX, PIXEL_RESOLUTION_Y);

	}
	
}
