package nl.sensorlab.videowall.animation.baseanimations.bouncypixels;

import java.util.ArrayList;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;

public class BouncyPixelsAnimation extends BaseAnimation{

	public PApplet parent;

	BouncyPixelSystem  bouncypixelssytem;
	
	private final int AMOUNT_BOUNCY_PIXELS = 10;
	private final int ADD_BOUNCY_PIXEL_MILLIS = 1000;
	private final int MAX_AMOUNT_BOUNCY_PIXELS = 500;
	private int addBouncyPixelCounterMillis = 0;
	
	// Settings
	private final float SPRING_INTENSITY = 0.005f;
	private final float GRAVITY_INTENSITY = 0.075f;
	private final float FRICTION_INTENSITY = -0.35f;
	private static final int[] PIXEL_COLORS = {PColor.color(7, 93, 144), PColor.color(24, 147, 196), PColor.color(108, 208, 198), PColor.color(235, 232, 225)};

	public BouncyPixelsAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		this.bouncypixelssytem = new BouncyPixelSystem(this, PIXEL_COLORS, SPRING_INTENSITY, GRAVITY_INTENSITY, FRICTION_INTENSITY);
		
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
			if(bouncypixelssytem.bouncypixels.size() > MAX_AMOUNT_BOUNCY_PIXELS) {
				bouncypixelssytem.clear();
				bouncypixelssytem.generateBouncyPixels(AMOUNT_BOUNCY_PIXELS);
			}
		}else {
			addBouncyPixelCounterMillis += dt;
		}
		

	}
}
