package nl.sensorlab.videowall.animation.baseanimations;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class SwirlAnimation extends BaseAnimation {

	private final static int AMOUNT_PARTICLES = 40;

	private PVector[] particles;
	private int[] colors;
	private int baseColor = 0;

	public SwirlAnimation(PApplet applet) {
		super(applet);
		this.particles =  new PVector[AMOUNT_PARTICLES];
		this.colors =  new int[AMOUNT_PARTICLES];
		
		// Generate the particles
		generateParticles(AMOUNT_PARTICLES);
	}
	
	private void generateParticles(int amountParticles) {
		for(int i = 0; i < amountParticles; i++) {
			float x = (float)(Math.random() * PIXEL_RESOLUTION_X);
			float y = (float)(Math.random() * PIXEL_RESOLUTION_Y);
			particles[i] = new PVector(x, y);
			colors[i] = PColor.color((int)(((50 + (Math.random() * 30)) + baseColor) % 255), (int)(100 + (Math.random() * 155)), 255, 150);
		}
	}

	private void updateAndDraw(PGraphics g, double dt) {
		for(int p = 0; p < particles.length; p++) {
			
			// Update the position(s)
			PVector position = particles[p]; // Get the current PVector
			int color = colors[p];
			
			// Shift; create the swirl effect; how does it work exactly? 
			PVector shift =  new PVector();
			shift.x = position.y - PIXEL_RESOLUTION_Y/2;
			shift.y = (float)Math.sin((position.x * (Math.PI * 2)) / PIXEL_RESOLUTION_X) * PIXEL_RESOLUTION_Y/8f;
			 
			// Normalize the vector to a length of 1
			shift.normalize();
			shift.mult((float)dt * 10);
			
			// Add the shift vector to the position
			position.add(shift);
			
			// Keep in bounds
			if(position.x > PIXEL_RESOLUTION_X){
				position.x = 0;
			}else if(position.x < 0){
				position.x = PIXEL_RESOLUTION_X;
			}
			
			// Draw
			g.noFill();
			g.strokeWeight(2);
			g.stroke(color);
			g.point(position.x, position.y);
			
			// Update the color
			colors[p] = PColor.color((int)(((50 + (Math.random() * 30)) + baseColor) % 255), (int)(100 + (Math.random() * 155)), 255, 120);
		}
		
		//
		baseColor += 0.5f;
//		if(baseColor > 1020) baseColor = 0;
	}

	protected final void drawAnimationFrame(PGraphics g, double dt) {
		// Add some fade effect
		g.noStroke();
		g.fill(0, 5);
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
		
		// Update and draw the particles
		updateAndDraw(g, dt * 0.0005);
	}

}
