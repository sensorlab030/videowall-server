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
	
	public static final int PANEL_COUNT = PHYSICAL_PANEL_WIDTH_CM.length;										// Number of panels
	public static final int ROWS_COUNT = (int) Math.floor(PHYSICAL_WALL_HEIGHT_CM / PHYSICAL_PIXEL_PITCH_CM); 	// Number of led rows
	public static final int COLUMNS_COUNT = PANEL_COUNT * 2; 													// Number of led columns
	
	// Source/preview image dimensions (pixels)
	public static final int SOURCE_CM_TO_PIXEL_RATIO = 2;														// Conversion ratio from cm to pixels
	public static final int SOURCE_IMG_WIDTH = PHYSICAL_WALL_WIDTH_CM * SOURCE_CM_TO_PIXEL_RATIO;				// Source image width for the wall configuration
	public static final int SOURCE_IMG_HEIGHT = PHYSICAL_WALL_HEIGHT_CM * SOURCE_CM_TO_PIXEL_RATIO;				// Source image height for the wall configuration
	public static final int ROW_HEIGHT = (int) Math.floor(SOURCE_IMG_HEIGHT / ROWS_COUNT);						// Row height
	public static final int BEAM_WIDTH = PHYSICAL_BEAM_WIDTH_CM * SOURCE_CM_TO_PIXEL_RATIO;						// Beam width in pixel
	
	public static final int RATIO = SOURCE_IMG_WIDTH / SOURCE_IMG_HEIGHT;										// Width / height ratio
}
