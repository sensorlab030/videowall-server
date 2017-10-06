package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;

public class AgathesAnimation extends Animation {
	
	int NBVALUES = 10;
	private float[] VALUES = new float[NBVALUES];
	private final float RESOLUTION_X = (float) WallConfiguration.SOURCE_IMG_WIDTH / 100f;
	float max = Integer.MIN_VALUE;
	float min = Integer.MAX_VALUE;
	
	public AgathesAnimation(PApplet applet) {
		super(applet);
		
		for(int i = 0; i < NBVALUES; i++) {
			VALUES[i] = (float)(Math.random());

			// Find min and max in VALUES
		    if (VALUES[i] < min) { min = VALUES[i]; }
		    if (VALUES[i] > max) { max = VALUES[i]; }
		}
		
	}

	private int ColorScale(float value) {
		return (int)PApplet.map(value, min, max, 0, 255);
	}
	
	private float xScale(float value, float width) {
		return PApplet.map(value, 0, NBVALUES, 0, width);
	}
	
	private float yScale(float value, float height) {
		return PApplet.map(value, 0, max, height, 0);
	}
	
	@Override
	protected void drawAnimationFrame(PGraphics g) {
		g.background(255);
		g.noFill();
		
		for (int i = 0; i < NBVALUES; i++) {	
			// Get bars x coordinates and width
			float xIndexMin = xScale(i, g.width);
			float xIndexMax = xScale(i + 1, g.width);
			float rectWidth = xIndexMax - xIndexMin;
			
			// Get y coordinates and height
			float y = yScale(VALUES[i], g.height);
			float rectHeight = g.height - y;
			
			// Draw bars
			g.fill(PColor.color(0, ColorScale(VALUES[i]), ColorScale(VALUES[i])));
			g.rect(xIndexMin, y, rectWidth, rectHeight);
			
		}
	}

}
