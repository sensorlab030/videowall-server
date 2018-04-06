package jonas;

import java.util.ArrayList;

import nl.sensorlab.videowall.animation.BouncyBubblesAnimation;
import processing.core.PGraphics;

public class Ball {

	BouncyBubblesAnimation parent;

	public float x, y;
	public float diameter;
	public float vx = 0;
	public float vy = 0;
	public int id; 

	public int color;

	public Ball(BouncyBubblesAnimation _p, float _x, float _y, float _d, int _id, int _color) {
		this.parent = _p;
		this.x = _x;
		this.y = _y;
		this.diameter = _d;
		this.id = _id;
		this.color = _color;
	} 


	public void collide(ArrayList<Ball> balls) {
		for (int i = id + 1; i <balls.size(); i++) {
			float dx = balls.get(i).x - x;
			float dy = balls.get(i).y - y;
			float distance = (float)Math.sqrt(dx*dx + dy*dy);
			float minDist = balls.get(i).diameter/2 + diameter/2;
			if (distance < minDist) { 
				float angle = (float)Math.atan2(dy, dx);
				float targetX = x + (float)Math.cos(angle) * minDist;
				float targetY = y + (float)Math.sin(angle) * minDist;
				float ax = (targetX - balls.get(i).x) * parent.spring;
				float ay = (targetY -balls.get(i).y) * parent.spring;
				vx -= ax;
				vy -= ay;
				balls.get(i).vx += ax;
				balls.get(i).vy += ay;
			}
		}
	}

	public void update() {
		vy += parent.gravity;
		x += vx;
		y += vy;
		if (x + diameter/2 > parent.PIXEL_RESOLUTION_X) {
			x = parent.PIXEL_RESOLUTION_X - diameter/2;
			vx *= parent.friction;
		} else if (x - diameter/2 < 0) {
			x = diameter/2;
			vx *= parent.friction;
		}
		if (y + diameter/2 > parent.PIXEL_RESOLUTION_Y) {
			y = parent.PIXEL_RESOLUTION_Y - diameter/2;
			vy *= parent.friction;
		} else if (y - diameter/2 < 0) {
			y = diameter/2;
			vy *= parent.friction;
		}

	}

	public void display(PGraphics g) {
		g.noStroke();
		g.fill(color);
		g.ellipse(x, y, diameter, diameter);
	}


}
