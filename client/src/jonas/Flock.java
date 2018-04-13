package jonas;

import java.util.ArrayList;

import nl.sensorlab.videowall.animation.FlockingAnimation;
import processing.core.PGraphics;

public class Flock {
	public ArrayList<Boid> boids; // An ArrayList for all the boids
	
	FlockingAnimation parent;

	public Flock(FlockingAnimation _parent) {
		this.parent = _parent;
		this.boids = new ArrayList<Boid>(); // Initialize the ArrayList
	}

	public void run(PGraphics g) {
		for (Boid b : boids) {
			b.run(boids, g);  // Passing the entire list of boids to each boid individually
		}
	}

	public void addBoid(Boid b) {
		boids.add(b);
	}
}
