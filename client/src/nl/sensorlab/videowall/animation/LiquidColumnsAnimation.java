package nl.sensorlab.videowall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import jonas.Column;
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

public class LiquidColumnsAnimation extends BaseAnimation {

	public PApplet parent;

	public PVector pushObject = new PVector(10,10);
	public float pushSpeed;
	public float pushRadius;

	ArrayList<Column> columns;

	public LiquidColumnsAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		this.columns = new ArrayList<Column>();

		// Generate the columns..
		generateColumns();

		// Generate push object; maybe we can use webcam imagery?
		setPushObject();
	}


	protected void setPushObject() {
		pushRadius = parent.random(3, 20);
		pushObject = new PVector(-pushRadius, parent.random(PIXEL_RESOLUTION_Y));
		pushSpeed = parent.random(0.05f, 0.5f);
	}

	protected void updatePushObject() {
		pushObject.x += pushSpeed;
		if(pushObject.x > (PIXEL_RESOLUTION_X + pushRadius)) {
			setPushObject();
		}
	}

	protected void drawPushObject(PGraphics g) {
		g.noStroke();
		g.fill(255,0,0);
		g.ellipse(pushObject.x, pushObject.y, pushRadius,pushRadius);
	}

	protected void generateColumns() {
		for(int i = 0; i < PIXEL_RESOLUTION_X; i++) {
			columns.add(new Column(this, i, 1.75f));
		}
	}

	protected void drawColumns(PGraphics g) {
		for(Column c : columns) {
			c.update();
			c.display(g);	
		}
	}

	@Override
	protected void drawAnimationFrame(PGraphics g) {
		g.fill(0, 64);
		g.noStroke();
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
		drawColumns(g);
		updatePushObject();
//		drawPushObject(g);
	}
}
