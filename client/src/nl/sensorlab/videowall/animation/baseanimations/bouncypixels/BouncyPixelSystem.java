package nl.sensorlab.videowall.animation.baseanimations.bouncypixels;

import java.util.ArrayList;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PGraphics;

public class BouncyPixelSystem {
	
	BouncyPixelsAnimation parent;
	
	public ArrayList<BouncyPixel> bouncypixels;
	
	public float spring = 0.15f;
	public float gravity = 0.015f;
	public float friction = -0.15f;
	
	public BouncyPixelSystem(BouncyPixelsAnimation parent, float spring, float gravity, float friction) {
		this.parent = parent;
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
		float diameter = (float)(1 + Math.random() * 3);
		int id = bouncypixels.size() - 1;
		int color = 255;
		bouncypixels.add(new BouncyPixel(this, x, y, diameter, id, color));
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
