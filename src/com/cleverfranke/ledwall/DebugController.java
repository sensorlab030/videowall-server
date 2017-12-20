package com.cleverfranke.ledwall;

import com.cleverfranke.util.FileSystem;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class DebugController extends PApplet {

	// Configuration
	private final static String[] SERIAL_PORTS = {"COM5", "COM4"};

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
		WallDriver.printPortList();
		// Setup LED driver
		driver = new WallDriver(this);
		driver.initialize(SERIAL_PORTS);

		// First anim
		animation = new BeachballPixelAnimation(this);
//		animation = new LineAnimation(this);
//		animation = new ImagePixelAnimation(this, FileSystem.getApplicationPath("resources/testpat-pixelmap.png"));
//		animation = new ImagePixelAnimation(this, FileSystem.getApplicationPath("resources/testpat-colorbars.png"));
		
	}

	@Override
	public void draw() {
		
		// Animation change
//		if (frameCount == 4 * 60) {
//			animation = new BeachballPixelAnimation(this);
//		} else if (frameCount == 10 * 60) {
//			
//		} else if (frameCount == 20 * 60) {
//			animation = new LineAnimation(this);
//		}
		
		// Update animation, fetch image
		animation.draw();
		PImage image = animation.getImage();
		
		// Send to LED driver
		if (image != null) {
			driver.displayImage(image);
		}
		
		// Display as preview
		image(image, 0, 0);
		
//		System.out.println(frameRate);
		
	}

	public static void main(String[] args) {
		// Program execution starts here
		PApplet.main(DebugController.class.getName());
	}

}
