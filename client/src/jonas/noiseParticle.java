package jonas;

import nl.sensorlab.videowall.animation.PerlinNoiseAnimation;
import processing.core.PGraphics;
import processing.core.PVector;

public class noiseParticle {

	PerlinNoiseAnimation parent;

	PVector direction;
	PVector velocity;
	PVector position;

	float speed = 0.25f;
	float alpha;
	float strokeWidth;

	int col;

	public noiseParticle(PerlinNoiseAnimation _parent, float _x, float _y, float _alpha, float _strokeWidth, int _col) {
		this.parent = _parent;
		this.direction = new PVector(0, 0);
		this.velocity = new PVector(0, 0);
		this.position = new PVector(_x, _y);
		this.alpha = _alpha;
		this.strokeWidth = _strokeWidth;
		this.col = _col;
	}
	public void update() {
		// Update angle
		float angle = parent.parent.noise(position.x/parent.noiseScale, position.y/parent.noiseScale) * parent.parent.TWO_PI * parent.noiseScale;

		direction.x =  parent.parent.cos(angle);
		direction.y =  parent.parent.sin(angle);

		velocity = direction.copy();
		velocity.mult(speed);

		position.add(velocity);

		// Check if within bounds
		if (position.x >  parent.PIXEL_RESOLUTION_X || position.x < 0 || position.y < 0 || position.y > parent.PIXEL_RESOLUTION_Y) {
			this.position = new PVector(parent.parent.random(0, parent.PIXEL_RESOLUTION_X), parent.parent.random(0, parent.PIXEL_RESOLUTION_Y));
		}
	}


	public void display(PGraphics g) {
		g.strokeWeight(strokeWidth);
		g.noFill();
		g.stroke(col, alpha);
		g.point(position.x, position.y);
	}
}
