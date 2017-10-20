package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;

import processing.core.PApplet;
import processing.core.PGraphics;

public class GridTest extends Animation {
	
	public GridTest(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, true, applet);
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void drawAnimationFrame(PGraphics g) {
		g.background(255);
		g.noFill();
		g.strokeWeight(1);
		
		for (int i = 0; i < WallConfiguration.COLUMNS_COUNT; i++) {
			int color = generateRandomRGBColor();
			g.stroke(color);
			g.line(i, 0, i, WallConfiguration.ROWS_COUNT);
		}
		
	}

	@Override
	public void prepareForQueueRotation() {
		// TODO Auto-generated method stub
		
	}

}
