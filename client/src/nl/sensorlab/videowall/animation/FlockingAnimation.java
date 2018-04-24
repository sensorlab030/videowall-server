package nl.sensorlab.videowall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import jonas.Boid;
import jonas.Flock;
import jonas.Pixel;
import jonas.TriggerObject;
import jonas.TriggerSequence;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class FlockingAnimation extends BaseAnimation {

	// Example used from Daniel Shiffman which can be found in the processing examples.
	public PApplet parent;

	public int[] boidcolors = {PColor.color(245,20,147), PColor.color(69,33,124), PColor.color(7,153,242), PColor.color(255,255,255)};

	public Flock flock;
	public int sizeFlock = 4;

	public int counter = 0; // <-- Add new boid every x amount of time
	public int counterAddBoids = 120;

	public FlockingAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		this.flock = new Flock(this);

		// Add boids
		generateFlock(sizeFlock);
	}

	protected void generateFlock(int _sizeFlock) {
		for(int i = 0; i < _sizeFlock; i++) {
			flock.addBoid(new Boid(this, PIXEL_RESOLUTION_X/2, PIXEL_RESOLUTION_Y/2, boidcolors[(int)Math.floor(Math.random() * boidcolors.length)]));
		}
	}

	protected void addBoid() {
		flock.addBoid(new Boid(this, (float)(Math.random() * PIXEL_RESOLUTION_X), (float)(Math.random() * PIXEL_RESOLUTION_Y/2), boidcolors[(int)Math.floor(Math.random() * boidcolors.length)]));
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double t) {
		// Add some fade effect
		g.fill(0, 50);
		g.noStroke();
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);

		// Flocking
		flock.run(g);
	

		// Add boid
		if(counter > counterAddBoids) {
			counter = 0;
			addBoid();
			
			System.err.println("boid added");
			// Reset flock
			if(flock.boids.size() > 100) {
				flock.boids.clear();
				addBoid();
			}	
		}else {
			counter++;
		}
	}
}
