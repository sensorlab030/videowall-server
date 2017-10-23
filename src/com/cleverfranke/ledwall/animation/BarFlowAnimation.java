
package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import java.util.concurrent.ThreadLocalRandom;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.looksgood.ani.*;

public class BarFlowAnimation extends Animation{
	// Parameters
	private int NBVALUES = 26; 									// Total number of bars
	private float DURATION = 3;									// Animation duration
	private int repeatCount = 3;
	
	private int startColor = PColor.color(0, 180, 180); 		// Bars initial color
	private int finalr = 255;									// Top end color of bars (red gradient)
	private float[] VALUES = new float[NBVALUES];				// Bar values
	private Bar[] bars = new Bar[NBVALUES];						// Bars array
	private boolean[] barsDoneDrawing = new boolean[NBVALUES];  // Bars animations done flags
		
	
	public class Bar {
		private int currentHeight;
		private int finalHeight;
		private int panelIndex;
		private int r = 0;
		private float delay;
		private AniSequence aniBar;
		private AniSequence aniColor;
		
		private Bar(int finalHeight, int panelIndex) {
			this.finalHeight = finalHeight;
			this.currentHeight = 0;
			this.panelIndex = panelIndex;
			this.delay = (float) (panelIndex  * Math.random() * DURATION / NBVALUES);
			this.aniBar = new AniSequence(applet);
			this.aniColor = new AniSequence(applet);
			this.setAniBar();
			this.setAniColor();
		}
		
		
		private void setAniBar(){
			aniBar.beginSequence();
			aniBar.add(Ani.to(this, 0, 0, "currentHeight", 0, Ani.QUAD_OUT));
			aniBar.add(Ani.to(this, DURATION, delay, "currentHeight", finalHeight + WallConfiguration.ROWS_COUNT, Ani.QUAD_IN));	
			aniBar.endSequence();
			
			aniBar.start();
		}

		private void setAniColor(){
			aniColor.beginSequence();
			aniColor.add(Ani.to(this, 0, 0, "r", 0, Ani.QUAD_OUT));
			aniColor.add(Ani.to(this, DURATION, delay, "r", finalr, Ani.QUAD_IN));
			aniColor.endSequence();
			
			aniColor.start();
		}
		
		private void draw(PGraphics g) {
			
			int y = WallConfiguration.ROWS_COUNT - currentHeight;
			
			for (int i = y; i <= y+y; i++) {
			      float inter = y == 0 ? 0 : PApplet.map(i, y, y+y, 0, 1);
			      int color = PColor.color(r, 180, 180);
			      int c = g.lerpColor(color, startColor, inter);
			      g.stroke(c);
			      g.strokeWeight(1);
			      g.point(panelIndex, i);
			}
			
			// If both animation ended, change done flag to true
			if (aniBar.isEnded() && aniColor.isEnded()) {
				barsDoneDrawing[panelIndex] = true;
			}
		}
		
	}
	
	public BarFlowAnimation(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, applet);
		generateBars();
	}

	
	/**
	 * Get y coordinates and height
	 */
	private void generateBars(){
		VALUES = generateRandomValues(NBVALUES);
		
		for(int i = 0; i < NBVALUES; i++) {
			int finalHeight = (int) PApplet.map(VALUES[i], 0, 2, 0, WallConfiguration.ROWS_COUNT);
			bars[i] = new Bar(finalHeight, i);
			barsDoneDrawing[i] = false;
		}	
	}
	
	/**
	 * When all the line animations are done, generate new lines and renew the animation
	 */
	public void isDoneDrawing() {
		repeatCount--;
		generateBars();
	}
	
	public void drawAnimationFrame(PGraphics g) {
		g.background(255);
		g.noStroke();
		
		// Draw each bar
		for (Bar bar: bars){
			bar.draw(g);
		} 		
		
		if (areAllTrue(barsDoneDrawing)) { isDoneDrawing(); }
		
	}
	
	public boolean isDone() {
		return (repeatCount == 0);
	}
	
	@Override
	public void prepareForQueueRotation() {
		isDoneDrawing();
		repeatCount = 2;
	}
}
