
package com.cleverfranke.ledwall.animation.deprecated;

import com.cleverfranke.util.PColor;
import processing.core.PApplet;
import processing.core.PGraphics;
import de.looksgood.ani.Ani;
import de.looksgood.ani.AniSequence;


/**
 * Random sized bars flowing from bottom to top, in a lava lamp feeling.
 * As the bars go up, their color at the top changes
 *
 */
public class BarFlowAnimation extends Animation{
	// Parameters
	private final int NBVALUES = WallConfiguration.COLUMN_COUNT; 		// Total number of bars
	private final float DURATION = 4;									// Animation duration
	private final int finalr = 255;										// Top end color of bars (red gradient)
	private final int startColor = PColor.color(0, 180, 180); 			// Bars initial color

	private int repeatCount;											// Number of loops
	private Bar[] bars = new Bar[NBVALUES];								// Bars array


	public class Bar {
		private int currentHeight;
		private int finalHeight;
		private int panelIndex;
		private boolean isDoneDrawing;
		private int r = 0;
		private float delay;
		private AniSequence aniBar;
		private AniSequence aniColor;

		private Bar(int panelIndex, boolean isDoneDrawing) {
			this.isDoneDrawing = isDoneDrawing;
			this.panelIndex = panelIndex;
			this.currentHeight = 0;
			this.delay = (float) (panelIndex  * Math.random() * DURATION / NBVALUES);
			this.aniBar = new AniSequence(applet);
			this.aniColor = new AniSequence(applet);
			this.setAniBar();
			this.setAniColor();
		}

		private void setFinalHeight(int finalHeight){
			this.finalHeight = finalHeight;
		}

		private void setIsDoneDrawing(boolean isDoneDrawing){
			this.isDoneDrawing = isDoneDrawing;
		}

		/**
		 * Animating height and y position of the bars
		 */
		private void setAniBar(){
			aniBar.beginSequence();
			aniBar.add(Ani.to(this, 0, 0, "currentHeight", 0, Ani.QUAD_OUT));
			aniBar.add(Ani.to(this, DURATION, delay, "currentHeight", finalHeight + graphicsContext.height, Ani.QUAD_IN));
			aniBar.endSequence();
		}

		/**
		 * Animating color gradients of the bars
		 */
		private void setAniColor(){
			aniColor.beginSequence();
			aniColor.add(Ani.to(this, 0, 0, "r", 0, Ani.QUAD_OUT));
			aniColor.add(Ani.to(this, DURATION, delay, "r", finalr, Ani.QUAD_IN));
			aniColor.endSequence();
		}

		private void draw(PGraphics g) {

			int y = graphicsContext.height - currentHeight;

			for (int i = y; i <= y+y; i++) {
				  // Gradient
			      float inter = y == 0 ? 0 : PApplet.map(i, y, y+y, 0, 1);
			      int color = PColor.color(r, 180, 180);
			      int c = g.lerpColor(color, startColor, inter);

			      g.stroke(c);
			      g.strokeWeight(1);
			      g.point(panelIndex, i);
			}

			// If both animation ended, change done flag to true
			if (aniBar.isEnded() && aniColor.isEnded()) {
				isDoneDrawing = true;
			}
		}

	}

	public BarFlowAnimation(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, applet);

		// Set repeat count
		setRepeatCount();

		// Create bars
		for(int i = 0; i < NBVALUES; i++) {
			bars[i] = new Bar(i, false);
		}
	}

	private void setRepeatCount(){
		repeatCount = 3;
	}

	/**
	 * Get y coordinates and height
	 */
	private void initBars(){
		float[] VALUES = generateRandomValues(NBVALUES);

		for(int i = 0; i < NBVALUES; i++) {
			int finalHeight = (int) PApplet.map(VALUES[i], 0, 2, 0, graphicsContext.height);
			bars[i].setFinalHeight(finalHeight);
			bars[i].setIsDoneDrawing(false);
			bars[i].aniBar.start();
			bars[i].aniColor.start();
		}
	}

	private boolean allBarsDoneDrawing(){
		for(Bar bar : bars) if(!bar.isDoneDrawing) return false;
	    return true;
	}

	@Override
	public void drawAnimationFrame(PGraphics g) {
		g.background(255);
		g.noStroke();

		// Draw each bar
		for (Bar bar: bars){
			bar.draw(g);
		}

		if (allBarsDoneDrawing() && repeatCount > 0) {
			initBars();
			repeatCount--;
		}

	}

	@Override
	public boolean isDone() {
		return (repeatCount == 0);
	}

	@Override
	public void prepareForQueueRotation() {
		setRepeatCount();
		initBars();
	}
}
