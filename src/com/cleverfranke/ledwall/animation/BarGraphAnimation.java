package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;
import de.looksgood.ani.Ani;
import de.looksgood.ani.AniSequence;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Animation that draws bars in and out
 *
 */
public class BarGraphAnimation extends Animation {
	// Parameters
	private int NBVALUES = 26; 						// Total number of bars
	private float DURATION = 1;						// Animation duration
	private int color = PColor.color(255, 100, 50); 	// Bars color


	private float[] VALUES = new float[NBVALUES];				// Bar values
	private boolean isDone = false;								// Flags the end of the animation
	private Bar[] bars = new Bar[NBVALUES];						// Bars array
	private boolean[] barsDoneDrawing = new boolean[NBVALUES];  // Bars animations done flags


	public class Bar {
		private int currentHeight;
		private int finalHeight;
		private int panelIndex;
		private AniSequence aniBar;


		private Bar(int finalHeight, int panelIndex) {
			this.finalHeight = finalHeight;
			this.currentHeight = 0;
			this.panelIndex = panelIndex;
			this.aniBar = new AniSequence(applet);
			this.setAniBar();
		}

		/**
		 * Animationg height and y position of the bars
		 */
		private void setAniBar(){
			float delay = panelIndex * DURATION / NBVALUES;

			aniBar.beginSequence();
			aniBar.add(Ani.to(this, DURATION, delay, "currentHeight", finalHeight, Ani.QUAD_IN));
			aniBar.add(Ani.to(this, DURATION / 2, delay, "currentHeight", 0, Ani.QUAD_OUT, "onEnd:isDoneDrawing"));
			aniBar.endSequence();

			aniBar.start();
		}


		@SuppressWarnings("unused")
		private void isDoneDrawing(){
			barsDoneDrawing[panelIndex] = true;
		}


		private void draw(PGraphics g) {
			g.rect(panelIndex, WallConfiguration.ROWS_COUNT - currentHeight, 1, currentHeight);
		}
	}


	public BarGraphAnimation(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, applet);
	}


	/**
	 * Get y coordinates and height
	 */
	private void generateBars(){
		VALUES = generateRandomValues(NBVALUES);
		float[] minMax = findMinMaxValues(VALUES);

		for(int i = 0; i < NBVALUES; i++) {
			int finalHeight = (int) PApplet.map(VALUES[i], 0, minMax[1], 0, WallConfiguration.ROWS_COUNT);
			bars[i] = new Bar(finalHeight, i);
			barsDoneDrawing[i] = false;
		}
	}


	@Override
	public void drawAnimationFrame(PGraphics g) {
		g.background(255);
		g.fill(color);
		g.noStroke();

		// Draw each bar
		for (Bar bar: bars){
			bar.draw(g);
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
		generateBars();
	}

}
