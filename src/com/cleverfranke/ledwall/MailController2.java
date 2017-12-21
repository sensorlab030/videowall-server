package com.cleverfranke.ledwall;

import com.cleverfranke.ledwall.animation.BeachballCanvasAnimation;
import com.cleverfranke.ledwall.animation.CanvasAnimation;
import com.cleverfranke.ledwall.animation.LineCanvasAnimation;
import com.cleverfranke.ledwall.walldriver.WallDriver;
import com.cleverfranke.ledwall.walldriver.WallDriverPort;
import com.cleverfranke.ledwall.walldriver.WallGeometry;

import processing.core.PApplet;
import processing.core.PImage;

public class MailController2 extends PApplet {
	
	WallDriver driver;
	private CanvasAnimation animation;

	@Override
	public void settings() {
//		Rectangle canvasGeometry = WallGeometry.scaleRectangleRounded(WallGeometry.getInstance().getWallGeometry(), CanvasAnimation.SCALE);
//		size(canvasGeometry.width, canvasGeometry.height);
		size(1024, 768);
	}

	@Override
	public void setup() {
		frameRate(WallDriverPort.FRAMERATE);
		
//		animation = new BeachballPixelAnimation(this);
//		animation = new ImagePixelAnimation(this, FileSystem.getApplicationPath("resources\\testpat-colorbars.png"));
//		animation = new ImagePixelAnimation(this, "testpat-colorbars.png");
//		animation = new CanvasAnimation(this);
		animation = new LineCanvasAnimation(this);
//		animation = new BeachballCanvasAnimation(this);
		
		// Configure wall driver
		driver = new WallDriver(this, "COM5", "COM4");
	}

	@Override
	public void draw() {
		
		animation.draw();
		PImage canvasImage = animation.getCanvasImage();
		image(canvasImage, 0, 0);
		
		PImage pixelImage = animation.getImage();
		image(pixelImage, 0, 400);
		
		// Send image to driver
		driver.displayImage(pixelImage);
		
	}

	public static void main(String[] args) {
		// Program execution starts here
		PApplet.main(MailController2.class.getName());
	}

}
