package nl.sensorlab.videowall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import weathermetrics.Pixel;
import weathermetrics.TriggerObject;
import weathermetrics.TriggerSequence;

public class WeatherAnimation extends BaseAnimation {
	
	private ArrayList<Pixel> pixels; // <-- Pixel object(s)
	
	private List<Integer> indicatorPixels = new ArrayList<Integer>();  // <-- LED's that function as indicators
	private List<Integer> canvasPixels = new ArrayList<Integer>(); // <-- LED's that function as the canvas
	
	private int includeVerticalAmountPixels = 5; // Pixels from the top and the bottom to include in the indicator.
	private boolean showTriggerObjects = false; // Show triggerObjects as guides to trigger lights (pixels)
	
	public TriggerSequence indicatorsequence;

	public WeatherAnimation(PApplet applet) {
		super(applet);
		
		// Create the arraylist containing the pixel objects
		pixels = new ArrayList<Pixel>();
		generatePixelObjects(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
		indicatorsequence = new TriggerSequence();
		
		// Add the led strips
		indicatorsequence.addTriggerObject(new TriggerObject(new PVector(0, 0), new PVector(PIXEL_RESOLUTION_X, 0), new PVector(PIXEL_RESOLUTION_X, includeVerticalAmountPixels), new PVector(0, includeVerticalAmountPixels), new PVector(-PIXEL_RESOLUTION_X, 0), 1)); // -PIXEL_RESOLUTION_X
		indicatorsequence.addTriggerObject(new TriggerObject(new PVector(PIXEL_RESOLUTION_X - 1, 0), new PVector(PIXEL_RESOLUTION_X, 0), new PVector(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y), new PVector(PIXEL_RESOLUTION_X - 1, PIXEL_RESOLUTION_Y), new PVector(0, -PIXEL_RESOLUTION_Y), 1));
		indicatorsequence.addTriggerObject(new TriggerObject(new PVector(0, PIXEL_RESOLUTION_Y - includeVerticalAmountPixels), new PVector(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y - includeVerticalAmountPixels), new PVector(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y), new PVector(0, PIXEL_RESOLUTION_Y), new PVector(PIXEL_RESOLUTION_X, 0), -1)); // -PIXEL_RESOLUTION_X
		indicatorsequence.addTriggerObject(new TriggerObject(new PVector(0, 0), new PVector(1, 0), new PVector(1, PIXEL_RESOLUTION_Y), new PVector(0, PIXEL_RESOLUTION_Y), new PVector(0, PIXEL_RESOLUTION_Y), -1));
	}
	
	protected void generatePixelObjects(int _xDensity, int _yDensity) {
		for(int x = 0; x < _xDensity; x++) {
			for(int y = 0; y < _yDensity; y++) {
				// Add the new Pixel
				pixels.add(new Pixel(new PVector(x, y)));
				
				// Add the pixel to a reference list.
				int pixelIndex = pixels.size() - 1;
				
				// Check which list the pixels belongs to
				if((y < includeVerticalAmountPixels || y >= _yDensity - includeVerticalAmountPixels) || (x < 1 || x >= _xDensity - 1)) {
					// Set settings for indicator pixels
					pixels.get(pixelIndex).alpha = 0;
					pixels.get(pixelIndex).color = PColor.color(255,0,0);
					pixels.get(pixelIndex).speed = 4;
					
					// Add to indicator
					indicatorPixels.add(pixelIndex);
				}else {
					// Set settings for canvas pixels
					pixels.get(pixelIndex).color = PColor.color(0,191,255);
					pixels.get(pixelIndex).alpha = applet.random(0, 255);
					pixels.get(pixelIndex).speed = applet.random(1, 10);
					
					// Add to canvas 
					canvasPixels.add(pixelIndex);
				}
			}
		}
	}
	
	protected void updatePixels() {
		// Update sequence indicator
		if(indicatorsequence.doneUpdating) {
			indicatorsequence.resetSequence();
		}else {
			indicatorsequence.updateSequence();
		}
		
		// Update indicator pixels
		for(int i = 0; i < indicatorPixels.size(); i++) {
			Pixel currentpixel = pixels.get(indicatorPixels.get(i));
			currentpixel.update();
			if(indicatorsequence.isInBounds(currentpixel)) {
				currentpixel.fadeIn(4); // FadeIn
			}else {
				currentpixel.fadeOut(10); // FadeOut
			}
		}
	
		// Update the canvas pixels
		for(int i = 0; i < canvasPixels.size(); i++) {
			Pixel currentpixel = pixels.get(canvasPixels.get(i));
			currentpixel.update();
			// Update pixels; fadeIn and fadeOut
			if(currentpixel.doneUpdating) {
				currentpixel.doneUpdating = false;
				currentpixel.toggleDirection();
			}
		}
	}
	
	protected void drawPixels(PGraphics g) {
		g.noFill();
		for(Pixel pixel:pixels) {
			g.stroke(pixel.color, pixel.alpha);
			g.point(pixel.position.x, pixel.position.y);
		}
		if(showTriggerObjects) indicatorsequence.drawTriggerObjects(g);
	}

	@Override
	protected void drawAnimationFrame(PGraphics g) {
		g.background(0);
		updatePixels();
		drawPixels(g);
	}
}
