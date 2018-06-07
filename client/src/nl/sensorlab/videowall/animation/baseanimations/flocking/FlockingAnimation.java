package nl.sensorlab.videowall.animation.baseanimations.flocking;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;

public class FlockingAnimation extends BaseAnimation {

	private static final int FLOCK_SIZE = 24;
	protected static final int[] BOID_COLORS = {PColor.color(7, 93, 144), PColor.color(24, 147, 196), PColor.color(108, 208, 198), PColor.color(235, 232, 225)};
	private static final int ADD_BOID_EVERY_MILLIS = 2500;
	private static final int MAX_AMOUNT_BOIDS = 100;
	protected static final int MIN_LIFETIME_BOID_MILLIS = 10000;
	protected static final int VARIATION_LIFETIME_BOID_MILLIS = 15000;
	
	private Flock flock;
	
	protected int addBoidCounterMillis = 0;

	public FlockingAnimation(PApplet applet) {
		super(applet);
		this.flock = new Flock(BOID_COLORS);

		// Add boids to the flock
		flock.generateFlock(FLOCK_SIZE);
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double dt) {
		
		// Add some fade effect
		g.fill(0, 30);
		g.noStroke();
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);

		// Draw and update the flock
		flock.updateAndDraw(g, dt);
	
		// Add boid
		if (addBoidCounterMillis > ADD_BOID_EVERY_MILLIS) {
			addBoidCounterMillis = 0;
			
			// Only allow boid to be added when in bounds
			if (flock.getFlockSize() < MAX_AMOUNT_BOIDS) {
				flock.addBoid();
			}	
			
		} else {
			addBoidCounterMillis += dt;
		}
	}
	
}