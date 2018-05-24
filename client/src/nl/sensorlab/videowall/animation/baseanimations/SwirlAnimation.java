package nl.sensorlab.videowall.animation.baseanimations;

import java.awt.Color;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class SwirlAnimation extends BaseAnimation {

	// Todo: maybe use something more logical: https://www.openprocessing.org/sketch/122600
	
	private final static int AMOUNT_PARTICLES = 50;

	private PVector[] particles;
	private int[] colors;
	
	private int colorGlobalRed = 0;
	private int colorDirection = 1;

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

			// Set the particle position
			particles[i] = new PVector(x, y);

			// Set the color with a random offset
			int red = colorGlobalRed; // Update the global red color in the update function
			int green = (int)(100 + (Math.random() * 100));
			int blue = 255;
			
			colors[i] = PColor.color(red, green, blue);
		}
	}

	private void updateAndDraw(PGraphics g, double dt) {
		// Update the particles -> PVectors
		for(int p = 0; p < particles.length; p++) {

			// Update the position(s)
			PVector position = particles[p]; // Get the current PVector
			int color = colors[p];

			// Shift; create the swirl effect: not sure how this works?
			PVector shift =  new PVector();
			shift.x = position.y - PIXEL_RESOLUTION_Y/2;
			shift.y = (float)Math.sin((position.x * (Math.PI * 2)) / PIXEL_RESOLUTION_X) * PIXEL_RESOLUTION_Y/8f;

			// Normalize the vector to a length of 1
			shift.normalize();
			shift.mult((float)(dt * 0.0035));

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
			colors[p] = PColor.color(colorGlobalRed, PColor.green(colors[p]), PColor.blue(colors[p]), 150);
		}
		
		// Reset direction -> fade back to 255 or 0
		if(colorGlobalRed >= 255) colorDirection = -1;
		if(colorGlobalRed <= 0) colorDirection = 1;
		
		// Update the global red color
		colorGlobalRed += colorDirection;
	}

	protected final void drawAnimationFrame(PGraphics g, double dt) {
		// Add some fade effect
		g.noStroke();
		g.fill(0, 5);
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);

		// Update and draw the particles
		updateAndDraw(g, dt);
	}
}
