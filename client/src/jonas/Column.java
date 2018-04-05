package jonas;

import nl.sensorlab.videowall.animation.LiquidColumnsAnimation;
import processing.core.PGraphics;
import processing.core.PVector;

public class Column {

	LiquidColumnsAnimation parent;

	private PVector[] positions;
	public float speed;

	public Column(LiquidColumnsAnimation _p, float _x, float _speed) {
		this.parent = _p;
		this.positions = new PVector[parent.PIXEL_RESOLUTION_Y];
		this.speed = _speed;
		for(int y = 0; y < parent.PIXEL_RESOLUTION_Y; y++) {
			this.positions[y] = new PVector(_x, y);
		}
	}

	public void update() {
		// Ignore first node
		for (int i = 1; i < positions.length; i++) {

			// Update the y position
			positions[i].y += speed;

			// Get the distance
			float distance = PVector.dist(parent.pushObject, positions[i]);

			// Push nodes away
			if(distance < parent.pushRadius){
				PVector temp = parent.pushObject.copy();
				temp.sub(positions[i]);
				temp.normalize();
				temp.mult(parent.pushRadius);
				temp = PVector.sub(parent.pushObject, temp);
				positions[i] = temp.copy();
			}
		}

		for (int i = 1; i < positions.length; i++) {
			PVector temp = PVector.sub(positions[i-1], positions[i]);
			temp.normalize();
			temp.mult(1);
			positions[i] = PVector.sub(positions[i-1], temp);
		}
	}

	public void display(PGraphics g) {
		g.noFill();
		g.stroke(255,200);
		g.beginShape();
		for (PVector p : positions) {
			g.vertex(p.x, p.y);
		}
		g.endShape();
	}
}
