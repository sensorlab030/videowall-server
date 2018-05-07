package nl.sensorlab.videowall.animation.baseanimations.flocking;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;

public class FlockingAnimation extends BaseAnimation {

	// Example used from Daniel Shiffman which can be found in the processing examples.
	public PApplet parent;
	
	private Flock flock;
	private static final int FLOCK_SIZE = 12;
	private static final int[] BOID_COLORS = {PColor.color(7, 93, 144), PColor.color(24, 147, 196), PColor.color(108, 208, 198), PColor.color(235, 232, 225)};
	private static final int ADD_BOID_EVERY_MILLIS = 2500;
	private static final int MAX_AMOUNT_BOIDS = 100; // <-- When to reset
	
	private int addBoidCounterMillis = 0;

	public FlockingAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		this.flock = new Flock(this, BOID_COLORS);

		// Add boids to the flock
		flock.generateFlock(FLOCK_SIZE);
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double dt) {
		// Add some fade effect
		g.fill(0, 50);
		g.noStroke();
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);

		// Draw the flock
		flock.draw(g);
	
		// Add boid
		if(addBoidCounterMillis > ADD_BOID_EVERY_MILLIS) {
			addBoidCounterMillis = 0;
			flock.addBoid();
			
			// Reset flock
			if(flock.boids.size() > MAX_AMOUNT_BOIDS) {
				flock.clear();
				flock.generateFlock(FLOCK_SIZE);
			}	
		}else {
			addBoidCounterMillis += t;
		}
	}
}