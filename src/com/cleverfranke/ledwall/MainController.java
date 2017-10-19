package com.cleverfranke.ledwall;

import com.cleverfranke.ledwall.animation.BarGraphAnimation;
import com.cleverfranke.ledwall.animation.ChestBoardAnimation;
import com.cleverfranke.ledwall.animation.LineGraphAnimation;
import com.cleverfranke.ledwall.animation.Particles;
import com.cleverfranke.ledwall.animation.RainBarsAnimation;
import com.cleverfranke.ledwall.animation.SensorLabAnimation;

import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PImage;

public class MainController extends PApplet {
	
	// Animation manager 
	AnimationManager animationManager;
	
	// Configuration
	private final static String[] SERIAL_PORTS = {"/dev/tty1", "/dev/tty2"};
	
	// Runtime members
	private WallDriver driver;
	
	
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
		Ani.init(this);

		animationManager = new AnimationManager();
		animationManager.queueVisualization(new BarGraphAnimation(true, this));
		// animationManager.queueVisualization(new RainBarsAnimation(true, this));
		animationManager.queueVisualization(new LineGraphAnimation(true, this));
//		animationManager.queueVisualization(new ChestBoardAnimation(true, this));
//		animationManager.queueVisualization(new SensorLabAnimation(true, this));
//		animationManager.queueVisualization(new Particles(true, this));
	}
	
	public void draw() {
		
		// Update animation frame
		animationManager.draw(g);
		
		// Get image from animation
		PImage animationFrame = animationManager.currentVisualization.getImage();
		
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
