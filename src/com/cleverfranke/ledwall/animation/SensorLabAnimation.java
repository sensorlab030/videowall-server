package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PGraphics;

public class SensorLabAnimation extends Animation {
	private final int F_DURATION = 20; 	// Animation frames duration
	private int frameCount;						// Number of frames at the start of the animation
	private boolean isTop = false;
	private boolean isMoving = true;
	private int layoutChangeStep = 7;
	private float[] yOffset = null;
	private boolean isDone = false;
	
	public SensorLabAnimation(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, false, applet);
		yOffset = new float[WallConfiguration.PANEL_COUNT + 1];
		frameCount = applet.frameCount;
	}
	
	private void drawStaircase(PGraphics g, boolean isTop, float[] yOffset){

		for (int i = 0; i < WallConfiguration.PANEL_COUNT; i++) {
			if (isTop) {
				g.fill(PColor.color(19, 110, 206));
				drawTopBar(g, i, yOffset[i]);
			} else {
				g.fill(PColor.color(0, 150, 206));
				drawBottomBar(g, i, yOffset[i]);
			}
			
			yOffset[i + 1] = yOffset[i] + PANEL_WIDTH[i];
			
			if (yOffset[i + 1] > WallConfiguration.SOURCE_IMG_HEIGHT) {
				yOffset[i + 1] = 0;
			}
		}
	}
	
	@Override
	public void drawAnimationFrame(PGraphics g) {
		g.background(255);
		g.noStroke();
		
		drawStaircase(g, isTop, yOffset);
		
		if (applet.frameCount % layoutChangeStep == 0) {
			isTop = !isTop;
			isMoving = !isMoving;
		}
		
		if (isMoving) {
			yOffset =  movePatternRight(yOffset);
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
