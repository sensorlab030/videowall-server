package nl.sensorlab.videowall.animation;

import java.awt.Point;
import java.awt.Rectangle;

import nl.sensorlab.videowall.walldriver.WallGeometry;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Class that takes pixel images, and creates a preview image on what the output would be in real lide
 */
public class Preview {
	
	public static final float SCALE = 2f; // Scale from cm to pixels
	
	private Rectangle previewGeometry;
	private PGraphics previewContext;
	private PImage previewImage;
	
	private Point[] pixelMapping;
	
	private WallGeometry wallGeometry = WallGeometry.getInstance();
	private int pixelLength;
	
	public Preview(PApplet applet) {
		
		// Create canvas
		previewGeometry = WallGeometry.scaleRectangleRounded(WallGeometry.getInstance().getWallGeometry(), SCALE);
		previewContext = applet.createGraphics(previewGeometry.width, previewGeometry.height); 
		previewImage = applet.createImage(previewGeometry.width, previewGeometry.height, PConstants.RGB); 
		
		// Create pixel mapping
		pixelMapping = new Point[WallGeometry.getInstance().getPixelCount()];
		for (int i = 0; i < pixelMapping.length; i++) {
			Point canvasCoordinate = WallGeometry.scalePointRounded(WallGeometry.getInstance().getPixelCoordinates(i), SCALE);
			pixelMapping[i] = canvasCoordinate;
		}
		
		// 
		pixelLength = (int) Math.round(15f * SCALE);
		
	}
	
	public PImage renderPreview(PImage pixelImage) {
		
		previewContext.beginDraw();
		previewContext.background(0);
		
		if (pixelImage != null) {
			// Draw pixels
			for (int i = 0; i < pixelMapping.length; i++) {
				int color = pixelImage.pixels[i];
				Point coord = pixelMapping[i];
				
				previewContext.stroke(color);
				previewContext.line(coord.x, coord.y, coord.x + ((i % 2 == 0) ? pixelLength : -pixelLength), coord.y);
			}
		}
		
		// Draw beams
		previewContext.noStroke();
		previewContext.fill(100);
		for (int i = 0; i < wallGeometry.getBeamCount(); i++) {
			Rectangle beamRect = WallGeometry.scaleRectangleRounded(wallGeometry.getBeamGeometry(i), SCALE);
			previewContext.rect(beamRect.x, beamRect.y, beamRect.width, beamRect.height);
		}
		
		previewContext.endDraw();
		previewImage = previewContext.get();
		
		return previewImage;
		
	}
	
	/**
	 * Fetch canvas image for preview purposes
	 * 
	 * @return
	 */
	public PImage getPreviewImage() {
		return previewContext;
	}
	
	/**
	 * Get canvas geometry in pixels
	 * 
	 * @return
	 */
	protected Rectangle getGeometry() {
		return previewGeometry;
	}
	
}
