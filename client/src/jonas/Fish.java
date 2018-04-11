package jonas;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.FishTankAnimation;
import processing.core.PGraphics;
import processing.core.PVector;

public class Fish {
	
	FishTankAnimation parent;

	private PVector loc;
	private PVector vel;
	
	/* Just to add some individuality to the fish wiggle */
	float s;
	float d;
	
	int color;

	public Fish(FishTankAnimation _p, float _x, float _y) {
		this.parent = _p;
		this.loc = new PVector(_x, _y);
			
		// Scaling
		this.d = parent.parent.random(0.015f, 0.03f);
		this.s = parent.parent.random(-90, 90);
		
		this.color = PColor.hsb(parent.parent.random(255),100,100);
		
		/* Make a random velocity */
		float speed = 0.075f;
		this.vel = new PVector(parent.parent.random(-speed, speed), parent.parent.random(-speed, speed));
	}

	public void display(PGraphics g) {
		loc.add(vel);
		g.fill(color, 30);
		g.stroke(color, 30);
		g.pushMatrix();
		g.translate(loc.x, loc.y);
		g.scale(d);
		/* Get the direction and add 90 degrees. */
		g.rotate(vel.heading2D() - parent.parent.radians(90));
		g.beginShape();
		for (int i = 0; i <= 180; i+=20) {
			float x = parent.parent.sin(parent.parent.radians(i)) * i/3;
			float angle = parent.parent.sin(parent.parent.radians(i+s+parent.parent.frameCount*5)) * 50;
			g.vertex(x-angle, i*2);
			g.vertex(x-angle, i*2);
		}
		/* 
	     Started from the top now we are here. We need to now start to draw from where the first for loop left off. 
	     Otherwise un ugly line will appear down the middle. To see what I mean uncomment the below line and comment
	     the other line.
		 */
		for (int i = 180; i >= 0; i-=20) {
			float x = parent.parent.sin(parent.parent.radians(i)) * i/3;
			float angle = parent.parent.sin(parent.parent.radians(i + s + parent.parent.frameCount * 5)) * 50;
			g.vertex(-x-angle, i*2);
			g.vertex(-x-angle, i*2);
		}
		g.endShape();
		g.popMatrix();
	}

	public void update(float _width, float _height) {
		/* Instead of changing the velocity when the fish  */
	    if (loc.x < 0) loc.x = _width;
	    if (loc.x > _width) loc.x = 0;
	    if (loc.y < 0) loc.y = _height;
	    if (loc.y > _height) loc.y = 0;
	}

}
