package nl.sensorlab.videowall.animation.baseanimations.bouncypixels;

import java.util.ArrayList;
import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;

public class BouncyPixelsAnimation extends BaseAnimation{

	public PApplet parent;

	BouncyPixelSystem  bouncypixelssytem;
	
	private final int AMOUNT_BOUNCY_PIXELS = 10;
	private final int ADD_BOUNCY_PIXEL_MILLIS = 1000;
	private int addBouncyPixelCounterMillis = 0;

	public BouncyPixelsAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		this.bouncypixelssytem = new BouncyPixelSystem(this ,0.05f, 0.075f, -0.35f);
		
		// Generate bouncy pixels
		bouncypixelssytem.generateBouncyPixels(AMOUNT_BOUNCY_PIXELS);
	}

	protected void drawAnimationFrame(PGraphics g, double dt) {
		// Add some fade effect
		g.fill(0, 50);
		g.noStroke();
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
		
		bouncypixelssytem.update();
		bouncypixelssytem.draw(g);
		
		
		if(addBouncyPixelCounterMillis > ADD_BOUNCY_PIXEL_MILLIS) {
			addBouncyPixelCounterMillis = 0;
			bouncypixelssytem.addBouncyPixel();
		}else {
			addBouncyPixelCounterMillis += dt;
		}
		

	}
}
