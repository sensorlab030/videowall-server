package com.cleverfranke.ledwall;

import java.util.Arrays;

/**
 * Class that stores the configuration of the wall
 */
public class WallConfiguration {

	// Physical dimensions in centimeters
	public static final int[] PHYSICAL_PANEL_WIDTH_CM = {63, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 74}; 	// Width for each panel (index 0 is leftmost panel)
	public static final int PHYSICAL_WALL_WIDTH_CM = Arrays.stream(PHYSICAL_PANEL_WIDTH_CM).sum();				// Width of  complete wall width in cm (sum of all panel widths)
	public static final int PHYSICAL_WALL_HEIGHT_CM = 280;														// Height of complete wall in cm
	public static final int PHYSICAL_BEAM_WIDTH_CM = 4;															// Width of the beams between two panels in cm
	public static final int PHYSICAL_PIXEL_OFFSET_CM = 10;														// Distance between ceiling and first pixel in cm
	public static final int PHYSICAL_PIXEL_PITCH_CM = 3;														// Distance between two pixels in cm
	public static final int PANEL_COUNT = 13;																	// Number of panels

	// Source/preview image dimensions (pixels)
	public static final int SOURCE_CM_TO_PIXEL_RATIO = 2;														// Conversion ratio from cm to pixels
	public static final int SOURCE_IMG_WIDTH = PHYSICAL_WALL_WIDTH_CM * SOURCE_CM_TO_PIXEL_RATIO;				// Source image width for the wall configuration
	public static final int SOURCE_IMG_HEIGHT = PHYSICAL_WALL_HEIGHT_CM * SOURCE_CM_TO_PIXEL_RATIO;				// Source image height for the wall configuration
	public static final int BEAM_WIDTH = PHYSICAL_BEAM_WIDTH_CM * SOURCE_CM_TO_PIXEL_RATIO;						// Beam width in pixel

	// Computed values
	public static final int RATIO = SOURCE_IMG_WIDTH / SOURCE_IMG_HEIGHT;										// Width / height ratio
	public static final int ROWS_COUNT = (int) Math.floor(PHYSICAL_WALL_HEIGHT_CM / PHYSICAL_PIXEL_PITCH_CM); 	// Number of led rows
	public static final int COLUMNS_COUNT = PANEL_COUNT * 2; 													// Number of led columns
	public static final int ROW_HEIGHT = (int) Math.floor(SOURCE_IMG_HEIGHT / ROWS_COUNT);						// Row height



	// Arrays containing coordinates and width of the panels and panel sides
	// They only depend on the parameters mentionned above
	// (only need to be calculated once)
	public static final float XPANELSIDESCOORD[] = getXCoordOfPanelSides();										// X coordinates of each sides of the panels
	public static final float XPANELCOORD[] = getXCoordOfPanels();												// X coordinates of each panels
	public static final float PANEL_WIDTH[] = getPixelWidthsOfPanels();											// Width in pixels of each panels


	/**
	 * Return an array that contains the X coordinates of the panel boundaries.
	 * @return xPos
	 */
	private static float[] getXCoordOfPanels() {
		// Array the will contain the x coordinates of all the panels
		float xPos[] = new float[PANEL_COUNT + 1];

		// Initial left x position
		xPos[0] = 0;

		for(int i = 1; i < PANEL_COUNT; i++) {
			// Get x right position of each panel
		    xPos[i] = PHYSICAL_PANEL_WIDTH_CM[i-1] * SOURCE_CM_TO_PIXEL_RATIO + xPos[i-1];
		}

		// Initial right end position
		xPos[PANEL_COUNT] = PHYSICAL_PANEL_WIDTH_CM[PANEL_COUNT-1] * SOURCE_CM_TO_PIXEL_RATIO + xPos[PANEL_COUNT-1];

		return xPos;
	}


	/**
	 * Return an array that contains the width in pixels of the panels
	 * @return xWidth
	 */
	private static float[] getPixelWidthsOfPanels() {
		// Array the will contain the x coordinates of all the panels
		float xWidth[] = new float[PANEL_COUNT];

		for(int i = 0; i < PANEL_COUNT; i++) {
			// Get x width
			xWidth[i] = PHYSICAL_PANEL_WIDTH_CM[i] * SOURCE_CM_TO_PIXEL_RATIO;
		}

		return xWidth;
	}


	/**
	 * Return an array that contains the width in pixels of the panels
	 * @return xWidth
	 */
	private static float[] getPixelWidthsOfPanelSides() {
		// Array the will contain the x coordinates of all the panels
		float xWidth[] = new float[PANEL_COUNT * 2];

		for(int i = 0; i < PANEL_COUNT * 2; i++) {
			int panelIndex = (int) Math.floor(i/2);
			xWidth[i] = (PHYSICAL_PANEL_WIDTH_CM[panelIndex] / 2) * SOURCE_CM_TO_PIXEL_RATIO;
		}

		return xWidth;
	}


	/**
	 * Return an array that contains the width in pixels of the panels
	 * @return xWidth
	 */
	private static float[] getXCoordOfPanelSides() {
		// Array the will contain the x coordinates of all the panels
		float xPos[] = new float[PANEL_COUNT * 2 + 1];

		// Array that contains the width of each panel sides
		float[] xWidth = getPixelWidthsOfPanelSides();

		xPos[0] = 0;

		for(int i = 1; i < PANEL_COUNT * 2 + 1; i++) {
			xPos[i] = xPos[i-1] + xWidth[i-1];
		}

		return xPos;
	}

}
