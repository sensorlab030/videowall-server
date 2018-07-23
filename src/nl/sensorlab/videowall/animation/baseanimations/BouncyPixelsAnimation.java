package nl.sensorlab.videowall.animation.baseanimations;

import java.util.ArrayList;
import java.util.Iterator;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class BouncyPixelsAnimation extends BaseAnimation{

	private ArrayList<BouncyPixel> bouncyPixels;

	private final int INIT_AMOUNT_BOUNCY_PIXELS = 100;
	private final int ADD_BOUNCY_PIXEL_EVERY_MILLIS = 500;
	private final int MAX_AMOUNT_BOUNCY_PIXELS = 1000;
	private final int MIN_LIFETIME_BOUNCY_PIXEL = 15000;
	private final int VARIATION_LIFETIME_BOUNCY_PIXEL = 15000;

	private int addBouncyPixelCounterMillis = 0;

	// Settings
	private final float SPRING_INTENSITY = 0.0005f;
	private final float GRAVITY_INTENSITY = 0.00015f;
	private final float FRICTION_INTENSITY = -0.45f;
	private static final int[] PIXEL_COLORS = {PColor.color(7, 93, 144), PColor.color(24, 147, 196), PColor.color(108, 208, 198), PColor.color(235, 232, 225)};

	public BouncyPixelsAnimation(PApplet applet) {
		super(applet);
		this.bouncyPixels = new ArrayList<BouncyPixel>();

		// Generate bouncy pixels
		generateBouncyPixels(INIT_AMOUNT_BOUNCY_PIXELS);
	}

	private void generateBouncyPixels(int amountBouncyPixels) {
		for(int i = 0; i < amountBouncyPixels; i++) {
			addBouncyPixel();
		}
	}

	private void addBouncyPixel() { 
		float x = (float)(Math.random() * BaseAnimation.PIXEL_RESOLUTION_X);
		float y = -(float)(Math.random() * BaseAnimation.PIXEL_RESOLUTION_Y); // Start outside window height
		int randomColor = PIXEL_COLORS[(int)Math.floor(Math.random() * PIXEL_COLORS.length)];
		int randomLifeTime = (int)(MIN_LIFETIME_BOUNCY_PIXEL + Math.random() * VARIATION_LIFETIME_BOUNCY_PIXEL); 
		bouncyPixels.add(new BouncyPixel(this, x, y, randomLifeTime, randomColor));
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double dt) {
		
		// Add some fade effect
		g.fill(0, 30);
		g.noStroke();
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);

		// Update pixels
		Iterator<BouncyPixel> it = bouncyPixels.iterator();
		while (it.hasNext()) {
			BouncyPixel bp = it.next();
			
			if (bp.lifeTimeCounterMillis > bp.lifetimeMillis) {
				it.remove();
			} else {
				bp.lifeTimeCounterMillis += dt;
				bp.collide(bouncyPixels);
				bp.update(dt);
				bp.draw(g);
			}
			
		}

		if (addBouncyPixelCounterMillis > ADD_BOUNCY_PIXEL_EVERY_MILLIS) {
			addBouncyPixelCounterMillis = 0;
			if (bouncyPixels.size() < MAX_AMOUNT_BOUNCY_PIXELS) {
				addBouncyPixel();
			}
		} else {
			addBouncyPixelCounterMillis += dt;
		}
	}

	public class BouncyPixel {

		private BouncyPixelsAnimation parent;

		public PVector position;
		public PVector velocity;

		public static final float RADIUS = 0.5f;
		private int color;

		public float lifeTimeCounterMillis = 0;
		public int lifetimeMillis;

		public BouncyPixel(BouncyPixelsAnimation parent, float x, float y, int lifetimeMillis, int color) {
			this.parent = parent;
			this.position = new PVector(x, y);
			this.velocity = new PVector(0, 0);
			this.color = color;
			this.lifetimeMillis = lifetimeMillis;
		}

		private void collide(ArrayList<BouncyPixel> bouncypixels) {
			
			for (int i = 0; i < bouncypixels.size(); i++) {
				BouncyPixel b = bouncypixels.get(i);
				
				// Ignore self
				if (b != this){
					
					float dx = b.position.x - position.x;
					float dy = b.position.y - position.y;

					// The minimum distance
					float minDistance = RADIUS * 2;

					// The actual distance
					float distance = (float)Math.sqrt(dx*dx + dy*dy);

					// If exceeds minimum distance
					if (distance < minDistance) {
						float angle = (float)Math.atan2(dy, dx);
						float targetX = position.x + (float)Math.cos(angle) * minDistance;
						float targetY = position.y + (float)Math.sin(angle) * minDistance;

						float ax = (targetX - b.position.x) * parent.SPRING_INTENSITY;
						float ay = (targetY - b.position.y) * parent.SPRING_INTENSITY;

						// Update velocity
						velocity.x -= ax;
						velocity.y -= ay;

						// Update b (change direction)
						b.velocity.x += ax;
						b.velocity.y += ay;
					}		
				}
			}
		}

		private void update(double dt) {
			// Only update the y velocity
			velocity.y += parent.GRAVITY_INTENSITY;

			// Update positions
			position.x += velocity.x * dt;
			position.y += velocity.y * dt;

			// Keep inbounds
			if (position.x + RADIUS > BaseAnimation.PIXEL_RESOLUTION_X) {
				position.x = BaseAnimation.PIXEL_RESOLUTION_X - RADIUS;
				velocity.x *= parent.FRICTION_INTENSITY;
			} else if (position.x -RADIUS < 0) {
				position.x = RADIUS;
				velocity.x *= parent.FRICTION_INTENSITY;
			}
			if (position.y + RADIUS > BaseAnimation.PIXEL_RESOLUTION_Y) {
				position.y = BaseAnimation.PIXEL_RESOLUTION_Y -RADIUS;
				velocity.y *= parent.FRICTION_INTENSITY;
			} else if (position.y - RADIUS < 0) {
				position.y = RADIUS;
				velocity.y *= parent.FRICTION_INTENSITY;
			}
		}

		private void draw(PGraphics g) {
			g.stroke(color);
			g.point(position.x, position.y);
		}
	}


}
