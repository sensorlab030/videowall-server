package nl.sensorlab.videowall.animation;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Animation that shows the OSX beach ball of death
 */
public class BeachballAnimation extends BaseCanvasAnimation {
	
	private final static float ROTATION_SPEED = 0.002f;
	
	private PImage image;
	private float rotation;
	
	public BeachballAnimation(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);
		
		// Fetch file from resources
		String file = getResource("beachball.png");
		if (file == null) {
			System.err.println("Failed to locate beachball file");
			return;
		}

		// Load image
		image = applet.loadImage(file);
		if (image != null) {
			image.resize((int) (getGeometry().width * 1.1f), 0);
		}
		
	}
	
	@Override
	public void update(double dt) {
		rotation += ROTATION_SPEED * dt;
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		g.background(0);
		
		if (image == null) {
			return;
		}
		
		// Draw rotated image
		g.pushMatrix();
		g.translate(g.width/2, g.height/2);
		g.rotate(rotation);
		g.imageMode(PConstants.CENTER);
		g.image(image, 0, 0);
		g.popMatrix();
		
	}

}

