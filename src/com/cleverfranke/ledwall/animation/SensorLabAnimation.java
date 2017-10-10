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
	int layoutChangeStep = 7;
	
	public SensorLabAnimation(PApplet applet) {
		super(applet);
		xWidth = getPixelWidthsOfPanels();
	}

	
	public void drawStaircase(boolean isTop){
		float yOffset = 0;
		
//		(applet.frameCount % 20 == 0);
//		System.currentTimeMillis()
//		applet.millis() 
		
		for (int i = 1; i < WallConfiguration.PANEL_COUNT; i++) {
			if (isTop) {
				drawTopBar(i, yOffset);
			} else {
				drawBottomBar(i, yOffset);
			}
			yOffset += xWidth[i];
			
			if (yOffset > WallConfiguration.SOURCE_IMG_HEIGHT) {
				yOffset = 0;
			}
		}
	}

	
	@Override
	protected void drawAnimationFrame(PGraphics g) {
		g.background(255);
		g.fill(PColor.color(19, 172, 206));
		
		drawStaircase(isTop);
		
		if (applet.frameCount % layoutChangeStep == 0) {
			isTop = !isTop;
		}
	}
}
