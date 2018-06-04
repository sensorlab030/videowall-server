package nl.sensorlab.videowall.animation.baseanimations;
import java.util.ArrayList;
import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class LiquidColumnsAnimation  extends BaseAnimation {

	private PApplet parent;
	private PushObject pushobject;
	private ArrayList<LiquidColumn> liquidcolumns;

	public LiquidColumnsAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		this.pushobject = new PushObject();
		this.liquidcolumns = new ArrayList<LiquidColumn>();
		generateColumns();
	}

	private void generateColumns() {
		for(int i = 0; i < PIXEL_RESOLUTION_X; i++) {
			liquidcolumns.add(new LiquidColumn(pushobject, i, .0175f));
		}
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double dt) {
		// Fade
		g.fill(0, 64);
		g.noStroke();
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);

		// Draw columns
		for(LiquidColumn lc : liquidcolumns) {
			lc.updateAndDraw(g, dt);
		}

		// Update pushObject
		pushobject.update(dt);
	}

	public class LiquidColumn {

		private PushObject pushobject; 
		private PVector[] positions;
		private float speed;

		public LiquidColumn(PushObject pushobject, float x, float speed) {
			this.pushobject = pushobject;
			this.positions = new PVector[BaseAnimation.PIXEL_RESOLUTION_Y];
			this.speed = speed;
			for(int y = 0; y < BaseAnimation.PIXEL_RESOLUTION_Y; y++) {
				this.positions[y] = new PVector(x, y);
			}
		}

		public void updateAndDraw(PGraphics g, double dt) {
			// No fill before loop.
			g.noFill();

			// Ignore first PVector which keep the other PVectors in check
			for (int i = 1; i < positions.length; i++) {
				// The current PVector that needs updating.
				PVector current = positions[i];

				// Update the y position
				current.y += speed * dt;

				// Get the distance
				float distance = PVector.dist(pushobject.position, current);

				// Push nodes when collide
				if(distance < pushobject.radius){
					PVector temp = pushobject.position.copy();

					// Determine the position of the push node
					temp.sub(current);

					// Normalize the vector to a length of 1
					temp.normalize();

					// Multiply the vector by the pushobject radius 
					temp.mult(pushobject.radius);

					// Set the position based on the position of the pushobject and the offset of the radius
					temp = PVector.sub(pushobject.position, temp);

					// Update the PVector
					current.x = temp.x;
					current.y = temp.y;
				}

				// Attract the PVector to the previous PVector in-line; this will keep the columns 'togehter'
				PVector temp = PVector.sub(positions[i-1], current);
				// Normalize the vector to a length of 1
				temp.normalize();
				temp = PVector.sub(positions[i-1], temp);
 
				// Update the PVector
				current.x = temp.x;
				current.y = temp.y;

				// Draw
				float colorR = PApplet.map(current.y, 0, BaseAnimation.PIXEL_RESOLUTION_Y, 0, 255);
				float colorG = PApplet.map(current.x, 0, BaseAnimation.PIXEL_RESOLUTION_X, 0, 255);
				g.stroke(colorR, colorG, 128, 200);
				g.point(current.x, current.y);
			}
		}

	}

	public class PushObject {
		private PVector position;
		private float speed;
		private float radius;

		public PushObject(){
			reset();
		}

		public void reset() {
			this.radius = (float)(3 + Math.random() * 20);
			this.speed = (float)(0.0025f + Math.random() * 0.0075f);
			this.position = new PVector(-this.radius, (float)(Math.random() * BaseAnimation.PIXEL_RESOLUTION_Y));
		}

		public void update(double dt) {
			this.position.x += this.speed * dt;
			if(this.position.x > (BaseAnimation.PIXEL_RESOLUTION_X + radius)) {
				reset();
			}
		}
	}

}

