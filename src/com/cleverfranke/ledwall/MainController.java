package com.cleverfranke.ledwall;

import com.cleverfranke.ledwall.animation.BarFlowAnimation;
import com.cleverfranke.ledwall.animation.BarGraphAnimation;
import com.cleverfranke.ledwall.animation.ChestBoardAnimation;
import com.cleverfranke.ledwall.animation.LineFlowAnimation;
import com.cleverfranke.ledwall.animation.LineGraphAnimation;
import com.cleverfranke.ledwall.animation.VideoStream;
//import com.cleverfranke.ledwall.animation.CaptureStream;


import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class MainController extends PApplet {

	// Animation manager
	AnimationManager animationManager;

	// Graphics
	PGraphics g;

	// Configuration
	private final static String[] SERIAL_PORTS = {"/dev/tty1", "/dev/tty2"};

	// Runtime members
	private WallDriver driver;


	@Override
	public void settings() {
		size(WallConfiguration.SOURCE_IMG_WIDTH, WallConfiguration.SOURCE_IMG_HEIGHT);
	}

	@Override
	public void setup() {
		// Setup frame rate
		frameRate(30);

		// Add setup code here
		driver = new WallDriver(this);
		// driver.initialize(SERIAL_PORTS);

		// Initialize animations
		Ani.init(this);

		// Initialize graphics context
		g = this.createGraphics(WallConfiguration.SOURCE_IMG_WIDTH, WallConfiguration.SOURCE_IMG_HEIGHT);
		g.noSmooth();

		// Initialize animations
		animationManager = new AnimationManager();
     	animationManager.queueVisualization(new BarFlowAnimation(true, this));
		animationManager.queueVisualization(new LineGraphAnimation(true, this));
		animationManager.queueVisualization(new BarGraphAnimation(true, this));
		animationManager.queueVisualization(new ChestBoardAnimation(true, this));
		animationManager.queueVisualization(new LineFlowAnimation(true, this));
//		animationManager.queueVisualization(new VideoStream(true, this));
//		animationManager.queueVisualization(new CaptureStream(true, this));
	}

	@Override
	public void draw() {
		// Update animation frame
		animationManager.draw(g);

		// Get image from animation
		PImage animationFrame = animationManager.currentVisualization.getImage();

		// If the animationFrame does have the expected dimension of the preview input, create preview, else just draw the animation frame
		if (animationFrame.height == WallConfiguration.ROWS_COUNT && animationFrame.width == WallConfiguration.COLUMNS_COUNT) {
			PImage previewImage = Preview.createPreview(g, animationFrame);
			image(previewImage, 0, 0);
		} else {
			image(animationFrame, 0, 0);
		}
	}

	public static void main(String[] args) {
		// Program execution starts here
		PApplet.main(MainController.class.getName());
	}

}
