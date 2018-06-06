package nl.sensorlab.videowall.animation.baseanimations;

import java.util.ArrayList;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class FishTankAnimation extends BaseAnimation{

	// Based on: https://www.openprocessing.org/sketch/162912

	private PApplet parent;

	private ArrayList<Fish> fishes;
	
	private final static int AMOUNT_FISHES = 20;
	private static final int[] FISH_COLORS = {PColor.color(7, 93, 144), PColor.color(24, 147, 196), PColor.color(108, 208, 198), PColor.color(235, 232, 225)};


	public FishTankAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		this.fishes = new ArrayList<Fish>();

		// Generate the fishes
		generateFishes(AMOUNT_FISHES);
	}

	protected void generateFishes(int amountFishes) {
		for(int i = 0; i < amountFishes; i++) {
			float x = (float)(Math.random() * PIXEL_RESOLUTION_X);
			float y = (float)(Math.random() * PIXEL_RESOLUTION_Y);
			
			// Set random color
			int color = FISH_COLORS[(int)Math.floor(Math.random()*FISH_COLORS.length)];
			
			// Create a 'fish'
			fishes.add(new Fish(x, y, color));
		}
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double dt) {
		// Add some fade effect
		g.noStroke();
		g.fill(0, 0, 30, 30);
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
		
		// Display the fish shapes
		for(Fish f: fishes) {
			f.updateAndDraw(g, parent.frameCount);
		}
	}

	private class Fish {

		private PVector position;
		private PVector velocity;
		private int color;

		private float scale;
		private float angle;
		
		private final static float VELOCITY_OFFSET = 0.075f;
		// How many pixels can the fish go outside of the canvas before it resets it's position.
		private final static int OFFSET_BOUNDING_BOX = 20; 

		public Fish(float x, float y, int color) {
			this.position = new PVector(x, y);
			this.color = color;
			this.velocity = new PVector(-VELOCITY_OFFSET + (float)Math.random() * (VELOCITY_OFFSET * 2), -VELOCITY_OFFSET + (float)Math.random() * (VELOCITY_OFFSET * 2));
			
			// Randomize the scale so fishes have different sizes
			this.scale = 0.015f + (float)Math.random() * 0.025f;
			
			// Randomize the angle
			this.angle = 0;//-90f + (float)Math.random() * 180;
		}

		public void updateAndDraw(PGraphics g, int frameCount) {
			position.add(velocity);

			// Fill the shape
			g.fill(color, 30);
			g.stroke(color, 30);
			g.strokeJoin(g.ROUND);

			// Translate, scale and rotate the shape
			g.pushMatrix();
			g.translate(position.x, position.y);
			g.scale(scale);
			//	Calculate the angle of rotation and subtract 90 degrees to make the shape point in the correct direction
			g.rotate(velocity.heading() - PApplet.radians(90));

			// Start drawing the vertex which will make up the fish shape
			g.beginShape();

			// Create the right side of the fish shape
			for(int y = 0; y < 180; y+=20) {
				float x = (float)Math.sin(PApplet.radians(y)) * y/3; // Increase the offset a bit (shapes appear wider)
				float a = (float)Math.sin(PApplet.radians((float)(y + angle + frameCount * 3))) * 50;
				g.vertex(x + a, y);
			}

			// Create the left side of the fish shape
			for (int y = 180; y >= 0; y-=20) {
				float x = (float)Math.sin(PApplet.radians(y)) * y/3; // Increase the offset a bit (shapes appear wider)
				float a = (float)Math.sin(PApplet.radians((float)(y + angle +  frameCount * 3))) * 50;
				g.vertex(- (x - a), y);
			}
			
			// End the fish shape
			g.endShape();
			g.popMatrix();

			// Make sure the shapes (center position) are kept within the bounding-box.
			if(position.x > BaseAnimation.PIXEL_RESOLUTION_X + OFFSET_BOUNDING_BOX) {
				position.x = -OFFSET_BOUNDING_BOX;
			}else if(position.x < -OFFSET_BOUNDING_BOX) {
				position.x =  BaseAnimation.PIXEL_RESOLUTION_X + OFFSET_BOUNDING_BOX;
			}
			if(position.y > BaseAnimation.PIXEL_RESOLUTION_Y + OFFSET_BOUNDING_BOX) {
				position.y = -OFFSET_BOUNDING_BOX;
			}else if(position.y < -OFFSET_BOUNDING_BOX) {
				position.y =  BaseAnimation.PIXEL_RESOLUTION_Y + OFFSET_BOUNDING_BOX;
			}
		}
	}
}
