package jonas;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.ColorGridAnimation;
import processing.core.PGraphics;
import processing.core.PVector;

public class Square {

	ColorGridAnimation parent;

	PVector position;

	float dimensions;

	float colorR;
	float colorG;
	float colorB = 200; // <-- Static
	
	int color;

	public Square(ColorGridAnimation _p, float _i, float _dimensions) {
		this.parent = _p;
		this.dimensions = _dimensions;	
		this.position = new PVector(parent.PIXEL_RESOLUTION_X/2, parent.PIXEL_RESOLUTION_Y/2);
	}

	public void updateColor() {
//		colorR = parent.parent.map(parent.parent.sin(parent.parent.radians(position.y)), -1, 1, 0, 255);
//		colorG = parent.parent.map(parent.parent.sin(parent.parent.radians(position.x)), -1, 1, 0, 255);
		
		colorR = parent.parent.map(position.y, 0, parent.PIXEL_RESOLUTION_Y, 0, 255);
		colorG = parent.parent.map(position.x, 0, parent.PIXEL_RESOLUTION_X, 0, 255);
		
		color = PColor.color((int)colorR, (int)colorG, (int)colorB);
	}

	public void update() {
		updateColor();
		
		if(((position.x) < 0 || (position.x + dimensions) > parent.PIXEL_RESOLUTION_X) || ((position.y) < 0 || (position.y + dimensions) > parent.PIXEL_RESOLUTION_Y)) {
			// Reset
			position = new PVector(parent.PIXEL_RESOLUTION_X/2, parent.PIXEL_RESOLUTION_Y/2);
		}else {
			position.x += (int)parent.parent.random(-2, 2) * dimensions;
			position.y += (int)parent.parent.random(-2, 2) * dimensions;
		}
	}
	
	public void display(PGraphics g) {
		g.noStroke();
		g.fill(color, 128);
		g.rect(position.x,  position.y,  dimensions, dimensions);
	}
}
