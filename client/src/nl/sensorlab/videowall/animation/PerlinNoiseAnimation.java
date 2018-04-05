package nl.sensorlab.videowall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import jonas.Node;
import jonas.Particle;
import jonas.Pixel;
import jonas.TriggerObject;
import jonas.TriggerSequence;
import jonas.noiseParticle;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class PerlinNoiseAnimation extends BaseAnimation {

	public PApplet parent;

	ArrayList<noiseParticle> particles;

	public int[] particleColors = {PColor.color(245,20,147), PColor.color(69,33,124), PColor.color(7,153,242), PColor.color(255,255,255)};
	public int amountParticles = 20;
	
	public float noiseScale = 50;


	public PerlinNoiseAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		this.particles = new ArrayList<noiseParticle>();
		generateParticles(amountParticles);
	}

	protected void generateParticles(int _amountParticles) {
		for (int i = 0; i < _amountParticles; i++) {
			float alpha = parent.map(i, 0, amountParticles, 10, 250); // <-- Create more depth
			float strokeWeight = parent.map(i, 0, amountParticles, 1, 3); // <-- Create more depth
			int color = particleColors[parent.floor(parent.random(particleColors.length))]; // <-- Set random color from the array
			particles.add(new noiseParticle(this, parent.random(0, PIXEL_RESOLUTION_X), parent.random(0, PIXEL_RESOLUTION_Y), alpha, strokeWeight, color));
		}
	}

	protected void drawParticles(PGraphics g) {
		for (noiseParticle p : particles) {
			p.update();
			p.display(g);
		}
	}

	@Override
	protected void drawAnimationFrame(PGraphics g) {
		// Draw particles
		drawParticles(g);
	}
}
