package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;

public class LineGraphAnimation extends Animation {
	
	private final int F_DURATION = 40; // Nb of frames
	private final int LINE_COUNT = 3;
	private final float RESOLUTION_X = (float) WallConfiguration.SOURCE_IMG_WIDTH / 100f;
	private boolean isDone;
	private float[] yOffset = new float[LINE_COUNT];
	int frameCount;
	
	private int[] colors = {
			PColor.color(0, 255, 255), 
			PColor.color(255, 0, 255),
			PColor.color(255, 255, 0)
			};
	
	public LineGraphAnimation(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, applet);
		frameCount = applet.frameCount;
		
		this.isDone = false;
		// Initialize noise seed
		for (int i = 0; i < LINE_COUNT; i++) {
			yOffset[i] = (float) i / (float) LINE_COUNT;
		}
	}

	@Override
	public void drawAnimationFrame(PGraphics g) {
		
		g.background(255);
		g.noFill();
		g.strokeWeight(100);
		
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
		
		if (applet.frameCount > frameCount + F_DURATION) {
			isDone = true;
		}
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	@Override
	public void prepareForQueueRotation() {
		isDone = false;
		frameCount = applet.frameCount;
	}

}
