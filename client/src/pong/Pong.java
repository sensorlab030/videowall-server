package pong;

import java.util.ArrayList;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.PongGameAnimation;
import processing.core.PGraphics;
import processing.core.PVector;

public class Pong {

	PongGameAnimation parent;

	ArrayList<Brick> bricks;

	Ball ball;
	Bat bat;

	public int width;
	public int height;

	public Pong(PongGameAnimation _p, int _width, int _height) {
		this.parent = _p;
		this.width = _width;
		this.height = _height;

		// Create a new ball and bat
		ball = new Ball(this, this.width/2, height * 0.8f, 2, .5f);
		bat = new Bat(this.width/2, this.height - 4, 6, 4);
		bricks = new ArrayList<Brick>();

		// Generate the bricks
		generateBricks(1, 25, 4, this.height/3, 1, 4, 0, 3);
	}

	private void generateBricks(int _startX, int _endX, int _startY, int _endY, int _brickWidth, int _brickHeight, int _spacingX, int _spacingY) {
		for(int x = _startX; x < _endX; x += (_spacingX + _brickWidth)) {
			for(int y = _startY; y < _endY; y += (_spacingY + _brickHeight)) {
				float g = parent.parent.map(y, _startY, _endY, 100, 240);
				int color = PColor.color(0, (int)g, 240);
				bricks.add(new Brick(x, y, _brickWidth, _brickHeight, color));
			}
		}
	}

	private void resetGame() {
		System.err.println("Reset Game");
		// Reset position
		ball.position.x = width/2;
		ball.position.y = height * 0.8f;
		ball.velocity.x = ball.speed;
		ball.velocity.y = ball.speed;

		ball.outsideGame = false;

		// Reset bricks
		for(Brick b : bricks) b.active = true;
	}

	boolean rectCollide(PVector _source, float _sourceW, float _sourceH, PVector _target, float _targetW, float _targetH) {
		if (_source.x + _sourceW >= _target.x &&
				_source.x <= _target.x + _targetW &&
				_source.y + _sourceH >= _target.y &&
				_source.y <= _target.y + _targetH) return true;
		return false;
	}

	public void update(int _inputX) {

		// Update accordingly
		ball.update();
		bat.update(_inputX);

		// Reset game when the player doenst catch the ball
		if(ball.outsideGame) resetGame();

		// Check if collide
		batCollide();
		brickCollide();
	}

	private void batCollide() {
		if (rectCollide(ball.position, ball.radius, ball.radius, bat.position, bat.width, bat.height)) {
			System.err.println("batCollide");
			// Determine the angle
			float angle = parent.parent.map((ball.position.x - bat.position.x), 0, bat.width, 150, 30);

			// Update the velocity (direction)
			ball.velocity.x = ball.speed * parent.parent.cos(parent.parent.radians(angle));
			ball.velocity.y = - ball.speed * parent.parent.sin(parent.parent.radians(angle));
		}
	}

	private void brickCollide() {
		for(Brick b : bricks) {
			if (rectCollide(ball.position, ball.radius, ball.radius, b.position, b.width, b.height) && b.active) {
				// Inverse direction
				ball.velocity.y *= -1;
				b.active = false; // <-- Hide when collision
			}
		}
	}

	public void display(PGraphics g) {
		// Only display bricks when active
		for(Brick b : bricks) if(b.active) b.display(g);

		// Display the ball and bat on top
		ball.display(g);
		bat.display(g);
	}
}
