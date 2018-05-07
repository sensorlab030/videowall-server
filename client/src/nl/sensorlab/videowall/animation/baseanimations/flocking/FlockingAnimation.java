package nl.sensorlab.videowall.animation.baseanimations.flocking;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;

public class FlockingAnimation extends BaseAnimation {

	// Example used from Daniel Shiffman which can be found in the processing examples.
	public PApplet parent;

	public int[] boidcolors = {PColor.color(245,20,147), PColor.color(69,33,124), PColor.color(7,153,242), PColor.color(255,255,255)};

	private Flock flock;
	private final int FLOCK_SIZE = 12;

	public int counter = 0; // <-- Add new boid every x amount of time
	public int counterAddBoids = 120;

	public FlockingAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		this.flock = new Flock(this, boidcolors);

		// add boids to the flock
		flock.generateFlock(FLOCK_SIZE);
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double t) {
		// Add some fade effect
		g.fill(0, 20);
		g.noStroke();
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);

		// Draw the flock..
		flock.draw(g);
	
		// Add boid
		if(counter > counterAddBoids) {
			counter = 0;
			flock.addBoid();
			
			// Reset flock
			if(flock.boids.size() > 100) {
				flock.clear();
				flock.addBoid();
			}	
		}else {
			counter++;
		}
	}
}