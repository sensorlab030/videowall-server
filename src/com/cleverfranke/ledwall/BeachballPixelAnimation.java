package com.cleverfranke.ledwall;

import com.cleverfranke.util.FileSystem;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class BeachballPixelAnimation extends BasePixelAnimation {
	
	PImage image;
	float rotation;

	public BeachballPixelAnimation(PApplet applet) {
		super(applet);
		image = applet.loadImage(FileSystem.getApplicationPath("resources/beachball.png"));
		if (image != null) {
			image.resize(0, (int) (PIXEL_RESOLUTION_Y * 1.1f));
		}
	}

	@Override
	protected void doDraw(PGraphics g) {
		g.pushMatrix();
		g.background(0);
		g.translate(PIXEL_RESOLUTION_X/2, PIXEL_RESOLUTION_Y/2);
		g.rotate(rotation);
		g.imageMode(PConstants.CENTER);
		g.image(image, 0, 0);
		g.popMatrix();
		rotation += .1f;
	}

}
