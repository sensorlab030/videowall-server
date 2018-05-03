package nl.sensorlab.videowall.animation.baseanimations;

import de.looksgood.ani.Ani;
import de.looksgood.ani.AniSequence;
import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;

public class SensorLabLogo extends BaseAnimation {
	
	private final static int BAR_COUNT = 13;
	private final static int BAR_WIDTH = 2;
	private final static int LOGO_BAR_COUNT = 3;
	private final static float GROW_SPEED = 0.001f;
	
	
	private float[] targetHeights = new float[LOGO_BAR_COUNT];
	
	// Bar members
	private float[] currentBarHeights = new float[BAR_COUNT];
	private float[] currentBarTargetHeights = new float[BAR_COUNT];
	
//	private float[] barTransitionProgress = new float[BAR_COUNT];
	
	private int currentBarInTransition = 0;
	
	
	
	public SensorLabLogo(PApplet applet) {
		super(applet);
		
		// Set target bar heights
		float barHeightDelta = 1f / (float) LOGO_BAR_COUNT;
		for (int i = 0; i < LOGO_BAR_COUNT; i++) {
			targetHeights[i] = (float) (i + 1) * barHeightDelta;
		}
		
		// Reset the animation to start
		resetAnimation();
		
	}
	
	@Override
	protected void drawAnimationFrame(PGraphics g, double dt) {
		
		// Apply growth to current bar (or spill over in next bar)
		float growth = (float) dt * GROW_SPEED;
		if (currentBarHeights[currentBarInTransition] + growth < currentBarTargetHeights[currentBarInTransition]) {
			currentBarHeights[currentBarInTransition] += growth;
		} else {
			
			// Capture growth excess to apply to next bar for smooth animation
			float excessGrowth = growth - (currentBarTargetHeights[currentBarInTransition] - currentBarHeights[currentBarInTransition]);
					
			// Set current bar to max (to prevent overshoot)
			currentBarHeights[currentBarInTransition] = currentBarTargetHeights[currentBarInTransition];
			
			// Switch to next bar or reset
			if (currentBarInTransition < BAR_COUNT - 1 ) {
				currentBarInTransition++;
			} else {
				resetAnimation();
			}
			
			currentBarHeights[currentBarInTransition] += growth;
			
		}
		
//		currentBarHeights[currentBarInTransition] += 
		
		// Update animation progress
//		barTransitionProgress[currentBarInTransition] = Math.min(1f, Math.max(0f, transitionProgress));
		
		// Setup graphics
		g.background(0);
		g.fill(255);
		g.noStroke();
		
		// Apply matrix transforms
		g.translate(0, g.height);
		
		// Draw bars
		for (int i = 0; i < BAR_COUNT; i++) {
			g.rect(BAR_WIDTH * i, 0, BAR_WIDTH, -currentBarHeights[i] * PIXEL_RESOLUTION_Y);
		}
		
	}
	
	private void resetAnimation() {
		System.out.println("Reset");
		
//		// Set target heights
//		for (int i = 0; i < BAR_COUNT; i++) {
//			currentBarTargetHeights[i] = targetHeights[i % LOGO_BAR_COUNT];
//			currentBarHeights[i] = 0f; //(float) Math.random();
//		}
		
		// Set current bar
		currentBarInTransition = 0;
		
	}
	
	private void currentBarComplete() {
		
//		System.out.println("CurrentBarComplete");
//		
//		// Switch to next bar or reset
//		if (currentBarInTransition < BAR_COUNT - 1 ) {
//			currentBarInTransition++;
//			transition.start();
//		} else {
//			resetAnimation();
//		}
	}
	
	private class Logo {
		
		/**
		 * Width of one bar in px
		 */
		private final static int BAR_WIDTH = 2;
		
		/**
		 * Number of bars in the logo
		 */
		private final static int BAR_COUNT = 3;
		
		/**
		 * Width of the logo)
		 */
		public final static int WIDTH = BAR_COUNT * BAR_WIDTH;
		
		private int xOffset;
		
		private float transitionProgress = 0f;
		
		private int[] barHeights = new int[BAR_COUNT];
		
		private AniSequence inOutTransition;
		
		public Logo() {
			
			// Calculate max bar height for each bar
			float barHeightDelta = BaseAnimation.PIXEL_RESOLUTION_Y / BAR_COUNT;
			for (int i = 0; i < BAR_COUNT; i++) {
				barHeights[i] = (int) Math.round((float) (i + 1) * (float) barHeightDelta);
			}
			
			// Init ANI
			inOutTransition = new AniSequence(applet);
			inOutTransition.beginSequence();
			inOutTransition.add(Ani.to(this, .5f, "transitionProgress", 1f, Ani.LINEAR)); // Fade in
			inOutTransition.add(Ani.to(this, .5f, 4f, "transitionProgress", 0f, Ani.LINEAR, "onEnd:change")); // Fade out
			inOutTransition.endSequence();
			
			change();
		}
		
		public void draw(PGraphics g) {
			g.pushMatrix();
			
			// Clamp transition progress to [0.0, 1.0]
			transitionProgress = Math.min(1f, Math.max(0f, transitionProgress));
			
			// Apply matrix transforms
			g.translate(xOffset, g.height);
			
			// Draw the three bars
			for (int i = 0; i < BAR_COUNT; i++) {
				g.rect(BAR_WIDTH * i, 0, BAR_WIDTH, -barHeights[i] * transitionProgress);
			}
		
			g.popMatrix();
		}
		
		private void change() {
			inOutTransition.start();
		}

		public void setXOffset(int xOffset) {
			this.xOffset = xOffset;
		}
		
	};
	
}
