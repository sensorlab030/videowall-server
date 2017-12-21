package com.cleverfranke.ledwall;

import com.cleverfranke.util.FileSystem;

import processing.core.PApplet;
import processing.core.PImage;

public class MappingTest2 extends PApplet {
	
	WallDriver wd;
	private BasePixelAnimation animation;

	@Override
	public void settings() {
		size(640, 480);
	}

	@Override
	public void setup() {
		frameRate(WallDriverPort.FRAMERATE);
		
		animation = new BeachballPixelAnimation(this);
//		animation = new ImagePixelAnimation(this, FileSystem.getApplicationPath("resources\\testpat-colorbars.png"));
//		animation = new ImagePixelAnimation(this, "testpat-colorbars.png");
		
		wd = new WallDriver(this, "COM5", "COM4");
	}

	@Override
	public void draw() {
		
		animation.draw();
		PImage image = animation.getImage();
		image(image, 0, 0);
		
		wd.displayImage(image);
		
	}

	public static void main(String[] args) {
		// Program execution starts here
		PApplet.main(MappingTest2.class.getName());
	}

}
