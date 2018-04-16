package nl.sensorlab.videowall.animation.canvasanimations;

import java.util.ArrayList;
import java.util.Random;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import de.looksgood.ani.AniSequence;
import nl.sensorlab.videowall.animation.BaseCanvasAnimation;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class SensorlabLogoAnimation extends BaseCanvasAnimation {
	
	private ArrayList<Square> squares = new ArrayList<>();

	public SensorlabLogoAnimation(PApplet applet) {
		super(applet, BaseCanvasAnimation.DEFAULT_SCALE, CANVAS_MODE_2D);
	}
	
	@Override
	public void isStarting() {
		
		// Define square size
		int width = getGeometry().width;
		int height = getGeometry().height;
		final int squaresize = height;
		
		// Create squares
		for (int y = 0; y < height; y += squaresize) {
			for (int x = 0; x < width; x += squaresize) {
				squares.add(new Square(new PVector(x + squaresize / 2, y + squaresize / 2), applet, squaresize));
			}
		}
		
	}
	
	@Override
	protected void drawCanvasAnimationFrame(PGraphics g, double dt) {
		
		// Draw all squares
		for (Square s: squares) {
			s.draw(g);
		}
		
	}
	
	@Override
	public void isStopping() {
		squares.clear();
	}
	
	/**
	 * Class that represents one square
	 */
	public class Square {
		
		private int size;
		private int halfsize;
		
		private Pattern pattern;
		private PVector pos;
		private float rotation;
		private boolean blackOnWhite;
		
		private AniSequence inOutTransition;
		private float transitionProgress = 0f;
		
		public Square(PVector pos, PApplet applet, int size) {
			this.size = size;
			this.halfsize = size / 2;
			this.pos = pos;
			
			float t = (float) (4f + Math.random() * 8f); 
			
			// Setup transition sequence
			inOutTransition = new AniSequence(applet);
			inOutTransition.beginSequence();
			inOutTransition.add(Ani.to(this, .5f, "transitionProgress", 1f, Ani.LINEAR)); // Fade in
			inOutTransition.add(Ani.to(this, .5f, t, "transitionProgress", 0f, Ani.LINEAR, "onEnd:change")); // Fade out
			inOutTransition.endSequence();
			
			// Randomize (and start transition)
			change();
			
		}
		
		public void draw(PGraphics g) {
			g.pushMatrix();
			g.pushStyle();
			g.translate(pos.x, pos.y);
			g.rotate(rotation);
			
			transitionProgress = Math.min(1f, Math.max(0f, transitionProgress));
			
			// Determine colors
			int colorF = blackOnWhite ? PColor.color(0) : PColor.color(255);
			int colorB = blackOnWhite ? PColor.color(255) : PColor.color(0);
			
			// BG
			g.fill(colorB);
			g.noStroke();
			g.rect(-halfsize, -halfsize, size, size);
			
			switch (pattern) {
				case BLANK: 
					break;
				case LINES: {
					g.noFill();
					g.stroke(colorF);
					g.strokeWeight(size / 8);
					g.strokeCap(PConstants.SQUARE);
					
					g.line(-halfsize, -halfsize * .5f, -halfsize + transitionProgress * size, -halfsize * .5f);
					g.line(halfsize, 0, halfsize - transitionProgress * size, 0);
					g.line(-halfsize, halfsize * .5f, -halfsize + transitionProgress * size, halfsize * .5f);
					
				} break;
				default:
					System.err.println("Unimplemented pattern: " + pattern);
					break;
				
			}
			
			// Solid part
			g.noStroke();
			g.fill(colorF);
			g.beginShape(PConstants.TRIANGLE_FAN);
			g.vertex(-halfsize, -halfsize);
			g.vertex(-halfsize, halfsize);
			g.vertex(halfsize, -halfsize);
			g.endShape();
			
			// Edge
			g.noFill();
			g.strokeWeight(2);
			g.stroke(0);
			g.rect(-halfsize, -halfsize, size, size);
			
			g.popStyle();
			g.popMatrix();
		}
		
		public void change() {
			pattern = randomPattern();
			rotation = randomRotation();
			
			// Start transition
			inOutTransition.start();
		}
		
		private Pattern randomPattern() {
			int pick = new Random().nextInt(Pattern.values().length);
			return Pattern.values()[pick];
		}
		
		private float randomRotation() {
			int random = new Random().nextInt(4);
			return ((float) random) * 0.5f * PConstants.PI;
		}
			
	}
	
	/**
	 * Enumeration for the square patterns
	 */
	public enum Pattern {
		BLANK,
		LINES
	}

}
