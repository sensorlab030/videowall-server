package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PGraphics;

public class SensorLabAnimation extends Animation {
	private final int F_DURATION = 20; 			// Animation frames duration
	private int frameCount;						// Number of frames at the start of the animation
	private boolean isTop = false;
	private boolean isMoving = true;
	private int layoutChangeStep = 7;
	private float[] yOffset = null;
	private boolean isDone = false;
	private int[] mappedPanelWidth = mapPanelWidth();
	
	public SensorLabAnimation(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, applet);
		yOffset = new float[PANEL_COUNT + 1];
		frameCount = applet.frameCount;
	}
	
	private void drawStaircase(PGraphics g, boolean isTop, float[] yOffset){

		for (int i = 0; i < PANEL_COUNT; i++) {
			if (isTop) {
				g.stroke(PColor.color(255, 0, 0));
				g.line(2*i, 0, 2*i, yOffset[i]);
				g.line(2*i+1, 0, 2*i+1, yOffset[i]);				
			} else {
				g.stroke(PColor.color(0, 150, 206));
				g.line(2*i, yOffset[i], 2*i, g.height);
				g.line(2*i+1, yOffset[i], 2*i+1, g.height);
			}
			
			yOffset[i + 1] = yOffset[i] + mappedPanelWidth[i];
			
			if (yOffset[i + 1] > graphicsContext.height) {
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
	
	public int[] mapPanelWidth() {
		int[] mapped = new int[PANEL_COUNT];
		for (int i=0; i < PANEL_COUNT; i++) {
			mapped[i] = (int) PApplet.map(PANEL_WIDTH[i], 0, WallConfiguration.SOURCE_IMG_HEIGHT, 0, WallConfiguration.ROWS_COUNT);
		}
		return mapped;
	}
}
