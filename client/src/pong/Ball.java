package pong;

import processing.core.PGraphics;
import processing.core.PVector;

public class Ball {

	Pong parent;

	public PVector position;
	public PVector velocity;

	public float radius;
	public float speed;
	public boolean outsideGame = false; // <-- If player does not catch the 'ball'

	public Ball(Pong _p, float _x, float _y, float _r, float _speed){
		this.parent = _p;
		this.position = new PVector(_x, _y);
		this.velocity = new PVector(_speed, _speed);
		this.speed = _speed;
		this.radius = _r;
	}

	void update() {
		// Check if player missed the ball
		if(this.position.y > parent.height) outsideGame = true;

		// Update velocities
		if (this.position.x < 0 || this.position.x > parent.width) this.velocity.x = - this.velocity.x;
		if (this.position.y < 0 || this.position.y > parent.height) this.velocity.y = - this.velocity.y;

		// Update positions
		this.position.x += this.velocity.x;
		this.position.y += this.velocity.y;
	}

	void display(PGraphics g) {
		g.noStroke();
		g.fill(255,20,147);
		g.ellipse(this.position.x, this.position.y, this.radius, this.radius);
	}

}
