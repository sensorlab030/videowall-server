package nl.sensorlab.videowall.animation.canvasanimations;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseCanvasAnimation;
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
	
	private int[] colors = {													// Line colors
			PColor.color(0, 255, 255), 
			PColor.color(255, 0, 255),
			PColor.color(255, 255, 0)
			};
	
	private Line[] lines = new Line[LINE_COUNT];
	
	public LineWaveAnimation(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);
		
		// Initialize lines
		float vertexSpacing = getGeometry().width / 10;
		for (int i = 0; i < LINE_COUNT; i++) {
			lines[i] = new Line(
					vertexSpacing, 
					 0.0001f * (i + 1), 
					colors[i], applet);
		}
	
	}
	

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g, double dt) {
		g.background(0);
		g.blendMode(PConstants.SCREEN);
		g.strokeWeight((float) g.height * .1f);
		g.noFill();
		
		// Draw lines
		for (Line l: lines) {
			l.draw(g, dt);
		}

	}
	
	public class Line {

		private float vertexSpacing;
		private float noiseY;
		private float noiseSpeed;
		private int color;
		
		public Line(float vertexSpacing, float noiseSpeed, int color, PApplet applet) {
			this.vertexSpacing = vertexSpacing;
			this.noiseSpeed = noiseSpeed;
			this.color = color;
			
			noiseY = (float) Math.random();
		}
		
		public void draw(PGraphics g, double dt) {
			noiseY += noiseSpeed * dt;
			
			// Draw line
			g.beginShape();
			for (float x = 0; x < g.width + 1; x += vertexSpacing) {
				float noiseVal = applet.noise(x * 0.005f, noiseY);
				float y = PApplet.map(noiseVal, 0, 1, (g.height * -OFFSCREEN_RATIO), (g.height + g.height * OFFSCREEN_RATIO));
				g.vertex(x, y);
			}
			g.stroke(color);
			g.endShape();
		}
		
	}

}
