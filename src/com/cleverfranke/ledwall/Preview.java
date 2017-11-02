package com.cleverfranke.ledwall;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Class that build a preview of the led wall, with real panel sizes and beams, based on the leds pixels
 */
public class Preview {
	public static float XPANELCOORD[] = WallConfiguration.XPANELCOORD; // X coordinates of each panels

	/**
	 * Creates a projected image of a led pixel grid to the 'real' led wall
	 * @param applet
	 * @param sourceImage : Original led pixel grid (measures number of led strips x number of led per strip)
	 * @return projectedImage: Image the size of the led wall, with each led strip assigned to the left or the right side of a beam
	 */
	public PImage createPreview(PApplet applet, PImage sourceImage) {
		// Create graphics context the size of the led wall
		PGraphics g = applet.createGraphics(WallConfiguration.SOURCE_IMG_WIDTH, WallConfiguration.SOURCE_IMG_HEIGHT);
		g.noSmooth();

		g.beginDraw();
		g.background(255);
		drawBeams(g);
		drawProjectedGridToPreview(g, sourceImage);
		g.endDraw();

		return g.get();
	}


	/**
	 * Project an image of 26x93 (resolution) on the wall LED struture, and draws it on the Preview canvas
	 * @param g : Graphics context,
	 * @param grid: grid of 26x93 pixels
	 */
	private static void drawProjectedGridToPreview(PGraphics g, PImage grid){
		int width = grid.width;
		int height = grid.height;
		final int LED_PASSTHROUGH = 70;
		final int LED_BAFFLE_DIFF = 5;
		final int ledStop = LED_PASSTHROUGH / 2;

		g.strokeWeight(3);
		g.stroke(255, 0, 0);

		/**
		 *  For all the pixels in grid, we are going to assign that pixel to a led position on the wall.
		 *  This position is defined by :
		 *  - The column index in the grid (which defines the x coordinates, or, on which panel the pixel will be projected.)
		 *  - The row index in the grid (which defines the y coordinates on the given panel)
		 */
		for (int i = 0; i < width; i++){
			// Get panel Index
			int panelIndex = (int) Math.ceil((float)i/2);

			// If panelIndex is odd or even, define x coordinates as the left side or the right side of the beam
			int x = (i%2 == 0) ? (int) (XPANELCOORD[panelIndex] + WallConfiguration.BEAM_WIDTH / 2) : (int) (XPANELCOORD[panelIndex]) - WallConfiguration.BEAM_WIDTH / 2 - 1;

			// For each row in the grid, get the corresponding pixel, and assign it to it's final position on the projection
			for (int j = 0; j < height; j++){
				int pixel = grid.get(i, j);

				for (int xDiff = 0; xDiff < ledStop; xDiff += LED_BAFFLE_DIFF) {

					int alpha = (int) Math.floor((1 - (float) xDiff / ledStop) * 255);
					g.stroke(pixel, alpha);
					g.point((i%2 == 0) ? x + xDiff : x - xDiff, j*WallConfiguration.ROW_HEIGHT + 2);
				}

			}
		}
	}



	/**
	 * Draws the beams on the preview image
	 * @param g : Graphics context
	 */
	private static void drawBeams(PGraphics g) {
		g.noStroke();
		g.fill(230);

		float x = 0;
		for (int i = 0; i < WallConfiguration.PANEL_COUNT; i++) {
			final float panelWidth = WallConfiguration.PANEL_WIDTH[i];

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
