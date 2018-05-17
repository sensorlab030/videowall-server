package nl.sensorlab.videowall.animation.baseanimations.perlinnoise;

import java.util.ArrayList;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;

public class PerlinNoiseAnimation extends BaseAnimation{

	PApplet parent;

	private ArrayList<PerlinParticle> particles;

	private final static int[] COLORS = {PColor.color(245,20,147), PColor.color(69,33,124), PColor.color(7,153,242), PColor.color(255,255,255)};
	private final static int AMOUNT_PARTICLES = 20;
	protected static float NOISE_SCALE = 50;

	public PerlinNoiseAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		this.particles = new ArrayList<PerlinParticle>();

		// Generate the particles
		generateParticles(AMOUNT_PARTICLES);
	}

	protected void generateParticles(int amountParticles) {
		for (int i = 0; i < amountParticles; i++) {
			float alpha = PApplet.map(i, 0, amountParticles, 10, 250); // <-- Create more depth
			float strokeWeight = PApplet.map(i, 0, amountParticles, 1, 3); // <-- Create more depth
			int color = COLORS[PApplet.floor(parent.random(COLORS.length))]; // <-- Set random color from the array
			//		particles.add(new noiseParticle(this, parent.random(0, PIXEL_RESOLUTION_X), parent.random(0, PIXEL_RESOLUTION_Y), alpha, strokeWeight, color));
		}
	}

	protected void drawParticles(PGraphics g) {
		for (PerlinParticle p : particles) {
			p.update();
			p.draw(g);
		}
	}

	protected void drawAnimationFrame(PGraphics g, double dt) {
		// Draw and update the particles
		drawParticles(g);
	}
}


//public class PerlinNoiseAnimation extends BaseAnimation {
//
//	public PApplet parent;
//
//	ArrayList<noiseParticle> particles;
//
//	private final int[] COLORS = {PColor.color(245,20,147), PColor.color(69,33,124), PColor.color(7,153,242), PColor.color(255,255,255)};
//	public int amountParticles = 20;
//	
//	public float noiseScale = 50;
//
//
//	public PerlinNoiseAnimation(PApplet applet) {
//		super(applet);
//		this.parent = applet;
//		this.particles = new ArrayList<noiseParticle>();
//		generateParticles(amountParticles);
//	}
//
//	protected void generateParticles(int _amountParticles) {
//		for (int i = 0; i < _amountParticles; i++) {
//			float alpha = parent.map(i, 0, amountParticles, 10, 250); // <-- Create more depth
//			float strokeWeight = parent.map(i, 0, amountParticles, 1, 3); // <-- Create more depth
//			int color = particleColors[parent.floor(parent.random(particleColors.length))]; // <-- Set random color from the array
//			particles.add(new noiseParticle(this, parent.random(0, PIXEL_RESOLUTION_X), parent.random(0, PIXEL_RESOLUTION_Y), alpha, strokeWeight, color));
//		}
//	}
//
//	protected void drawParticles(PGraphics g) {
//		for (noiseParticle p : particles) {
//			p.update();
//			p.display(g);
//		}
//	}
//
//	@Override
//	protected void drawAnimationFrame(PGraphics g, double t) {
//		// Draw particles
//		drawParticles(g);
//	}
//}