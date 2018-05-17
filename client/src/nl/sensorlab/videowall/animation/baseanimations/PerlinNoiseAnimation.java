package nl.sensorlab.videowall.animation.baseanimations;

import java.util.ArrayList;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class PerlinNoiseAnimation extends BaseAnimation{

	PApplet parent;

	private ArrayList<PerlinParticle> particles;

	protected static final int[] PARTICLE_COLORS = {PColor.color(7, 93, 144), PColor.color(24, 147, 196), PColor.color(108, 208, 198), PColor.color(235, 232, 225)};
	private final static int AMOUNT_PARTICLES = 20;
	protected static float NOISE_SCALE = 50;
	
	private boolean init = true;

	public PerlinNoiseAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		this.particles = new ArrayList<PerlinParticle>();

		// Generate the particles
		generateParticles(AMOUNT_PARTICLES);
	}
	
	//public PerlinParticle(PerlinNoiseAnimation parent, float x, float y, float speed, int color, float alpha, float strokeWidth) {
	protected void generateParticles(int amountParticles) {
		for (int i = 0; i < amountParticles; i++) {
			float x = (float)(Math.random() * BaseAnimation.PIXEL_RESOLUTION_X);
			float y = (float)(Math.random() * BaseAnimation.PIXEL_RESOLUTION_Y);
			
			// <-- Create more depth
			float alpha = PApplet.map(i, 0, amountParticles, 10, 250); 
			float strokeWeight = PApplet.map(i, 0, amountParticles, 0.5f, 2.0f);
			
			//Set random color from the array
			int color = PARTICLE_COLORS[PApplet.floor(parent.random(PARTICLE_COLORS.length))];
			
			// Set random speed
			float randomSpeed = (float)(0.0025f + Math.random() * 0.0025);
			
			// Create the new particle
			particles.add(new PerlinParticle(this, x, y, randomSpeed, color, alpha, strokeWeight));
		}
	}

	protected void drawParticles(PGraphics g, double dt) {
		// On init set the background black.
		if(init) {
			init = false;
			g.background(0);
		}
		
		// Draw and update the perlin noise particles
		for (PerlinParticle p : particles) {
			p.update(dt);
			p.draw(g);
		}
	}

	protected void drawAnimationFrame(PGraphics g, double dt) {
		// Draw and update the particles
		drawParticles(g, dt);
	}
	
	public class PerlinParticle {

		private PerlinNoiseAnimation parent;

		private PVector position;
		private PVector direction;
		private PVector velocity;

		private float speed;
		private float alpha;
		private float strokeWidth;

		private int color;

		public PerlinParticle(PerlinNoiseAnimation parent, float x, float y, float speed, int color, float alpha, float strokeWidth) {
			this.parent = parent;
			this.position = new PVector(x, y);
			this.direction = new PVector(0, 0);
			this.velocity = new PVector(0, 0);
			this.speed = speed;
			this.color = color;
			this.alpha = alpha;
			this.strokeWidth = strokeWidth;
		}

		public void update(double dt) {
			//Perlin noise is a random sequence generator producing a more natural, harmonic succession of numbers than that of the standard random() function. 
			//It was developed by Ken Perlin in the 1980s and has been used in graphical applications to generate procedural textures, shapes, terrains, and other seemingly organic forms.
			
			// Update angle
			float angle = parent.parent.noise(position.x/PerlinNoiseAnimation.NOISE_SCALE, position.y/PerlinNoiseAnimation.NOISE_SCALE) * PConstants.TWO_PI * PerlinNoiseAnimation.NOISE_SCALE;

			direction.x =  PApplet.cos(angle);
			direction.y =  PApplet.sin(angle);

			velocity = direction.copy();
			velocity.mult(speed * (float)dt);

			position.add(velocity);

			// Check if within bounds
			if (position.x > BaseAnimation.PIXEL_RESOLUTION_X || position.x < 0 || position.y < 0 || position.y > BaseAnimation.PIXEL_RESOLUTION_Y) {
				float x = (float)(Math.random() * BaseAnimation.PIXEL_RESOLUTION_X);
				float y = (float)(Math.random() * BaseAnimation.PIXEL_RESOLUTION_Y);
				this.position = new PVector(x,y);
			}
		}

		public void draw(PGraphics g) {
			g.noFill();
			g.strokeWeight(strokeWidth);
			g.stroke(color, alpha);
			g.point(position.x, position.y);
		}

	}
}