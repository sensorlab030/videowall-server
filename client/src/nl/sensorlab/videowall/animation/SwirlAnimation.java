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
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class SwirlAnimation extends BaseAnimation {

	// Based on: https://www.openprocessing.org/sketch/418124
	public PApplet parent;

	ArrayList<Particle> particles;
	
	public int amountParticles = 24;
	public float baseColor = 0;

	public SwirlAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		
		particles = new ArrayList<Particle>();
		generateParticles(amountParticles);
	}
	
	public void generateParticles(int _amountParticles) {
		for (int i = 0; i <  _amountParticles; i++) {
			Particle p = new Particle(this);
			particles.add(p);
		}
	}
	
	@Override
	protected void drawAnimationFrame(PGraphics g, double t) {
		// Add some fade effect
		g.noStroke();
		g.fill(0, 4);
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
		
		// Draw particles
		for(Particle p : particles) {
			p.update((float)0.001);
			p.display(g);
		}
		
		baseColor += 0.15f;
		if(baseColor > 1020) baseColor = 0;
	}
}
