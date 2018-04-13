package nl.sensorlab.videowall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import jonas.Pixel;
import jonas.TriggerObject;
import jonas.TriggerSequence;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class WavesAnimation extends BaseAnimation {

	// Examples based on: https://www.openprocessing.org/sketch/133048 and https://www.openprocessing.org/sketch/500317
	PApplet parent;

	public int counter = 0;
	public float mappingHeight = PIXEL_RESOLUTION_Y;
	public float red, green, blue;
	
	float entryOffset = 0;
	float lineOffset = 0;

	public WavesAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
	}

	protected void updateColor() {
		// How does this work?
		red = parent.sin(parent.radians(counter));
		green = parent.sin(parent.radians(counter + 60));
		blue = parent.sin(parent.radians(counter + 120));

		red = parent.map(red, 0,1,0,255);
		green = parent.map(green, 0,1,0,255);
		blue = parent.map(blue, 0,1,220,255);

		counter++;
		if(counter > 500) counter = 0;
	}

	protected void drawWaves(PGraphics g) {
		// Update colors
		updateColor();
		
		// Reset offset
		entryOffset = 0;
		
		g.stroke(red, green, blue, 100);
		g.strokeWeight(3);
		g.noFill();
		g.beginShape();
		for (int x = 0; x <= PIXEL_RESOLUTION_X; x++) {
			 float y = parent.map(parent.noise(entryOffset, lineOffset), 0, 1, 0, PIXEL_RESOLUTION_Y);
			 g.vertex(x, y);
			 entryOffset += 0.25;
		}
		g.endShape();
		lineOffset += 0.015;
	}

	@Override
	protected void drawAnimationFrame(PGraphics g) {
		// Add some fade effect
		g.fill(0, 5);
		g.noStroke();
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
		// Draw the waves.
		drawWaves(g);
	}
}
