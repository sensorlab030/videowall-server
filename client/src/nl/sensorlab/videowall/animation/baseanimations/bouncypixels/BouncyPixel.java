package nl.sensorlab.videowall.animation.baseanimations.bouncypixels;

import java.util.ArrayList;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PGraphics;
import processing.core.PVector;

public class BouncyPixel {

	BouncyPixelSystem parent;

	public PVector position;
	public PVector velocity;
	public float diameter;
	public int id;
	private int color;

	public BouncyPixel(BouncyPixelSystem parent, float x, float y, float d, int id, int color) {
		this.parent = parent;
		this.position = new PVector(x, y);
		this.velocity = new PVector(0, 0);
		this.diameter = d;
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
				float minDistance = b.diameter/2 + diameter/2;

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
		if (position.x + diameter/2 > BaseAnimation.PIXEL_RESOLUTION_X) {
			position.x = BaseAnimation.PIXEL_RESOLUTION_X - diameter/2;
			velocity.x *= parent.friction;
		} else if (position.x - diameter/2 < 0) {
			position.x = diameter/2;
			velocity.x *= parent.friction;
		}
		if (position.y + diameter/2 > BaseAnimation.PIXEL_RESOLUTION_Y) {
			position.y = BaseAnimation.PIXEL_RESOLUTION_Y - diameter/2;
			velocity.y *= parent.friction;
		} else if (position.y - diameter/2 < 0) {
			position.y = diameter/2;
			velocity.y *= parent.friction;
		}
	}

	public void draw(PGraphics g) {
		g.stroke(color);
		g.point(position.x, position.y);
	}
}
