package com.cleverfranke.ledwall;

import com.cleverfranke.ledwall.animation.Animation;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class Preview {
	public static float XPANELCOORD[] = Animation.getXCoordOfPanels(); // X coordinates of each panels
	
	public static PImage createPreview(PApplet applet, PImage sourceImage) {
		PImage projectedImage = projectGridToPreview(sourceImage);
		
		PGraphics g = applet.createGraphics(projectedImage.width, projectedImage.height);
		g.beginDraw();
		g.background(255);
		drawPixels(g, projectedImage);
		drawBeams(g);
		g.endDraw();
		
		return g.get();
	}
	
	/**
	 * Scale an image of 26x93 (resolution) of the wall to the image preview sizes
	 * @param grid of 26x93 pixels
	 * @return wall, an image of grid projected on the wall beams
	 */
	private static PImage projectGridToPreview(PImage grid){
		
		PImage wall = new PImage(WallConfiguration.SOURCE_IMG_WIDTH, WallConfiguration.SOURCE_IMG_HEIGHT);
		for (int i = 0; i< grid.width; i++){
			int x = 0;
			int panelIndex = (int) Math.ceil((float)i/2);
			
			for (int j = 0; j < grid.height; j++){
				int pixel = grid.get(i, j);
				
				if (i%2 == 0) {
					x = (int) (XPANELCOORD[panelIndex] + WallConfiguration.BEAM_WIDTH / 2);
				} else {
					x = (int) (XPANELCOORD[panelIndex]) - WallConfiguration.BEAM_WIDTH / 2 - 1;
				}
				
				wall.set(x, j*WallConfiguration.ROW_HEIGHT + 2, pixel);			
			}
		}
		return wall;
	}
	
	private static void drawPixels(PGraphics g, PImage sourceImage) {
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
				float xOffsetLeft = x + WallConfiguration.BEAM_WIDTH / 2;
				g.stroke(sourceImage.get((int) xOffsetLeft, pixelY));
				for (int xDiff = 0; xDiff < LED_PASSTHROUGH; xDiff += LED_BAFFLE_DIFF) {
					g.point(xOffsetLeft + xDiff, pixelY);
				}
				
				// Right pixels
				float xOffsetRight = x + panelWidth - WallConfiguration.BEAM_WIDTH / 2 - 1;
				g.stroke(sourceImage.get((int) xOffsetRight, pixelY));
				for (int xDiff = 0; xDiff < LED_PASSTHROUGH; xDiff += LED_BAFFLE_DIFF) {
					g.point(xOffsetRight - xDiff, pixelY);
				}
				
			}
			
			x += panelWidth;
			
		}
		
	}
	
	private static void drawBeams(PGraphics g) {
		g.noStroke();
		g.fill(230);
		
		float x = 0;
		for (int i = 0; i < WallConfiguration.PANEL_COUNT; i++) {
			final int panelWidth = WallConfiguration.PHYSICAL_PANEL_WIDTH_CM[i] * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO;

			// Draw left beam
			if (i == 0) {
				g.rect(0, 0, WallConfiguration.BEAM_WIDTH / 2, WallConfiguration.SOURCE_IMG_HEIGHT);
			} 
			
			// Draw right beam
			if (i == WallConfiguration.PANEL_COUNT - 1) {
				// Draw right beam at the end of the wall
				g.rect(x + panelWidth - WallConfiguration.BEAM_WIDTH / 2, 0, WallConfiguration.BEAM_WIDTH / 2, WallConfiguration.SOURCE_IMG_HEIGHT);
			} else {
				// Draw right beam extending to next panel
				g.rect(x + panelWidth - WallConfiguration.BEAM_WIDTH / 2, 0, WallConfiguration.BEAM_WIDTH, WallConfiguration.SOURCE_IMG_HEIGHT);
			}
			
			x += panelWidth;
			
		}
	}

}
