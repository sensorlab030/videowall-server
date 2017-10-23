package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import processing.core.PApplet;
import processing.core.PImage;

public abstract class LedAnimation extends Animation {
	
	public LedAnimation(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, applet);
		
		// Override graphic context
		super.graphicsContext = applet.createGraphics(WallConfiguration.COLUMNS_COUNT, WallConfiguration.ROWS_COUNT);
		super.image = new PImage(WallConfiguration.COLUMNS_COUNT, WallConfiguration.ROWS_COUNT * 4);
	}
	
	
	
	/**
	 * Fetch the latest animation frame, projected on the wall canvas
	 * 
	 * @return latest animation frame
	 */
	public PImage getImage() {
		return projectGridToWall(image);
	}
	
	
	/**
	 * Scale an image of 26x93 (resolution) of the wall to the image preview sizes
	 * @param grid
	 * @return wall, an image of grid projected on the wall beams
	 */
	public PImage projectGridToWall(PImage grid){
		
		PImage wall = new PImage(WallConfiguration.SOURCE_IMG_WIDTH, WallConfiguration.SOURCE_IMG_HEIGHT);
		final int BEAM_WIDTH = WallConfiguration.PHYSICAL_BEAM_WIDTH_CM * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO;
		
		for (int i = 0; i< grid.width; i++){
			int x = 0;
			int panelIndex = (int) Math.ceil((float)i/2);
			
			for (int j = 0; j < grid.height; j++){
				int pixel = grid.get(i, j);
				
				if (i%2 == 0) {
					x = (int) (XPANELCOORD[panelIndex] + BEAM_WIDTH / 2);
				} else {
					x = (int) (XPANELCOORD[panelIndex]) - BEAM_WIDTH / 2 - 1;
				}
				
				wall.set(x, j*WallConfiguration.ROW_HEIGHT + 2, pixel);			
			}
		}
		return wall;
	}

}
