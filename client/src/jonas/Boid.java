package jonas;

import java.util.ArrayList;

import nl.sensorlab.videowall.animation.FlockingAnimation;
import processing.core.PGraphics;
import processing.core.PVector;

public class Boid {

	FlockingAnimation parent;

	PVector position;
	PVector velocity;
	PVector acceleration;

	float r;
	float maxforce;    // Maximum steering force
	float maxspeed;    // Maximum speed
	
	float neighbordist = 20;
	
	int color;

	public Boid(FlockingAnimation _parent, float _x, float _y, int _color) {
		this.parent = _parent;
		this.acceleration = new PVector(0, 0);
		this.color = _color;

		// Leaving the code temporarily this way so that this example runs in JS
		float angle = (float)(Math.random() * (Math.PI * 2));
		this.velocity = new PVector((float)Math.cos(angle), (float)Math.sin(angle));

		this.position = new PVector(_x, _y);
		this.r = 0.5f;
		this.maxspeed = 0.35f;
		this.maxforce = 0.035f;
	}

	public void run(ArrayList<Boid> boids, PGraphics g) {
		flock(boids);
		update();
		borders();
		render(g);
	}

	void applyForce(PVector force) {
		// We could add mass here if we want A = F / M
		acceleration.add(force);
	}

	// We accumulate a new acceleration each time based on three rules
	public void flock(ArrayList<Boid> boids) {
		PVector sep = separate(boids);   // Separation
		PVector ali = align(boids);      // Alignment
		PVector coh = cohesion(boids);   // Cohesion
		// Arbitrarily weight these forces
		sep.mult(1.5f);
		ali.mult(1.0f);
		coh.mult(1.0f);
		// Add the force vectors to acceleration
		applyForce(sep);
		applyForce(ali);
		applyForce(coh);
	}


	// Method to update position
	public void update() {
		// Update velocity
		velocity.add(acceleration);
		// Limit speed
		velocity.limit(maxspeed);
		position.add(velocity);
		// Reset accelertion to 0 each cycle
		acceleration.mult(0);
	}

	// A method that calculates and applies a steering force towards a target
	// STEER = DESIRED MINUS VELOCITY
	public PVector seek(PVector target) {
		PVector desired = PVector.sub(target, position);  // A vector pointing from the position to the target
		// Scale to maximum speed
		desired.normalize();
		desired.mult(maxspeed);

		// Above two lines of code below could be condensed with new PVector setMag() method
		// Not using this method until Processing.js catches up
		// desired.setMag(maxspeed);

		// Steering = Desired minus Velocity
		PVector steer = PVector.sub(desired, velocity);
		steer.limit(maxforce);  // Limit to maximum steering force
		return steer;
	}

	public void render(PGraphics g) {
		// Draw a triangle rotated in the direction of velocity
		float theta = velocity.heading2D() + parent.parent.radians(90);
		// heading2D() above is now heading() but leaving old syntax until Processing.js catches up
		
		g.noStroke();
		g.fill(color);
		g.pushMatrix();
		g.translate(position.x, position.y);
		g.rotate(theta);
		g.rect(0, 0, 1 + r, 1);
		g.popMatrix();
	}

	// Wraparound
	public void borders() {
		if (position.x < -r) position.x = parent.PIXEL_RESOLUTION_X+r;
		if (position.y < -r) position.y = parent.PIXEL_RESOLUTION_Y+r;
		if (position.x > parent.PIXEL_RESOLUTION_X+r) position.x = -r;
		if (position.y > parent.PIXEL_RESOLUTION_Y+r) position.y = -r;
	}

	// Separation
	// Method checks for nearby boids and steers away
	public  PVector separate (ArrayList<Boid> boids) {
		float desiredseparation = neighbordist * 0.5f;
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
			// First two lines of code below could be condensed with new PVector setMag() method
			// Not using this method until Processing.js catches up
			// steer.setMag(maxspeed);

			// Implement Reynolds: Steering = Desired - Velocity
			steer.normalize();
			steer.mult(maxspeed);
			steer.sub(velocity);
			steer.limit(maxforce);
		}
		return steer;
	}


	// Alignment
	// For every nearby boid in the system, calculate the average velocity
	public PVector align (ArrayList<Boid> boids) {
		PVector sum = new PVector(0, 0);
		int count = 0;
		for (Boid other : boids) {
			float d = PVector.dist(position, other.position);
			if ((d > 0) && (d < neighbordist)) {
				sum.add(other.velocity);
				count++;
			}
		}
		if (count > 0) {
			sum.div((float)count);
			// First two lines of code below could be condensed with new PVector setMag() method
			// Not using this method until Processing.js catches up
			// sum.setMag(maxspeed);

			// Implement Reynolds: Steering = Desired - Velocity
			sum.normalize();
			sum.mult(maxspeed);
			PVector steer = PVector.sub(sum, velocity);
			steer.limit(maxforce);
			return steer;
		} 
		else {
			return new PVector(0, 0);
		}
	}
	
	  // Cohesion
	  // For the average position (i.e. center) of all nearby boids, calculate steering vector towards that position
	public PVector cohesion (ArrayList<Boid> boids) {
	    PVector sum = new PVector(0, 0);   // Start with empty vector to accumulate all positions
	    int count = 0;
	    for (Boid other : boids) {
	      float d = PVector.dist(position, other.position);
	      if ((d > 0) && (d < neighbordist)) {
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


}
