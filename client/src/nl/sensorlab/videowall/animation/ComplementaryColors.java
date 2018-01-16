package nl.sensorlab.videowall.animation;

import java.util.Random;

import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class ComplementaryColors extends BaseCanvasAnimation {
	
	private final int MAX_TIME = 900;
	private float hue; 
	private int size, xpos, ypos, counter;
	private boolean initialized;

	public ComplementaryColors(PApplet applet) {
		super(applet);
	
		// Random starting color
		Random generator = new Random(System.currentTimeMillis());
		hue = generator.nextFloat();
	 
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		
		if (!initialized) {
			size = g.height;
			xpos = 0;
			ypos = g.height / 2;
			g.background(0);
			initialized = true;
		}
		
		g.noStroke();
		g.rectMode(PConstants.CENTER);
		counter = (applet.frameCount) % MAX_TIME;

		// scaling en moving
		size = size + size;
		xpos = (xpos + 1) % g.width;
		ypos = (ypos + 1) % g.height;

		g.fill(PColor.hsb(hue, 1f, 1f));
		if (size > g.height * 2) {
			size = 1;
			hue = (hue + .495f) % 1f;
		}

		// change scene after certain count;
		if (counter < MAX_TIME / 3) {
			g.rect(xpos, g.height/2, size, size);
		} else if (counter < MAX_TIME / 3 * 2) {
			g.rect(xpos*2 % g.width, g.height/2, size, g.height);
		} else {
			g.rect(g.width/2, ypos, g.width, size);
		}
	}

}
