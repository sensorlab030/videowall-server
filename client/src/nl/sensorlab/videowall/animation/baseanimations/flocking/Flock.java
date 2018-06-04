package nl.sensorlab.videowall.animation.baseanimations.flocking;
import java.util.ArrayList;
import java.util.Iterator;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PGraphics;

public class Flock {

	private ArrayList<Boid> boids;

	protected int lifeTimeBoidCounterMillis = 0;


	public Flock(int[] colors) {
		this.boids = new ArrayList<Boid>();
	}

	public void generateFlock(int amountBoids) {
		for(int i = 0; i <= amountBoids; i++) {
			addBoid();
		}
	}

	public void removeBoid(int index) {
		boids.remove(index);
	}

	public void addBoid() {
		// Set random color
		int randomColor = FlockingAnimation.BOID_COLORS[(int)Math.floor(Math.random() * FlockingAnimation.BOID_COLORS.length)];

		// Add the boid
		int randomLifeTime = (int)(FlockingAnimation.MIN_LIFETIME_BOID_MILLIS + (Math.random() * FlockingAnimation.VARIATION_LIFETIME_BOID_MILLIS));	
		boids.add(new Boid(BaseAnimation.PIXEL_RESOLUTION_X/2, BaseAnimation.PIXEL_RESOLUTION_Y/2, randomLifeTime, randomColor));
	}

	public void clear() {
		boids.clear();
	}

	public int getFlockSize() {
		return boids.size();
	}

	public void updateAndDraw(PGraphics g, double dt) {

		Iterator<Boid> it = boids.iterator();
		while (it.hasNext()) {
			Boid boid = it.next();
			boid.update(boids, dt);
			boid.draw(g);
			
			// Remove the boid if exceeds the lifetime
			if (boid.lifeTimeCounterMillis > boid.lifetimeMillis) {
				it.remove();
			} else {
				boid.lifeTimeCounterMillis += dt;
			}
		}
		
	}
}
