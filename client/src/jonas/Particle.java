package jonas;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.SwirlAnimation;
import processing.core.PGraphics;
import processing.core.PVector;

public class Particle {

	public SwirlAnimation parent;
	public PVector position;
	public int color;

	public Particle(SwirlAnimation _parent) {
		this.parent = _parent;
		this.position = new PVector((float)(Math.random() * parent.PIXEL_RESOLUTION_X), (float)(Math.random() * parent.PIXEL_RESOLUTION_Y));
		this.color = PColor.color((int)(((50 + (Math.random() * 30)) + parent.baseColor) % 255), (int)(100 + (Math.random() * 155)), 255, 150);
	}

	public void update(float _d) {
		PVector shift =  new PVector();

		float x = position.x - (parent.PIXEL_RESOLUTION_X/2);
		float y = position.y - (parent.PIXEL_RESOLUTION_Y/2);

		shift.x = y;
		shift.y = -(float)Math.sin(x * (Math.PI * 2) / parent.PIXEL_RESOLUTION_X) * parent.PIXEL_RESOLUTION_Y/8;

		shift.normalize();
		shift.mult(_d * 100);

		position.add(shift);

		// Check if within bounds
		position.x = (position.x > parent.PIXEL_RESOLUTION_X ? 0 : position.x < 0 ? parent.PIXEL_RESOLUTION_X : position.x);

		// Change colors
		//		color = PColor.color(((parent.parent.hue(color) + parent.baseColor) % 255), parent.parent.saturation(color),parent.parent.brightness(color), 150);
		color =  PColor.color((int)(((50 + (Math.random() * 30)) + parent.baseColor) % 255), (int)(100 + (Math.random() * 155)), 255, 120);
		//
		//		float h = ((parent.parent.hue(color) + 1) % 360);
		//		float s = parent.parent.saturation(color);
		//		float b = parent.parent.brightness(color);
		//
		//		color = PColor.hsb(h, s , b);

	}

	public void display(PGraphics g) {
		g.noFill();
		g.strokeWeight(2);
		g.stroke(color);
		g.point(position.x, position.y);
	}

}
