package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;

public class AgathesAnimation extends Animation {
	// PARAMETERS
	int NBVALUES = 13; // Total number of bars
	int step = 40; // Bar loading speed (load 10 levels at once)
	
	// VARIABLES
	private float[] VALUES = new float[NBVALUES];
	private final float RESOLUTION_X = (float) WallConfiguration.SOURCE_IMG_WIDTH / (NBVALUES + 1);
	float max = Integer.MIN_VALUE;
	float min = Integer.MAX_VALUE;
	float[] currentHeight = new float[NBVALUES];
	float[] finalHeight = new float[NBVALUES];
	int [] colors = new int[NBVALUES];
	
	public AgathesAnimation(PApplet applet) {
		super(applet);
		
		for(int i = 0; i < NBVALUES; i++) {
			// Generate random value
			VALUES[i] = (float)(Math.random());
			
			// Generate random color
			colors[i] = generateRandomRGBColor();
			
			// Find min and max in VALUES
		    if (VALUES[i] < min) { min = VALUES[i]; }
		    if (VALUES[i] > max) { max = VALUES[i]; }
		}
		
	}
	
	private int generateRandomRGBColor() {
		int r = (int) (Math.random() * 255);
		int g = (int) (Math.random() * 255);
		int b = (int) (Math.random() * 255);
		return PColor.color(r, g, b);
	}
		
	private float yScale(float value, float height) {
		return PApplet.map(value, 0, max, 0, height);
	}
	

	@Override
	protected void drawAnimationFrame(PGraphics g) {		
		g.background(255);
		g.noFill();

 		for (int i = 0; i < NBVALUES; i++) {	
			// Get y coordinates and height
			finalHeight[i] = yScale(VALUES[i], g.height);
			
			// Get color
			g.fill(colors[i]);
			
			// Create shape
			PShape rect = g.createShape();
			rect.beginShape();
			
			// Add the 4 points to form a rectangle
			rect.vertex(i * RESOLUTION_X, g.height - currentHeight[i]);
			rect.vertex((i + 1) * RESOLUTION_X, g.height - currentHeight[i]);
			rect.vertex((i + 1) * RESOLUTION_X, g.height);
			rect.vertex(i * RESOLUTION_X, g.height);
			
			// Draw shape
			rect.endShape();
			g.shape(rect);
			
			
			// If bar has not reached its final height, add a level for next draw
			if (currentHeight[i] + step < finalHeight[i]) {
				currentHeight[i] = currentHeight[i] + step;
			} 
 		}
 		
	}

}
