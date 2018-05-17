package nl.sensorlab.videowall.animation.baseanimations;

import java.util.ArrayList;
import com.cleverfranke.util.PColor;
import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class BouncyPixelsAnimation extends BaseAnimation{

	public PApplet parent;
	private ArrayList<BouncyPixel> bouncypixels;
	private ArrayList<Integer> bouncypixelstoremove; // <-- Store id's for pixels that exceed the lifetime
	
	private final int INIT_AMOUNT_BOUNCY_PIXELS = 100;
	private final int ADD_BOUNCY_PIXEL_EVERY_MILLIS = 500;
	private final int MAX_AMOUNT_BOUNCY_PIXELS = 1000;
	private final int MIN_LIFETIME_BOUNCY_PIXEL = 10000;
	private final int VARIATION_LIFETIME_BOUNCY_PIXEL = 15000;
	
	private int addBouncyPixelCounterMillis = 0;
	
	// Settings
	private final float SPRING_INTENSITY = 0.05f;
	private final float GRAVITY_INTENSITY = 0.015f;
	private final float FRICTION_INTENSITY = -0.45f;
	private static final int[] PIXEL_COLORS = {PColor.color(7, 93, 144), PColor.color(24, 147, 196), PColor.color(108, 208, 198), PColor.color(235, 232, 225)};

	public BouncyPixelsAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		this.bouncypixels = new ArrayList<BouncyPixel>();
		this.bouncypixelstoremove = new ArrayList<Integer>();
		
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
		float y = -(float)(Math.random() * BaseAnimation.PIXEL_RESOLUTION_Y);// Start outside window height
		final float diameter = 1; // <-- Because it are pixels we want this to be set to 1; we set the radius of the pixel diameter/2; When using the radius for an ellipse we should do radius * 2 (this is not applicable for now because we are using pixels (point) instead). 
		int id = bouncypixels.size(); // <-- The id which is actually the index
		int randomColor = PIXEL_COLORS[(int)Math.floor(Math.random() * PIXEL_COLORS.length)];
		int randomLifeTime = (int)(MIN_LIFETIME_BOUNCY_PIXEL + Math.random() * VARIATION_LIFETIME_BOUNCY_PIXEL); 
		bouncypixels.add(new BouncyPixel(this, x, y, randomLifeTime, diameter, id, randomColor));
	}
	
	private void clearBouncyPixels() {
		bouncypixels.clear();
	}
	
	private void removeBouncyPixel(int index) {
		bouncypixels.remove(index);
	}
	
	private void updateBouncyPixels(double dt) {
		// Reset the remove arraylist
		bouncypixelstoremove.clear();
		
		// Update pixels
		int index = 0;
		for(BouncyPixel bp : bouncypixels) {
			bp.collide(bouncypixels);
			bp.update();
			// Add the pixel id if exceeds
			if(bp.lifetimeMillis < bp.lifeTimeCounterMillis) {
				// Add the index of the bouncy pixel to the remove arraylist
				bouncypixelstoremove.add(index);
			}else {
				bp.lifeTimeCounterMillis += dt;
			}
			index++;
		}
				
		// Remove entries in a descending order; this will remove the elements from the list without undesirable side effects
		for(int i = bouncypixelstoremove.size() - 1; i >= 0; i--) {
			removeBouncyPixel(bouncypixelstoremove.get(i));
		}
	}
	
	private void drawBouncyPixels(PGraphics g) {
		for(BouncyPixel bp : bouncypixels) {
			bp.draw(g);
		}
	}

	protected void drawAnimationFrame(PGraphics g, double dt) {
		// Add some fade effect
		g.fill(0, 30);
		g.noStroke();
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
		
		// Update and draw the pixels
		updateBouncyPixels(dt);
		drawBouncyPixels(g);
		
		if(addBouncyPixelCounterMillis > ADD_BOUNCY_PIXEL_EVERY_MILLIS) {
			addBouncyPixelCounterMillis = 0;
			if(bouncypixels.size() < MAX_AMOUNT_BOUNCY_PIXELS) {
				addBouncyPixel(); // <-- Add a new pixel
			}
		}else {
			addBouncyPixelCounterMillis += dt;
		}
	}

	public class BouncyPixel {

		private BouncyPixelsAnimation parent;

		public PVector position;
		public PVector velocity;
		
		public float radius;
		public int id;
		private int color;
		
		public float lifeTimeCounterMillis = 0;
		public int lifetimeMillis;

		public BouncyPixel(BouncyPixelsAnimation parent, float x, float y, int lifetimeMillis, float diameter, int id, int color) {
			this.parent = parent;
			this.position = new PVector(x, y);
			this.velocity = new PVector(0, 0);
			this.radius = diameter/2;
			this.id = id;
			this.color = color;
			this.lifetimeMillis = lifetimeMillis;
		}

		private void collide(ArrayList<BouncyPixel> bouncypixels) {
			for (int i = 0; i < bouncypixels.size(); i++) {
				// Ignore self
				if(i != id){
					BouncyPixel b = bouncypixels.get(i);
					float dx = b.position.x - position.x;
					float dy = b.position.y - position.y;

					// The minimum distance
					float minDistance = b.radius + radius;

					// The actual distance
					float distance = (float)Math.sqrt(dx*dx + dy*dy);

					// If exceeds minimum distance
					if(distance < minDistance) {
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

		private void update() {
			// Only update the y velocity
			velocity.y += parent.GRAVITY_INTENSITY;

			// Update positions
			position.x += velocity.x;
			position.y += velocity.y;

			// Keep inbounds
			if (position.x + radius > BaseAnimation.PIXEL_RESOLUTION_X) {
				position.x = BaseAnimation.PIXEL_RESOLUTION_X - radius;
				velocity.x *= parent.FRICTION_INTENSITY;
			} else if (position.x -radius < 0) {
				position.x = radius;
				velocity.x *= parent.FRICTION_INTENSITY;
			}
			if (position.y + radius > BaseAnimation.PIXEL_RESOLUTION_Y) {
				position.y = BaseAnimation.PIXEL_RESOLUTION_Y -radius;
				velocity.y *= parent.FRICTION_INTENSITY;
			} else if (position.y - radius < 0) {
				position.y = radius;
				velocity.y *= parent.FRICTION_INTENSITY;
			}
		}

		private void draw(PGraphics g) {
			g.stroke(color);
			g.point(position.x, position.y);
		}
	}


}
