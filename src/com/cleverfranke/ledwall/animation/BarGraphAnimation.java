package com.cleverfranke.ledwall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PGraphics;

public class BarGraphAnimation extends Animation {
	// Parameters
	private int NBVALUES = 13; 						// Total number of bars
	private float DURATION = 5;						// Animation duration
	private int color = PColor.color(0, 180, 180); 	// Bars color
	
	// Variables
	private float[] VALUES = new float[NBVALUES];
	private float max = Integer.MIN_VALUE;
	private float min = Integer.MAX_VALUE;
	private boolean isDone = false;					// Flags the end of the animation
	private Bar[] bars = new Bar[NBVALUES];
	private boolean[] barsDoneDrawing = new boolean[NBVALUES];
	
	public class Bar {
		private float currentHeight;
		private float finalHeight;
		private int panelIndex;
		private Ani aniBar;
		
		private Bar(float finalHeight, int panelIndex) {
			this.finalHeight = finalHeight;
			this.currentHeight = 0;
			this.panelIndex = panelIndex;
			this.setAniBars();
		}
		
		private void setAniBars(){
			aniBar = new Ani(this, DURATION, (float) 0, "currentHeight", finalHeight, Ani.LINEAR, "onEnd:isDoneDrawing");
			aniBar.start();
		}
		
		@SuppressWarnings("unused")
		private void isDoneDrawing(){
			barsDoneDrawing[panelIndex] = true;
		}
		
		private void drawBar(PGraphics g) {
			drawBottomBar(g, panelIndex, g.height - currentHeight);
		}
	}
	
	
	public BarGraphAnimation(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, applet);
		findMinMaxValues();	
		generateBars();
	}
		
	/**
	 * Find min and max in VALUES.
	 * Here we also use this function to generate random values at first, but the animation could be provided with real VALUES.
	 */
	private void findMinMaxValues() {
		for(int i = 0; i < NBVALUES; i++) {
			// Generate random value
			VALUES[i] = (float)(Math.random());

			// Find min and max in VALUES
		    if (VALUES[i] < min) { min = VALUES[i]; }
		    if (VALUES[i] > max) { max = VALUES[i]; }
		}	
	}
	
	/**
	 * Get y coordinates and height
	 */
	private void generateBars(){
		for(int i = 0; i < NBVALUES; i++) {
			float finalHeight = PApplet.map(VALUES[i], 0, max, 0, WallConfiguration.SOURCE_IMG_HEIGHT);
			bars[i] = new Bar(finalHeight, i);
			barsDoneDrawing[i] = false;
		}	
	}
	

	public void drawAnimationFrame(PGraphics g) {		
		g.background(255);
		g.fill(color);
		g.noStroke();
		
		// Draw each bar
		for (Bar bar: bars){
			bar.drawBar(g);
		} 		
		
		// Flag end of animation
		if (areAllTrue(barsDoneDrawing)) { isDone = true; }
	}


	@Override
	public boolean isDone() {
		return isDone;
	}
	
	@Override
	public void prepareForQueueRotation() {
		isDone = false;
	}

}
