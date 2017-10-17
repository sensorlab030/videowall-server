package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;

public class BarGraphAnimation extends Animation {
	// PARAMETERS
	int NBVALUES = 10; // Total number of bars
	int step = 40; // Bar loading speed (load 10 levels at once)
	boolean useAllWidth = true; // If true then the last bar use the width left over. If false then one bar use one panel.
	
	// VARIABLES
	private float[] VALUES = new float[NBVALUES];
	float max = Integer.MIN_VALUE;
	float min = Integer.MAX_VALUE;
	float[] currentHeight = new float[NBVALUES];
	float[] finalHeight = new float[NBVALUES];
	int [] colors = new int[NBVALUES];
	float[] xPos = new float[NBVALUES + 1];
	
	
	public BarGraphAnimation(boolean inDefaultRotation, PApplet applet) {
		super(15, inDefaultRotation, applet);
		
		// Initial left x position
		xPos[0] = 0;
			
		for(int i = 0; i < NBVALUES; i++) {
			// Generate random value
			VALUES[i] = (float)(Math.random() + 0.1);
			
			// Generate random color
			colors[i] = generateRandomRGBColor();
			
			// Find min and max in VALUES
		    if (VALUES[i] < min) { min = VALUES[i]; }
		    if (VALUES[i] > max) { max = VALUES[i]; }

			// Get x right position of each panel
		    if (i != 0) {
		    	xPos[i] = WallConfiguration.PHYSICAL_PANEL_WIDTH_CM[i-1] * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO + xPos[i-1];
		    }
		}
		
		// Initial right end position
		if (useAllWidth) {
			xPos[NBVALUES] = WallConfiguration.SOURCE_IMG_WIDTH;
		} else {
			xPos[NBVALUES] = WallConfiguration.PHYSICAL_PANEL_WIDTH_CM[NBVALUES-1] * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO + xPos[NBVALUES-1];
		}
		
	}
		
	
	public void drawAnimationFrame(PGraphics g) {		
		g.background(255);
		g.noFill();
		
 		for (int i = 0; i < NBVALUES; i++) {	
			// Get y coordinates and height
			finalHeight[i] = PApplet.map(VALUES[i], 0, max, 0, g.height);
			
			// Get color
			g.fill(colors[i]);
			
			// Create shape
			PShape rect = g.createShape();
			rect.beginShape();
						
			// Add the 4 points to form a rectangle
			rect.vertex(xPos[i], g.height - currentHeight[i]);
			rect.vertex(xPos[i + 1], g.height - currentHeight[i]);
			rect.vertex(xPos[i + 1], g.height);
			rect.vertex(xPos[i], g.height);
			
			// Draw shape
			rect.endShape();
			g.shape(rect);
			
			
			// If bar has not reached its final height, add a level for next draw
			if (currentHeight[i] + step < finalHeight[i]) {
				currentHeight[i] = currentHeight[i] + step;
			} else {
				currentHeight[i] = finalHeight[i];
			}
 		}
 		
	}

}
