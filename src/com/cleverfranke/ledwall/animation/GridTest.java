package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;

import processing.core.PApplet;
import processing.core.PGraphics;

public class GridTest extends Animation {
	int colors[] = new int[WallConfiguration.COLUMNS_COUNT];
	
	public GridTest(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, true, applet);
		
		for (int i = 0; i < WallConfiguration.COLUMNS_COUNT; i++) {
			int color = generateRandomRGBColor();
			colors[i] = color;
		}
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
			g.stroke(colors[i]);
			g.line(i, 0, i, WallConfiguration.ROWS_COUNT);
		}
		
	}

	@Override
	public void prepareForQueueRotation() {
		// TODO Auto-generated method stub
		
	}

}
