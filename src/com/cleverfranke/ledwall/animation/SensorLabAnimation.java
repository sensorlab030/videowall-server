package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;

public class SensorLabAnimation extends Animation {
	
	
	public SensorLabAnimation(PApplet applet) {
		super(applet);
		
		
	}


	
	@Override
	protected void drawAnimationFrame(PGraphics g) {
		g.background(255);
		g.fill(PColor.color(19, 172, 206));
		
		int panelId = 1;
		float panelWidthCm = WallConfiguration.PHYSICAL_PANEL_WIDTH_CM[panelId];
		float[] xPos = getXCoordOfPanels();
		
			
		// Draw square
		// Create shape
		PShape rect = g.createShape();
		rect.beginShape();
		
		// Add the 4 points to form a rectangle
		rect.vertex(0, 150);
		rect.vertex(0, 300);
		rect.vertex(150, 300);
		rect.vertex(150, 150);
	
		// Draw shape
		rect.endShape();
		g.shape(rect);
	}
}
