package nl.sensorlab.videowall.animation.baseanimations;
import java.util.ArrayList;

import nl.sensorlab.videowall.animation.BaseAnimation;
import nl.sensorlab.videowall.animation.baseanimations.liquidcolumns.LiquidColumn;
import nl.sensorlab.videowall.animation.baseanimations.liquidcolumns.PushObject;
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
		this.pushobject = new PushObject(this);
		this.liquidcolumns = new ArrayList<LiquidColumn>();
		generateColumns();
	}

	private void generateColumns() {
		for(int i = 0; i < PIXEL_RESOLUTION_X; i++) liquidcolumns.add(new LiquidColumn(this, i, 1.75f));
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double dt) {
		// Fade
		g.fill(0, 64);
		g.noStroke();
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);

		// Draw columns
		for(LiquidColumn lc : liquidcolumns) {
			lc.update();
			lc.draw(g);
		}

		// Update pushObject
		pushobject.update();
	}	
}