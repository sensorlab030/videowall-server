package nl.sensorlab.videowall.animation.baseanimations.flocking;

import java.util.ArrayList;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class Boid {

	private PVector position;
	private PVector velocity;
	private PVector acceleration;


	public float radius = 0.5f;
	public float maxSpeed = 0.35f;
	public float maxForce = 0.355f;
	public float neighbourDistance = 20;

	int color;

	public Boid(float x ,float y, int color) {
		this.position = new PVector(x, y);
		this.acceleration = new PVector(0,0);
		this.color = color;

		float angle = (float)(Math.random() * (Math.PI * 2));
		this.velocity = new PVector((float)Math.cos(angle), (float)Math.sin(angle)); 
	}

	public void update(ArrayList<Boid> boids) {
		flock(boids);

		// Update velocity
		velocity.add(acceleration);

		// Limit speed
		velocity.limit(maxSpeed);
		position.add(velocity);

		// Reset accelertion to 0 each cycle
		acceleration.mult(0);

		// Keep the boid within the window
		keepInBounds();
	}

	private void flock(ArrayList<Boid> boids) {	
		PVector sep = separate(boids);   // Separation
		PVector ali = align(boids);      // Alignment
		PVector coh = cohesion(boids);   // Cohesion

		// Arbitrarily weight these forces
		sep.mult(1.5f);
		ali.mult(1.0f);
		coh.mult(1.0f);

		// Add the force vectors to acceleration
		acceleration.add(sep);
		acceleration.add(ali);
		acceleration.add(coh);
	}

	public void keepInBounds() {
		if (position.x < - radius) {
			position.x = BaseAnimation.PIXEL_RESOLUTION_X+radius;
		}
		if (position.y < - radius) {
			position.y = BaseAnimation.PIXEL_RESOLUTION_Y+radius;
		}
		if (position.x > BaseAnimation.PIXEL_RESOLUTION_X+radius) {
			position.x = -radius;
		}
		if (position.y > BaseAnimation.PIXEL_RESOLUTION_Y+radius) {
			position.y = -radius;
		}
	}

	// A method that calculates and applies a steering force towards a target
	// STEER = DESIRED MINUS VELOCITY
	public PVector seek(PVector target) {
		PVector desired = PVector.sub(target, position);  // A vector pointing from the position to the target
		// Scale to maximum speed
		desired.setMag(maxSpeed);

		// Steering = Desired minus Velocity
		PVector steer = PVector.sub(desired, velocity);
		steer.limit(maxForce);  // Limit to maximum steering force
		return steer;
	}

	// Separation
	// Method checks for nearby boids and steers away
	public  PVector separate (ArrayList<Boid> boids) {
		float desiredseparation = neighbourDistance * 0.5f;
		PVector steer = new PVector(0, 0, 0);
		int count = 0;
		// For every boid in the system, check if it's too close
		for (Boid other : boids) {
			float d = PVector.dist(position, other.position);
			// If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
			if ((d > 0) && (d < desiredseparation)) {
				// Calculate vector pointing away from neighbor
				PVector diff = PVector.sub(position, other.position);
				diff.normalize();
				diff.div(d);        // Weight by distance
				steer.add(diff);
				count++;            // Keep track of how many
			}
		}
		// Average -- divide by how many
		if (count > 0) {
			steer.div((float)count);
		}

		// As long as the vector is greater than 0
		if (steer.mag() > 0) {
			// Implement Reynolds: Steering = Desired - Velocity
			steer.setMag(maxSpeed);
			steer.sub(velocity);
			steer.limit(maxForce);
		}
		return steer;
	}


	// Cohesion
	// For the average position (i.e. center) of all nearby boids, calculate steering vector towards that position
	public PVector cohesion (ArrayList<Boid> boids) {
		PVector sum = new PVector(0, 0);   // Start with empty vector to accumulate all positions
		int count = 0;
		for (Boid other : boids) {
			float d = PVector.dist(position, other.position);
			if ((d > 0) && (d < neighbourDistance)) {
				sum.add(other.position); // Add position
				count++;
			}
		}
		if (count > 0) {
			sum.div(count);
			return seek(sum);  // Steer towards the position
		} 
		else {
			return new PVector(0, 0);
		}
	}

	// Alignment
	// For every nearby boid in the system, calculate the average velocity
	public PVector align (ArrayList<Boid> boids) {
		PVector sum = new PVector(0, 0);
		int count = 0;
		for (Boid other : boids) {
			float d = PVector.dist(position, other.position);
			if ((d > 0) && (d < neighbourDistance)) {
				sum.add(other.velocity);
				count++;
			}
		}
		if (count > 0) {
			sum.div((float)count);
			sum.setMag(maxSpeed);
			PVector steer = PVector.sub(sum, velocity);
			steer.limit(maxForce);
			return steer;
		} 
		else {
			return new PVector(0, 0);
		}
	}

	public void draw(PGraphics g) {
		// Draw a triangle rotated in the direction of velocity
		float theta = velocity.heading() + PApplet.radians(90);

		g.noStroke();
		g.fill(color);
		g.pushMatrix();
		g.translate(position.x, position.y);
		g.rotate(theta);
		g.rect(0, 0, 1 + radius, 1);
		g.popMatrix();
	}
}
