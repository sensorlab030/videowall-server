package nl.sensorlab.videowall.animation.baseanimations.liquidcolumns;
import processing.core.PGraphics;
import processing.core.PVector;

public class LiquidColumn {

	private LiquidColumnsAnimation parent; 

	private PVector[] positions;
	private float speed;

	public LiquidColumn(LiquidColumnsAnimation p, float x, float speed) {
		this.parent = p;
		this.positions = new PVector[parent.PIXEL_RESOLUTION_Y];
		this.speed = speed;
		for(int y = 0; y < parent.PIXEL_RESOLUTION_Y; y++) {
			this.positions[y] = new PVector(x, y);
		}
	}

	public void update() {
		// Ignore first node
		for (int i = 1; i < positions.length; i++) {
			// Update the y position
			positions[i].y += speed;

			// Get the distance
			float distance = PVector.dist(parent.pushobject.position, positions[i]);

			// Push nodes away
			if(distance < parent.pushobject.radius){
				PVector temp = parent.pushobject.position.copy();
				temp.sub(positions[i]);
				temp.normalize();
				temp.mult(parent.pushobject.radius);
				temp = PVector.sub(parent.pushobject.position, temp);
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
	
	
	public void draw(PGraphics g) {
		g.noFill();
		for (PVector p : positions) {
			float colorR = parent.parent.map( p.y, 0, parent.PIXEL_RESOLUTION_Y, 0, 255);
			float colorG = parent.parent.map(p.x, 0, parent.PIXEL_RESOLUTION_X, 0, 255);
			g.stroke(colorR, colorG, 128, 200);
			g.point(p.x, p.y);
		}
	}
	
}
