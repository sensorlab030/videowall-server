package nl.sensorlab.videowall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PFont;
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
	private boolean showTriggerObjects = true; // Show triggerObjects as guides to trigger lights (pixels)
	
	public TriggerSequence indicatorsequence, bartest;
	public ArrayList<TriggerSequence> sequences;
	
	PGraphics stretchFont;
	PFont pixelFont;

	public WeatherAnimation(PApplet applet) {
		super(applet);
		
		// Init Ani
		Ani.init(applet);
		
		// Allow overwrite
		Ani.overwrite();
		
		// Create a list with all the sequences.
		sequences = new ArrayList<TriggerSequence>();
		
		// Create the arraylist containing the pixel objects
		pixels = new ArrayList<Pixel>();
		generatePixelObjects(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
		
		// Set pixel font
		pixelFont = applet.createFont("data/Pixeled.tff", 10);
		
		// Create Sequence
		indicatorsequence = new TriggerSequence();
		bartest = new TriggerSequence();
		
		
		// Bars
//		for(int x = 1; x < PIXEL_RESOLUTION_X - 2; x+=2) {
//			
//			float minPrecp = 0;
//			float maxPrecp = 200;
//			float randomPrecp = applet.random(10 + minPrecp, maxPrecp);
//			
//			float mapYleds = applet.map(randomPrecp, minPrecp, maxPrecp, PIXEL_RESOLUTION_Y - includeVerticalAmountPixels, includeVerticalAmountPixels);
//			
//			TriggerObject temp = new TriggerObject(
//								new PVector(x, mapYleds), 
//								new PVector(x + 2, mapYleds), 
//								new PVector(x + 2, PIXEL_RESOLUTION_Y - includeVerticalAmountPixels), 
//								new PVector(x, PIXEL_RESOLUTION_Y - includeVerticalAmountPixels), 
//								new PVector(0, PIXEL_RESOLUTION_Y), 
//								Ani.SINE_IN,
//								6);
//			
//			bartest.addTriggerObject(temp);
//		}
		
		
		// Slide in from 0,0
//		TriggerObject temp = new TriggerObject(
//		new PVector(0, 0), 
//		new PVector(PIXEL_RESOLUTION_X, 0), 
//		new PVector(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y), 
//		new PVector(0, PIXEL_RESOLUTION_Y), 
//		new PVector(-PIXEL_RESOLUTION_X, -PIXEL_RESOLUTION_Y), 
//		Ani.SINE_OUT,
//		4);
//		bartest.addTriggerObject(temp);
//		
//		
		// Rain
		float minPrecp = 0;
		float maxPrecp = 200;
		float randomPrecp = applet.random(10 + minPrecp, maxPrecp);
		
		
		randomPrecp = 200;
		
		for(int x = 1; x < PIXEL_RESOLUTION_X - 1; x++) {
		
			float speed  = applet.map(randomPrecp, minPrecp, maxPrecp, 20, 4);
			float length = applet.map(randomPrecp, minPrecp, maxPrecp, 10, 4);
			float spacing = 4;//applet.map(randomPrecp, minPrecp, maxPrecp, 10, 1);
			
			float yOffset = 0;//PIXEL_RESOLUTION_Y;
			
			for(int y = -PIXEL_RESOLUTION_Y; y < PIXEL_RESOLUTION_Y * 2; y+= (int)(length + spacing)) {
				
				// Differ each row
				if((x % 2) == 0) {
					yOffset = -(spacing);
				}else {
					yOffset = spacing;
				}
				
				TriggerObject temp = new TriggerObject(
				new PVector(x, y + yOffset), 
				new PVector(x + 1, y + yOffset), 
				new PVector(x + 1, y + length + yOffset), 
				new PVector(x,  y + length + yOffset), 
				new PVector(0, - PIXEL_RESOLUTION_Y), 
				Ani.LINEAR,
				2);
				
				bartest.addTriggerObject(temp);
			}
		
	}
	
		// Add the led strips
		indicatorsequence.addTriggerObject(new TriggerObject(new PVector(0, 0), new PVector(PIXEL_RESOLUTION_X, 0), new PVector(PIXEL_RESOLUTION_X, includeVerticalAmountPixels), new PVector(0, includeVerticalAmountPixels), new PVector(-PIXEL_RESOLUTION_X, 0), Ani.LINEAR, 5));
		indicatorsequence.addTriggerObject(new TriggerObject(new PVector(PIXEL_RESOLUTION_X - 1, includeVerticalAmountPixels), new PVector(PIXEL_RESOLUTION_X, 0), new PVector(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y), new PVector(PIXEL_RESOLUTION_X - 1, PIXEL_RESOLUTION_Y), new PVector(0, -PIXEL_RESOLUTION_Y), Ani.LINEAR, 3));
		indicatorsequence.addTriggerObject(new TriggerObject(new PVector(0, PIXEL_RESOLUTION_Y - includeVerticalAmountPixels), new PVector(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y - includeVerticalAmountPixels), new PVector(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y), new PVector(0, PIXEL_RESOLUTION_Y), new PVector(PIXEL_RESOLUTION_X, 0), Ani.LINEAR, 5));
		indicatorsequence.addTriggerObject(new TriggerObject(new PVector(0, 0), new PVector(1, 0), new PVector(1, PIXEL_RESOLUTION_Y), new PVector(0, PIXEL_RESOLUTION_Y), new PVector(0, PIXEL_RESOLUTION_Y), Ani.LINEAR, 3));
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
					pixels.get(pixelIndex).color = PColor.color(255);
					
					// Add to indicator
					indicatorPixels.add(pixelIndex);
				}else {
					// Set settings for canvas pixels
					pixels.get(pixelIndex).color = PColor.color(51, 204, 255);
					pixels.get(pixelIndex).alpha = 0;
					
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
			// Trigger next visual indicator
		}else {
			indicatorsequence.updateSequence();
		}
		
		// Update indicator pixels
		for(int i = 0; i < indicatorPixels.size(); i++) {
			Pixel currentpixel = pixels.get(indicatorPixels.get(i));
			if(indicatorsequence.isInBounds(currentpixel)) {
				currentpixel.fadeIn(1f, 255); // FadeIn
			}else {
				currentpixel.fadeOut(0.5f, 0); // FadeOut
			}
			// Update the pixel
			currentpixel.update();
		}
		
		if(bartest.doneUpdating){
			bartest.resetSequence(); // reverseSequence
			// Reset and toggle to next animation.
			// sequenceUpdaterIndex++;
		}else {
			bartest.updateAll(); // Update all at once
		}
	
		// Update the canvas pixels
		for(int i = 0; i < canvasPixels.size(); i++) { // canvasPixels
			
			
			Pixel currentpixel = pixels.get(canvasPixels.get(i));
			
			
			if(bartest.isInBounds(currentpixel)) {
				float a = applet.map(currentpixel.position.y, includeVerticalAmountPixels,  PIXEL_RESOLUTION_Y - includeVerticalAmountPixels, 255, 110); // <-- Set alpha based on distance
				 a = 255;
				//currentpixel.fadeIn(1.25f, a); // FadeIn//
//				 currentpixel.fadeIn(0.1f, a); // FadeIn
				 currentpixel.alpha = 255;
			}else {
//				currentpixel.fadeOut(0.05f, 0); // FadeOut
//				currentpixel.fadeOut(0.5f, 0); // FadeOut
				currentpixel.alpha = 0;
			}
			currentpixel.update();
		}
	}
	
	protected void drawPixels(PGraphics g) {
		g.noFill();
		for(Pixel pixel:pixels) {
			g.stroke(pixel.color, pixel.alpha);
			g.point(pixel.position.x, pixel.position.y);
		}
		if(showTriggerObjects) indicatorsequence.drawTriggerObjects(g);
		if(showTriggerObjects) bartest.drawTriggerObjects(g);
			
		stretchText("DAYS", g);
	}
	
	public void stretchText(String _text, PGraphics g) {
		applet.textFont(pixelFont);
		applet.textSize(20);
		
		int textWidth 	= (int)applet.textWidth(_text);
		int textHeight 	= (int)((applet.textAscent() + applet.textDescent()) * 0.6);
		
		// Start drawing the actual text
		stretchFont = applet.createGraphics(textWidth, textHeight);
		stretchFont.beginDraw();
		stretchFont.noStroke();
		stretchFont.fill(255,50);
		stretchFont.textSize(20);
		stretchFont.textAlign(applet.LEFT, applet.TOP);
		stretchFont.text(_text, 0, (int)(-textHeight*0.3));
		stretchFont.endDraw();
		
		// Strech the image to fill the canvas
		g.image(stretchFont, 0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
	}

	@Override
	protected void drawAnimationFrame(PGraphics g) {
		g.background(0);
		updatePixels();
		drawPixels(g);
	}
}
