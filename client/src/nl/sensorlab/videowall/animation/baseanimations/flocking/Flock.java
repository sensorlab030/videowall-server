package nl.sensorlab.videowall.animation.baseanimations.flocking;
import java.util.ArrayList;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PGraphics;

public class Flock {

	FlockingAnimation parent;
	
	public  ArrayList<Boid> boids;
	public int[] colors;

	public Flock(FlockingAnimation p, int[] colors) {
		this.parent = p;
		this.boids = new ArrayList<Boid>();
		this.colors = colors;
	}
	
	public void generateFlock(int amountBoids) {
		for(int i = 0; i <= amountBoids; i++) {
			addBoid();
		}
	}
	
	public void addBoid() {
		// Set random color
		int randomColor = colors[(int)Math.floor(Math.random() * colors.length)];
		
		// Add the boid
		boids.add(new Boid(BaseAnimation.PIXEL_RESOLUTION_X/2, BaseAnimation.PIXEL_RESOLUTION_Y/2, randomColor));
	}
	
	public void clear() {
		boids.clear();
	}
	
	public void draw(PGraphics g) {
		for(Boid boid : boids) {
			boid.update(boids);
			boid.draw(g);
		}
	}
}
