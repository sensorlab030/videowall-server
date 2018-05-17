package nl.sensorlab.videowall.animation.baseanimations.bouncypixels;

import java.util.ArrayList;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PGraphics;
import processing.core.PVector;

public class BouncyPixel {

	private BouncyPixelSystem parent;

	public PVector position;
	public PVector velocity;
	public float radius;
	public int id;
	private int color;

	public BouncyPixel(BouncyPixelSystem parent, float x, float y, float diameter, int id, int color) {
		this.parent = parent;
		this.position = new PVector(x, y);
		this.velocity = new PVector(0, 0);
		this.radius = diameter/2;
		this.id = id;
		this.color = color;
	}

	public void collide(ArrayList<BouncyPixel> bouncypixels) {
		for (int i = 0; i < bouncypixels.size(); i++) {
			// Ignore self
			if(i != id){
				BouncyPixel b = bouncypixels.get(i);
				float dx = b.position.x - position.x;
				float dy = b.position.y - position.y;

				// The minimum distance
				float minDistance = b.radius + radius;

				// The actual distance
				float distance = (float)Math.sqrt(dx*dx + dy*dy);

				// If exceeds minimum distance
				if(distance < minDistance) {
					float angle = (float)Math.atan2(dy, dx);
					float targetX = position.x + (float)Math.cos(angle) * minDistance;
					float targetY = position.y + (float)Math.sin(angle) * minDistance;
					
					float ax = (targetX - b.position.x) * parent.spring;
					float ay = (targetY - b.position.y) * parent.spring;

					// Update velocity
					velocity.x -= ax;
					velocity.y -= ay;

					// Update b (change direction)
					b.velocity.x += ax;
					b.velocity.y += ay;
				}		
			}
		}
	}

	public void update() {
		// Only update the y velocity
		velocity.y += parent.gravity;

		// Update positions
		position.x += velocity.x;
		position.y += velocity.y;

		// Keep inbounds
		if (position.x + radius > BaseAnimation.PIXEL_RESOLUTION_X) {
			position.x = BaseAnimation.PIXEL_RESOLUTION_X - radius;
			velocity.x *= parent.friction;
		} else if (position.x -radius < 0) {
			position.x = radius;
			velocity.x *= parent.friction;
		}
		if (position.y + radius > BaseAnimation.PIXEL_RESOLUTION_Y) {
			position.y = BaseAnimation.PIXEL_RESOLUTION_Y -radius;
			velocity.y *= parent.friction;
		} else if (position.y - radius < 0) {
			position.y = radius;
			velocity.y *= parent.friction;
		}
	}

	public void draw(PGraphics g) {
		g.stroke(color);
		g.point(position.x, position.y);
	}
}
