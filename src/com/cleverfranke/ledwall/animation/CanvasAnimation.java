package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;


public abstract class CanvasAnimation extends Animation {
	// Canvas parameters
	public final static int CANVAS_WIDTH = 600;
	public final static int CANVAS_HEIGHT = CANVAS_WIDTH / WallConfiguration.RATIO;
		
	public final static int[] PANEL_COORD_MAPPED_TO_CANVAS = mapPanelCoordToCanvas();
	public final static int[] ROWS_COORD_MAPPED_TO_CANVAS = mapRowsCoordToCanvas();
	
	
	public CanvasAnimation(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, applet);		
		super.graphicsContext = applet.createGraphics(CANVAS_WIDTH, CANVAS_HEIGHT);
		super.image = new PImage(CANVAS_WIDTH, CANVAS_HEIGHT);
	}
	
	
	/**
	 * Draw new frame of the animation
	 * 
	 * @return the new frame
	 *
	 */
	public PImage draw() {
		
		// Draw animation frame to image
		graphicsContext.beginDraw();
		drawAnimationFrame(graphicsContext);
		
		/** Uncomment this to show the beams, rows and columns used when 
		 * the canvas image is projected to the grid
		 *
		 * drawBeamsRowsColumns(graphicsContext);
		 * 
		 */	
		
		graphicsContext.endDraw();
		image = graphicsContext.get();
		
		//Return image
		return getImage();
		
	}

	/**
	 * Draws the beams, the rows and the columns of pixels that are used
	 * when computing the projection of a canvas to a grid
	 * @param g
	 */
	public void drawBeamsRowsColumns(PGraphics g){
		// Draw columns
		for (int i = 0; i < PANEL_COORD_MAPPED_TO_CANVAS.length; i++) {
			for (int j = 0; j < ROWS_COORD_MAPPED_TO_CANVAS.length; j++) {
				int x = PANEL_COORD_MAPPED_TO_CANVAS[i];
				int y = ROWS_COORD_MAPPED_TO_CANVAS[j];
				g.strokeWeight(1);
				g.stroke(PColor.color(255, 0, 0));
				g.point(x, y);
			}
		}
	
		// Draw beams 
		int i = 0;
		for (int coord: PANEL_COORD_MAPPED_TO_CANVAS){
			if (i%2 == 0) {
				g.stroke(PColor.color(255,0 ,0));
			} else {
				g.stroke(PColor.color(0, 255 ,0));
			}
			
			g.strokeWeight(1);
			g.line(coord, 0, coord, CANVAS_HEIGHT);
			i++;
		}
		
		// Draw rows
		for (int coord: ROWS_COORD_MAPPED_TO_CANVAS){
			g.stroke(PColor.color(0, 0, 255));
			
			g.strokeWeight(1);
			g.line(0, coord, CANVAS_WIDTH, coord);
			i++;
		}
	}
	
	
	/**
	 * Fetch the latest animation frame
	 * 
	 * @return latest animation frame
	 */
	public PImage getImage() {
		/** Uncomment this to show the canvas image and not the projected one
		 *
		 * return image;
		 * 
		 */	
		return projectCanvasToGrid(image);
	}
	
	private static PImage projectCanvasToGrid(PImage canvas) {
//		canvas.resize(WallConfiguration.COLUMNS_COUNT, WallConfiguration.ROWS_COUNT);
//		return canvas;
		
		// Get canvas pixels
		canvas.loadPixels();
		int[] pixels = canvas.pixels;
		
		// Create grid image
		PImage grid = new PImage(WallConfiguration.COLUMNS_COUNT, WallConfiguration.ROWS_COUNT);
		
		for (int i = 0; i < WallConfiguration.COLUMNS_COUNT; i++) {
			for (int j = 0; j < WallConfiguration.ROWS_COUNT; j++) {
				int x = PANEL_COORD_MAPPED_TO_CANVAS[i];
				int y = ROWS_COORD_MAPPED_TO_CANVAS[j];
				
				int pixel = pixels[y*CANVAS_WIDTH + x];
				grid.set(i, j, pixel);
			}
		}

		return grid;
	}

	
	
	/**
	 * Maps the panel coordinates on the wall to the canvas size.
	 * @return
	 */
	private static int[] mapPanelCoordToCanvas(){
		int[] mapped = new int[WallConfiguration.XPANELCOORD.length * 2 - 2];
		
		int i = 0;
		for(float x : WallConfiguration.XPANELCOORD) {
			int rightCoord = (int) PApplet.map(x + WallConfiguration.BEAM_WIDTH / 2, 0, WallConfiguration.SOURCE_IMG_WIDTH, 0, CANVAS_WIDTH);
			int leftCoord = (int) PApplet.map(x - WallConfiguration.BEAM_WIDTH / 2 - 1, 0, WallConfiguration.SOURCE_IMG_WIDTH, 0, CANVAS_WIDTH);
			
			if (leftCoord > 0 && leftCoord < CANVAS_WIDTH) {
				mapped[i] = leftCoord;
				i++;
			}
			
			if (rightCoord > 0 && rightCoord < CANVAS_WIDTH) {
				mapped[i] = rightCoord;
				i++;
			}
		}
		
		return mapped;
	}
	
	/**
	 * Maps the rows coordinates on the wall to the canvas size.
	 * @return
	 */
	private static int[] mapRowsCoordToCanvas(){
		int[] mapped = new int[WallConfiguration.ROWS_COUNT];

		for(int i = 0; i < WallConfiguration.ROWS_COUNT; i++) {
			mapped[i] = (int) PApplet.map(i, 0, WallConfiguration.ROWS_COUNT, 0, CANVAS_HEIGHT);
		}
		
		return mapped;
	}
	
	public abstract boolean isDone();
	
	@Override
	public void prepareForQueueRotation() {}
	
	public abstract void drawAnimationFrame(PGraphics g);
}
