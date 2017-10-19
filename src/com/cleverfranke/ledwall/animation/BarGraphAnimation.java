package com.cleverfranke.ledwall.animation;
import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import de.looksgood.ani.AniSequence;
import processing.core.PApplet;
import processing.core.PGraphics;

public class BarGraphAnimation extends Animation {
	// Parameters
	private int NBVALUES = 13; 						// Total number of bars
	private float DURATION = 3;						// Animation duration
	private int color = PColor.color(0, 180, 180); 	// Bars color
	

	private float[] VALUES = generateRandomValues(NBVALUES);	// Bar values
	private boolean isDone = false;								// Flags the end of the animation
	private Bar[] bars = new Bar[NBVALUES];						// Bars array
	private boolean[] barsDoneDrawing = new boolean[NBVALUES];  // Bars animations done flags
	
	
	public class Bar {
		private float currentHeight;
		private float finalHeight;
		private int panelIndex;
		private AniSequence aniBar;
		
		
		private Bar(float finalHeight, int panelIndex) {
			this.finalHeight = finalHeight;
			this.currentHeight = 0;
			this.panelIndex = panelIndex;
			this.setAniBar();
		}
		
		
		private void setAniBar(){
			float delay = panelIndex * DURATION / VALUES.length;
			
			this.aniBar = new AniSequence(applet);
			this.aniBar.beginSequence();
			this.aniBar.add(Ani.to(this, DURATION, delay, "currentHeight", finalHeight, Ani.QUAD_IN));
			this.aniBar.add(Ani.to(this, DURATION / 2, delay, "currentHeight", 0, Ani.QUAD_OUT, "onEnd:isDoneDrawing"));
			this.aniBar.endSequence();
			
			this.aniBar.start();
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
		generateBars();
	}

	
	/**
	 * Get y coordinates and height
	 */
	private void generateBars(){
		float[] minMax = findMinMaxValues(VALUES);	
		
		for(int i = 0; i < NBVALUES; i++) {
			float finalHeight = PApplet.map(VALUES[i], 0, minMax[1], 0, WallConfiguration.SOURCE_IMG_HEIGHT);
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
