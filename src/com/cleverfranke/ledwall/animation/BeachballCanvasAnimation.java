package com.cleverfranke.ledwall.animation;

import com.cleverfranke.util.FileSystem;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PShape;

public class BeachballCanvasAnimation extends CanvasAnimation {
	
	private PImage image;
	private float rotation;
	
	public BeachballCanvasAnimation(PApplet applet) {
		super(applet);

		// Load image
		image = applet.loadImage(FileSystem.getApplicationPath("resources/beachball.png"));
		if (image != null) {
			image.resize((int) (getGeometry().width * 1.1f), 0);
		}
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		g.background(0);
		
		if (image == null) {
			return;
		}
		
		// Draw rotated image
		g.pushMatrix();
		g.translate(getGeometry().width/2, getGeometry().height/2);
		g.rotate(rotation);
		g.imageMode(PConstants.CENTER);
		g.image(image, 0, 0);
		g.popMatrix();
		rotation += .1f;
		
	}

}
