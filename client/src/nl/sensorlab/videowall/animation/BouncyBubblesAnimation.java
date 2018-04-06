package nl.sensorlab.videowall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import jonas.Ball;
import jonas.Node;
import jonas.Particle;
import jonas.Pixel;
import jonas.TriggerObject;
import jonas.TriggerSequence;
import jonas.noiseParticle;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class BouncyBubblesAnimation extends BaseAnimation {

	public PApplet parent;

	public ArrayList<Ball> balls;

	public int amountOfBalls = 10;
	public float spring = 0.05f;
	public float gravity = 0.075f;
	public float friction = -0.35f;

	public int counter = 0; // <-- Add new ball every x amount of time
	public int counterAddBall = 100;
	
	public int[] ballcolors = {PColor.color(245,20,147), PColor.color(69,33,124), PColor.color(7,153,242), PColor.color(255,255,255)};

	public BouncyBubblesAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		this.balls = new ArrayList<Ball>();
		generateBalls(amountOfBalls);
	}


	public void generateBalls(int _amountOfBalls) {
		for (int i = 0; i < _amountOfBalls; i++) {
			addBall();
		}
	}

	public void addBall() {
		int index = balls.size() - 1;
		int color = ballcolors[(int)Math.floor(Math.random() * ballcolors.length)];
		balls.add(new Ball(this,  (float)(Math.random() * PIXEL_RESOLUTION_X), 0, parent.random(1, 3), index, color));
	}

	public void drawBalls(PGraphics g) {
		for (Ball ball : balls) {
			ball.collide(balls);
			ball.update();
			ball.display(g);
		}	
	}

	@Override
	protected void drawAnimationFrame(PGraphics g) {
		// Add some fade effect
		g.fill(0, 50);
		g.noStroke();
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);

		// Draw and update the balls
		drawBalls(g);
		
		// Add ball
		if(counter > counterAddBall) {
			counter = 0;
			addBall();
			if(balls.size() > 500) {
				balls.clear();
				generateBalls(10);
			}
		}else {
			counter++;	
		}
	}
}