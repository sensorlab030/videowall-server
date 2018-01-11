package com.cleverfranke.ledwall.animation;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

public class ComplementaryColors extends BaseCanvasAnimation {
	
	int b1, b2, c1, c2;
	int h, s, b, diam, xpos, ypos, countert, maxtime;
	
	boolean initialized;

	public ComplementaryColors(PApplet applet) {
		super(applet);
	
		Random generator = new Random(applet.millis());
		
		h = generator.nextInt(360);
		s = 100;
		b = 100;
		countert = 0;
		maxtime = 900;
	 
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		
		if (!initialized) {
			diam = g.height;
			xpos = 0;
			ypos = g.height / 2;
			g.background(0);
			initialized = true;
		}
		
		g.noStroke();
		g.rectMode(PConstants.CENTER);
		g.colorMode(PConstants.HSB, 360, 100, 100);
		
		countert = (applet.frameCount) % maxtime;

		// scaling en moving
		diam = diam + diam;
		xpos = (xpos + 1) % g.width;
		ypos = (ypos + 1) % g.height;

		int pompi = applet.color(h, s, b);
		g.fill(pompi);
		if (diam > g.height * 2) {
			diam = 1;
			// contrasting color change
			h = (h + 175) % 360;
		}

		// change scene after certain count;
		if (countert < maxtime / 3) {
			g.rect(xpos, g.height/2, diam, diam);
		} else if (countert < maxtime / 3 * 2) {
			g.rect(xpos*2 % g.width, g.height/2, diam, g.height);
		} else {
			g.rect(g.width/2, ypos, g.width, diam);
		}
	}

}
