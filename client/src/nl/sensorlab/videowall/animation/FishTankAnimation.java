package nl.sensorlab.videowall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import jonas.Fish;
import jonas.Node;
import jonas.Particle;
import jonas.Pixel;
import jonas.TriggerObject;
import jonas.TriggerSequence;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class FishTankAnimation extends BaseAnimation {

	// Based on: https://www.openprocessing.org/sketch/162912
	public PApplet parent;

	private ArrayList<Fish> fishes;
	private int amountFishes = 10;

	public FishTankAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;

		this.fishes = new ArrayList<Fish>();
		generateFishes(amountFishes);

	}

	protected void generateFishes(int _amountFishes) {
		for(int i = 0; i < amountFishes; i++) fishes.add(new Fish(this, (float)(PIXEL_RESOLUTION_X * Math.random()), (float)(PIXEL_RESOLUTION_Y * Math.random())));
	}


	@Override
	protected void drawAnimationFrame(PGraphics g, double t) {
		// Add some fade effect
		g.noStroke();
		g.fill(0,0,128, 30);
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);

		// Update and draw
		g.strokeJoin(g.ROUND);
		for(Fish f : fishes) {
			f.update(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
			f.display( g);
		}
	}
}
