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


	private static float radius = 0.5f;
	private static float maxSpeed = 0.35f;
	private static float maxForce = 0.355f;
	private static float neighbourDistance = 20;

	private int color;

	public Boid(float x ,float y, int color) {
		this.position = new PVector(x, y);
		this.acceleration = new PVector(0,0);
		this.color = color;

		float angle = (float)(Math.random() * (Math.PI * 2));
		this.velocity = new PVector((float)Math.cos(angle), (float)Math.sin(angle)); 
	}

	public void update(ArrayList<Boid> boids) {
		updateFlock(boids);

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


	private void updateFlock(ArrayList<Boid> boids) {
		float desiredseparation = neighbourDistance * 0.5f;

		PVector separation = new PVector(0,0);
		PVector cohesion = new PVector(0,0);
		PVector alignment = new PVector(0,0);
		
		int count = 0;
		
		// Loop over all the boids
		for (Boid other : boids) {
			// For every boid in the system, check if it's too close
			float distance = PVector.dist(position, other.position);
			
			// If the distance is greater than 0 and less than an arbitrary amount (0 when you are yourself)
			if ((distance> 0) && (distance < desiredseparation)) {
				PVector diff = PVector.sub(position, other.position);
				diff.normalize();
				diff.div(distance);        // Weight by distance
				
				// Calculate vector pointing away from neighbor
				separation.add(diff);
				
				// For the average position (i.e. center) of all nearby boids, calculate steering vector towards that position
				cohesion.add(other.position); // Add position
				
				// For every nearby boid in the system, calculate the average velocity
				alignment.add(other.velocity);
				
				// Keep track of how many
				count++;            
			}
		}

		
		if (count > 0) {
			// ...
			cohesion = seek(cohesion.div((float)count));
			
			// Average -- divide by how many
			separation.div((float)count);
			
			// Implement Reynolds: Steering = Desired - Velocity
			alignment.div((float)count);
			alignment.normalize();
			alignment.mult(maxSpeed);
			alignment = PVector.sub(alignment, velocity);
			alignment.limit(maxForce);
			
		}else {
			alignment = new PVector(0,0);
			cohesion = new PVector(0,0); 
		}

		// As long as the vector is greater than 0
		if (separation.mag() > 0) {
			// Implement Reynolds: Steering = Desired - Velocity
			separation.normalize();
			separation.mult(maxSpeed);
			separation.sub(velocity);
			separation.limit(maxForce);
		}
		
		separation.mult(1.5f);
		alignment.mult(1.0f);
		cohesion.mult(1.0f);

		// Add the force vectors to acceleration
		acceleration.add(separation);
		acceleration.add(alignment);
		acceleration.add(cohesion);
	}



	public void keepInBounds() {
		if (position.x < - radius) {
			position.x = BaseAnimation.PIXEL_RESOLUTION_X+radius;
		}else if (position.x > BaseAnimation.PIXEL_RESOLUTION_X+radius) {
			position.x = -radius;
		}
		if (position.y < - radius) {
			position.y = BaseAnimation.PIXEL_RESOLUTION_Y+radius;
		}else if (position.y > BaseAnimation.PIXEL_RESOLUTION_Y+radius) {
			position.y = -radius;
		}
	}

	// A method that calculates and applies a steering force towards a target
	// STEER = DESIRED MINUS VELOCITY
	public PVector seek(PVector target) {
		PVector desired = PVector.sub(target, position);  // A vector pointing from the position to the target
		// Scale to maximum speed
		desired.normalize();
		desired.mult(maxSpeed);

		// Steering = Desired minus Velocity
		PVector steer = PVector.sub(desired, velocity);
		steer.limit(maxForce);  // Limit to maximum steering force
		return steer;
	}

	public void draw(PGraphics g) {
		// Draw a rectangle rotated in the direction of velocity
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
