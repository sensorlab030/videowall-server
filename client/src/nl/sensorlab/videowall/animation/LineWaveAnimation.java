package nl.sensorlab.videowall.animation;

import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;

/**
 * Animation that shows a number of horizontal wavy lines, driven by
 * random noise.  
 */
public class LineWaveAnimation extends BaseCanvasAnimation {
	
	private final int LINE_COUNT = 3;											// Number of lines to plot
	
	private float[] yOffset = new float[LINE_COUNT];							// Stores the yOffset of each line
	private int[] colors = {													// Line colors
			PColor.color(0, 255, 255, 100), 
			PColor.color(255, 0, 255, 100),
			PColor.color(255, 255, 0, 100)
			};
	
	public LineWaveAnimation(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);
		
		// Initialize noise seed
		for (int i = 0; i < LINE_COUNT; i++) {
			yOffset[i] = (float) i / (float) LINE_COUNT;
		}
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		g.background(0);
		g.noFill();
		g.strokeWeight((float) (0.25*g.height));
		g.blendMode(PConstants.SCREEN);
		
		for (int i = 0; i < LINE_COUNT; i++) {
			
			PShape line = g.createShape();
			line.beginShape(PConstants.LINE);
			
			float xOff = 0;
			for (float x = 0; x < g.width + 1; x += (float) getGeometry().width / 100f) {
				float y = PApplet.map(applet.noise(xOff, yOffset[i]), 0, 1, 0, g.height);
				line.vertex(x,  y);
				xOff += 0.25f;
			}
			
			// Draw shape
			line.endShape();
			g.stroke(colors[i]);
			g.shape(line);
			
			// Update Y offset
			yOffset[i] += 0.01f;
			
		}
	}

}
