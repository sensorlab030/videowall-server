package nl.sensorlab.videowall.animation;

import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

/**
 * Animation that shows a number of horizontal wavy lines, driven by
 * random noise.  
 */
public class LineWaveAnimation extends BaseCanvasAnimation {
	
	private final int LINE_COUNT = 3;											// Number of lines to plot
	private final float OFFSCREEN_RATIO = .3f;									// How far the lines can move off screen
	private float[] xDiff = new float[LINE_COUNT];
	private float[] yOffset = new float[LINE_COUNT];							// Stores the yOffset of each line
	private int[] colors = {													// Line colors
			PColor.color(0, 255, 255), 
			PColor.color(255, 0, 255),
			PColor.color(255, 255, 0)
			};
	
	public LineWaveAnimation(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);
		
		// Initialize noise seed
		for (int i = 0; i < LINE_COUNT; i++) {
			xDiff[i] = 0.05f + (float) i * 0.10f;
			yOffset[i] = (float) i / (float) LINE_COUNT;
		}
	
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		g.background(0);
		g.blendMode(PConstants.SCREEN);
		g.strokeWeight((float) g.height * .1f);
		g.noFill();
		
		for (int i = 0; i < LINE_COUNT; i++) {
			
			// Draw line
			g.beginShape();
			float xOff = 0;
			for (float x = 0; x < g.width + 1; x += (float) getGeometry().width / 10f) {
				g.vertex(x,  PApplet.map(applet.noise(xOff, yOffset[i]), 0, 1, g.height * -OFFSCREEN_RATIO, g.height + g.height * OFFSCREEN_RATIO));
				xOff += xDiff[i];
			}
			g.stroke(colors[i]);
			g.endShape();
			
			// Update Y offset
			yOffset[i] += 0.01f;
			
		}
	}

}
