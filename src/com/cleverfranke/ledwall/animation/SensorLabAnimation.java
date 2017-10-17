package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;

public class SensorLabAnimation extends Animation {
	float[] xWidth = null;
	boolean isTop = false;
	boolean isMoving = true;
	int layoutChangeStep = 7;
	float[] yOffset = null;
	
	public SensorLabAnimation(boolean inDefaultRotation, PApplet applet) {
		super(15, inDefaultRotation, applet);
		xWidth = getPixelWidthsOfPanels();
		yOffset = new float[WallConfiguration.PANEL_COUNT + 1];
	}
	
//	(applet.frameCount % 20 == 0);
//	System.currentTimeMillis()
//	applet.millis() 

	
	public void drawStaircase(PGraphics g, boolean isTop, float[] yOffset){

		for (int i = 0; i < WallConfiguration.PANEL_COUNT; i++) {
			if (isTop) {
				drawTopBar(g, i, yOffset[i]);
			} else {
				drawBottomBar(g, i, yOffset[i]);
			}
			
			yOffset[i + 1] = yOffset[i] + xWidth[i];
			
			if (yOffset[i + 1] > WallConfiguration.SOURCE_IMG_HEIGHT) {
				yOffset[i + 1] = 0;
			}
		}
	}
	
	@Override
	public void drawAnimationFrame(PGraphics g) {
		g.background(255);
		g.fill(PColor.color(19, 172, 206));
		
		drawStaircase(g, isTop, yOffset);
		
		if (applet.frameCount % layoutChangeStep == 0) {
			isTop = !isTop;
			isMoving = !isMoving;
		}
		
		if (isMoving) {
			yOffset =  movePatternRight(yOffset);
		}
		

	}
}
