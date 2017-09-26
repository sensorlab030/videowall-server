package com.cleverfranke.ledwall;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class Preview {
	
	public static PImage createPreview(PApplet applet, PImage sourceImage) {
		
		PGraphics g = applet.createGraphics(sourceImage.width, sourceImage.height);
		g.beginDraw();
		g.background(255);
		drawPixels(g, sourceImage);
		drawBeams(g);
		g.endDraw();
		
		return g.get();
	}
	
	private static void drawPixels(PGraphics g, PImage sourceImage) {
		
		final int BEAM_WIDTH = WallConfiguration.PHYSICAL_BEAM_WIDTH_CM * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO;
		final int LED_PASSTHROUGH = 70;
		final int LED_BAFFLE_DIFF = 5;
		
		g.strokeWeight(3);
		g.stroke(255, 0, 0);
		
		float x = 0;
		for (int i = 0; i < WallConfiguration.PANEL_COUNT; i++) {
			final int panelWidth = WallConfiguration.PHYSICAL_PANEL_WIDTH_CM[i] * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO;
			
			// Loop over pixels
			for (int pixelY = WallConfiguration.PHYSICAL_PIXEL_OFFSET_CM * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO; pixelY < WallConfiguration.SOURCE_IMG_HEIGHT; pixelY += WallConfiguration.PHYSICAL_PIXEL_PITCH_CM * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO) {
				
				// Left pixels
				float xOffsetLeft = x + BEAM_WIDTH / 2;
				g.stroke(sourceImage.get((int) xOffsetLeft, pixelY));
				for (int xDiff = 0; xDiff < LED_PASSTHROUGH; xDiff += LED_BAFFLE_DIFF) {
					g.point(xOffsetLeft + xDiff, pixelY);
				}
				
				// Right pixels
				float xOffsetRight = x + panelWidth - BEAM_WIDTH / 2 - 1;
				g.stroke(sourceImage.get((int) xOffsetLeft, pixelY));
				for (int xDiff = 0; xDiff < LED_PASSTHROUGH; xDiff += LED_BAFFLE_DIFF) {
					g.point(xOffsetRight - xDiff, pixelY);
				}
				
			}
			
			x += panelWidth;
			
		}
		
	}
	
	private static void drawBeams(PGraphics g) {
		
		final int beamWidth = WallConfiguration.PHYSICAL_BEAM_WIDTH_CM * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO;
		
		g.noStroke();
		g.fill(230);
		
		float x = 0;
		for (int i = 0; i < WallConfiguration.PANEL_COUNT; i++) {
			final int panelWidth = WallConfiguration.PHYSICAL_PANEL_WIDTH_CM[i] * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO;

			// Draw left beam
			if (i == 0) {
				g.rect(0, 0, beamWidth / 2, WallConfiguration.SOURCE_IMG_HEIGHT);
			} 
			
			// Draw right beam
			if (i == WallConfiguration.PANEL_COUNT - 1) {
				// Draw right beam at the end of the wall
				g.rect(x + panelWidth - beamWidth / 2, 0, beamWidth / 2, WallConfiguration.SOURCE_IMG_HEIGHT);
			} else {
				// Draw right beam extending to next panel
				g.rect(x + panelWidth - beamWidth / 2, 0, beamWidth, WallConfiguration.SOURCE_IMG_HEIGHT);
			}
			
			x += panelWidth;
			
		}
	}

}
