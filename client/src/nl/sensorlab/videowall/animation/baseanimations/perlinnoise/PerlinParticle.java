package nl.sensorlab.videowall.animation.baseanimations.perlinnoise;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class PerlinParticle {
	
	PerlinNoiseAnimation parent;
	
	private PVector position;
	private PVector direction;
	private PVector velocity;
	
	private float speed;
	private float alpha;
	private float strokeWidth;
	
	private int color;

	public PerlinParticle(PerlinNoiseAnimation parent, float x, float y, float speed, int color, float alpha, float strokeWidth) {
		this.parent = parent;
		this.position = new PVector(x, y);
		this.direction = new PVector(0, 0);
		this.velocity = new PVector(0, 0);
		this.speed = speed;
		this.color = color;
		this.alpha = alpha;
		this.strokeWidth = strokeWidth;
	}
	
	public void update() {
		// Update angle
		float angle = parent.parent.noise(position.x/PerlinNoiseAnimation.NOISE_SCALE, position.y/PerlinNoiseAnimation.NOISE_SCALE) * PConstants.TWO_PI * PerlinNoiseAnimation.NOISE_SCALE;

		direction.x =  PApplet.cos(angle);
		direction.y =  PApplet.sin(angle);

		velocity = direction.copy();
		velocity.mult(speed);

		position.add(velocity);

		// Check if within bounds
		if (position.x >  BaseAnimation.PIXEL_RESOLUTION_X || position.x < 0 || position.y < 0 || position.y > BaseAnimation.PIXEL_RESOLUTION_Y) {
			this.position = new PVector(parent.parent.random(0, BaseAnimation.PIXEL_RESOLUTION_X), parent.parent.random(0, BaseAnimation.PIXEL_RESOLUTION_Y));
		}
	}
	
	public void draw(PGraphics g) {
		g.strokeWeight(strokeWidth);
		g.noFill();
		g.stroke(color, alpha);
		g.point(position.x, position.y);
	}

}


//public class noiseParticle {
//
//	PerlinNoiseAnimation parent;
//
//	PVector direction;
//	PVector velocity;
//	PVector position;
//
//	float speed = 0.25f;
//	float alpha;
//	float strokeWidth;
//
//	int col;
//
//	public noiseParticle(PerlinNoiseAnimation _parent, float _x, float _y, float _alpha, float _strokeWidth, int _col) {
//		this.parent = _parent;
//		this.direction = new PVector(0, 0);
//		this.velocity = new PVector(0, 0);
//		this.position = new PVector(_x, _y);
//		this.alpha = _alpha;
//		this.strokeWidth = _strokeWidth;
//		this.col = _col;
//	}
//	public void update() {
//		// Update angle
//		float angle = parent.parent.noise(position.x/parent.noiseScale, position.y/parent.noiseScale) * parent.parent.TWO_PI * parent.noiseScale;
//
//		direction.x =  parent.parent.cos(angle);
//		direction.y =  parent.parent.sin(angle);
//
//		velocity = direction.copy();
//		velocity.mult(speed);
//
//		position.add(velocity);
//
//		// Check if within bounds
//		if (position.x >  parent.PIXEL_RESOLUTION_X || position.x < 0 || position.y < 0 || position.y > parent.PIXEL_RESOLUTION_Y) {
//			this.position = new PVector(parent.parent.random(0, parent.PIXEL_RESOLUTION_X), parent.parent.random(0, parent.PIXEL_RESOLUTION_Y));
//		}
//	}
//
//
//	public void display(PGraphics g) {
//		g.strokeWeight(strokeWidth);
//		g.noFill();
//		g.stroke(col, alpha);
//		g.point(position.x, position.y);
//	}
//}