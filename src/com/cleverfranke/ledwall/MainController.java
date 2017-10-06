package com.cleverfranke.ledwall;

import com.cleverfranke.ledwall.animation.AgathesAnimation;
import com.cleverfranke.ledwall.animation.Animation;
import com.cleverfranke.ledwall.animation.LineGraphAnimation;

import processing.core.PApplet;
import processing.core.PImage;

public class MainController extends PApplet {
	
	// Configuration
	private final static String[] SERIAL_PORTS = {"/dev/tty1", "/dev/tty2"};
	
	// Runtime members
	private WallDriver driver;
	private Animation currentAnimation;
	
	public void settings() {
		size(WallConfiguration.SOURCE_IMG_WIDTH, WallConfiguration.SOURCE_IMG_HEIGHT);
	}
	
	public void setup() {
		
		// Setup frame rate
		frameRate(30);

		// Add setup code here
		driver = new WallDriver(this);
//		driver.initialize(SERIAL_PORTS);
		
		// Initialize animations
		currentAnimation = new AgathesAnimation(this);
		currentAnimation.prepare();
		
	}
	
	public void draw() {
		
		// Update animation frame
		currentAnimation.draw();
		PImage animationFrame = currentAnimation.getImage(); 
		
		// Create preview
		PImage previewImage = Preview.createPreview(this, animationFrame);

		// Draw preview
		image(previewImage, 0, 0);
		
	}

	public static void main(String[] args) {
		// Program execution starts here
		PApplet.main(MainController.class.getName());
	}

}
