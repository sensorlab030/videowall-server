package nl.sensorlab.videowall.animation.baseanimations.bouncypixels;

import java.util.ArrayList;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PGraphics;

public class BouncyPixelSystem {
	
	private BouncyPixelsAnimation parent;
	
	public ArrayList<BouncyPixel> bouncypixels;
	private int[] colors;
	
	public float spring;
	public float gravity;
	public float friction;
	
	public BouncyPixelSystem(BouncyPixelsAnimation parent, int[] colors, float spring, float gravity, float friction) {
		this.parent = parent;
		this.colors = colors;
		this.spring = spring;
		this.gravity = gravity;
		this.friction = friction;
		this.bouncypixels = new ArrayList<BouncyPixel>();
	}
	
	public void generateBouncyPixels(int amountBouncyPixels) {
		for(int i = 0; i < amountBouncyPixels; i++) {
			addBouncyPixel();
		}
	}
	
	public void addBouncyPixel() { 
		float x = (float)(Math.random() * BaseAnimation.PIXEL_RESOLUTION_X);
		float y = -(float)(Math.random() * BaseAnimation.PIXEL_RESOLUTION_Y);// Start outside window height
		float diameter = 1; // <-- Because it are pixels we want this to be set to 1
		int id = bouncypixels.size() - 1;
		int randomColor = colors[(int)Math.floor(Math.random() * colors.length)];
		bouncypixels.add(new BouncyPixel(this, x, y, diameter, id, randomColor));
	}
	
	public void clear() {
		bouncypixels.clear();
	}
	
	public void update() {
		for(BouncyPixel bp : bouncypixels) {
			bp.collide(bouncypixels);
			bp.update();
		}
	}
	
	public void draw(PGraphics g) {
		for(BouncyPixel bp : bouncypixels) {
			bp.draw(g);
		}
	}
}
