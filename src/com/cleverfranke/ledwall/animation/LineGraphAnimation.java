package com.cleverfranke.ledwall.animation;

import com.cleverfranke.util.PColor;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;


/**
 * Animation that draws 3 lines with a wave-like motion
 */
public class LineGraphAnimation extends CanvasAnimation {
	
	private final int F_DURATION = 40; 											// Nb of frames that define how long the animation lasts
	private final int LINE_COUNT = 3;											// Number of lines to plot
	private final float RESOLUTION_X = (float) graphicsContext.width / 100f;	// Resolution of x
	private float[] yOffset = new float[LINE_COUNT];							// Stores the yOffset of each line
	int frameCount;																// Stores the frame count at which the animation starts
	private int[] colors = {													// Line colors
			PColor.color(0, 255, 255), 
			PColor.color(255, 0, 255),
			PColor.color(255, 255, 0)
			};
	
	public LineGraphAnimation(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, applet);
		frameCount = applet.frameCount;

		// Initialize noise seed
		for (int i = 0; i < LINE_COUNT; i++) {
			yOffset[i] = (float) i / (float) LINE_COUNT;
		}
	}

	@Override
	public void drawAnimationFrame(PGraphics g) {
		
		g.background(255);
		g.noFill();
		g.strokeWeight((float) (0.25*g.height));
		
		for (int i = 0; i < LINE_COUNT; i++) {
			
			PShape line = g.createShape();
			line.beginShape(PConstants.LINE);
			
			float xOff = 0;
			for (float x = 0; x < g.width + 1; x += RESOLUTION_X) {
				float y = PApplet.map(applet.noise(xOff, yOffset[i]), 0, 1, 0, g.height);
				line.vertex(x,  y);
				xOff += 0.01f;
			}
			
			// Draw shape
			line.endShape();
			g.stroke(colors[i]);
			g.shape(line);
			
			// Update Y offset
			yOffset[i] += 0.01f;
			
		}	
	}
	
	public boolean isDone() {
		return (applet.frameCount > frameCount + F_DURATION);
	}
	
	@Override
	public void prepareForQueueRotation() {
		frameCount = applet.frameCount;
	}

}
