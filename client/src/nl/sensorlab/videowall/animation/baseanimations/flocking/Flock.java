package nl.sensorlab.videowall.animation.baseanimations.flocking;
import java.util.ArrayList;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PGraphics;

public class Flock {

	private FlockingAnimation parent;

	private ArrayList<Boid> boids;
	private ArrayList<Integer> boidstoremove;
	
	protected int lifeTimeBoidCounterMillis = 0;


	public Flock(FlockingAnimation p, int[] colors) {
		this.parent = p;
		this.boids = new ArrayList<Boid>();
		this.boidstoremove = new ArrayList<Integer>();
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
		// Reset the remove arraylist
		boidstoremove.clear();

		int index = 0;
		for(Boid boid : boids) {
			boid.update(boids);
			boid.draw(g);
			
			// Add the boid index if exceeds the lifetime
			if(boid.lifetimeMillis < boid.lifeTimeCounterMillis) {
				// Add the index of the bouncy pixel to the remove arraylist
				boidstoremove.add(index);
			}else {
				boid.lifeTimeCounterMillis += dt;
			}
			index++;
		}

		// Remove entries in a descending order; this will remove the elements from the list without undesirable side effects
		for(int i = boidstoremove.size() - 1; i >= 0; i--) {
			removeBoid(boidstoremove.get(i));
		}
	}
}
