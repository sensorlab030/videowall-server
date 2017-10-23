package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;


public abstract class CanvasAnimation extends Animation {
	public static int width = 600;
	public static int height = width / WallConfiguration.RATIO;
	public static int[] mappedPanelCoord = mapPanelCoordToCanvas();
	public static int[] mappedRowsCoord = mapRowsCoordToCanvas();
	
	public CanvasAnimation(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, applet);		
		super.graphicsContext = applet.createGraphics(width, height);
		super.image = new PImage(width, height);
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
		
//		for (int i = 0; i < mappedPanelCoord.length; i++) {
//			for (int j = 0; j < mappedRowsCoord.length; j++) {
//				int x = mappedPanelCoord[i];
//				int y = mappedRowsCoord[j];
//				graphicsContext.strokeWeight(1);
//				graphicsContext.stroke(PColor.color(255, 0, 0));
//				graphicsContext.point(x, y);
//			}
//		}
//		
//		// Draw beams 
//		int i = 0;
//		for (int coord: mappedPanelCoord){
//			if (i%2 == 0) {
//				graphicsContext.stroke(PColor.color(255,0 ,0));
//			} else {
//				graphicsContext.stroke(PColor.color(0, 255 ,0));
//			}
//			
//			graphicsContext.strokeWeight(1);
//			graphicsContext.line(coord, 0, coord, height);
//			i++;
//		}
//		
//		// Draw rows
//		for (int coord: mappedRowsCoord){
//			graphicsContext.stroke(PColor.color(0, 0, 255));
//			
//			graphicsContext.strokeWeight(1);
//			graphicsContext.line(0, coord, width, coord);
//			i++;
//		}
		
		graphicsContext.endDraw();
		image = graphicsContext.get();
		
		//Return image
		return getImage();
		
	}

	/**
	 * Fetch the latest animation frame
	 * 
	 * @return latest animation frame
	 */
	public PImage getImage() {
		return projectCanvasToGrid(image);
	}
	
	private static PImage projectCanvasToGrid(PImage canvas) {

		PImage grid = new PImage(WallConfiguration.COLUMNS_COUNT, WallConfiguration.ROWS_COUNT);
		
		for (int i = 0; i < grid.width; i++) {
			for (int j = 0; j < grid.height; j++) {
				int x = mappedPanelCoord[i];
				int y = mappedRowsCoord[j];
				
				int pixel = canvas.get(x, y);
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
		int[] mapped = new int[XPANELCOORD.length * 2 - 2];
		int i = 0;
		for(float x : XPANELCOORD) {
			int rightCoord = (int) PApplet.map(x + WallConfiguration.BEAM_WIDTH / 2, 0, WallConfiguration.SOURCE_IMG_WIDTH, 0, width);
			int leftCoord = (int) PApplet.map(x - WallConfiguration.BEAM_WIDTH / 2 - 1, 0, WallConfiguration.SOURCE_IMG_WIDTH, 0, width);
			
			if (leftCoord > 0 && leftCoord < width) {
				mapped[i] = leftCoord;
				i++;
			}
			
			if (rightCoord > 0 && rightCoord < width) {
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

		for(int i = 0; i < mapped.length; i++) {
			mapped[i] = (int) PApplet.map(i, 0, mapped.length, 0, height);
		}
		
		return mapped;
	}
	
	public abstract boolean isDone();
	
	@Override
	public void prepareForQueueRotation() {}
	
	public abstract void drawAnimationFrame(PGraphics g);
}
