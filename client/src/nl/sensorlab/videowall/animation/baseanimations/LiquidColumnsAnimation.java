package nl.sensorlab.videowall.animation.baseanimations;
import java.util.ArrayList;
import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class LiquidColumnsAnimation  extends BaseAnimation {

	public PApplet parent;

	public PushObject pushobject;
	ArrayList<LiquidColumn> liquidcolumns;

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
			lc.update(dt);
			lc.draw(g);
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

		public void update(double dt) {
			// Ignore first node
			for (int i = 1; i < positions.length; i++) {
				// Update the y position
				positions[i].y += speed * dt;

				// Get the distance
				float distance = PVector.dist(pushobject.position, positions[i]);

				// Push nodes away
				if(distance < pushobject.radius){
					PVector temp = pushobject.position.copy();
					temp.sub(positions[i]);
					temp.normalize();
					temp.mult(pushobject.radius);
					temp = PVector.sub(pushobject.position, temp);
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
				float colorR = PApplet.map( p.y, 0, BaseAnimation.PIXEL_RESOLUTION_Y, 0, 255);
				float colorG = PApplet.map(p.x, 0, BaseAnimation.PIXEL_RESOLUTION_X, 0, 255);
				g.stroke(colorR, colorG, 128, 200);
				g.point(p.x, p.y);
			}
		}
		
	}

	
	public class PushObject {
		
		public PVector position;
		public float speed;
		public float radius;
		
		public PushObject(){
			reset();
		}
		
		public void reset() {
			this.radius = (float)(3 + Math.random() * 20);
			this.speed = (float)(0.005f + Math.random() * 0.025f);
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

