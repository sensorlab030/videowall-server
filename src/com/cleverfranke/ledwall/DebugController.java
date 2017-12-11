package com.cleverfranke.ledwall;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class DebugController extends PApplet {

	// Configuration
	private final static String[] SERIAL_PORTS = {"/dev/tty1", "/dev/tty2"};

	// Runtime members
	private WallDriver driver;
	private BasePixelAnimation animation;
	

	@Override
	public void settings() {
		size(BasePixelAnimation.PIXEL_RESOLUTION_X, BasePixelAnimation.PIXEL_RESOLUTION_Y);
	}

	@Override
	public void setup() {
		// Setup frame rate
		frameRate(60);
		
		// Setup LED driver
		driver = new WallDriver(this);
		// driver.initialize(SERIAL_PORTS);
		
		// Setup animation
//		animation = new ImagePixelAnimation(this, "testPattern.png");
		animation = new BeachballPixelAnimation(this);

	}

	@Override
	public void draw() {
		
		// Update animation, fetch image
		animation.draw();
		PImage image = animation.getImage();
		
		// Send to LED driver
		driver.displayImage(image);
		
		// Display as preview
		image(image, 0, 0);
		
		System.out.println(frameRate);
		
	}

	public static void main(String[] args) {
		// Program execution starts here
		PApplet.main(DebugController.class.getName());
	}

}
