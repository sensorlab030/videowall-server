package com.cleverfranke.ledwall;

import com.cleverfranke.ledwall.animation.PixelAnimation;
import com.cleverfranke.util.FileSystem;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class ImagePixelAnimation extends PixelAnimation {
	
	private PImage image;

	public ImagePixelAnimation(PApplet applet, String imageFile) {
		super(applet);
		image = applet.loadImage(FileSystem.getApplicationPath("resources/" + imageFile));
		if (image != null) {
			image.resize(PIXEL_RESOLUTION_X, 0);
		}
	}

	@Override
	protected void drawAnimationFrame(PGraphics g) {
		g.pushMatrix();
		g.background(0);
		g.imageMode(PConstants.CENTER);
		g.image(image, PIXEL_RESOLUTION_X / 2, PIXEL_RESOLUTION_Y / 2);
		g.popMatrix();
	}

}
