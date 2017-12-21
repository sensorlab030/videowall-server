package com.cleverfranke.ledwall;

import java.applet.Applet;

import com.cleverfranke.util.FileSystem;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class MappingTest extends PApplet {
	
	PImage sourceImage;
	PImage bufferImage;
	
	PImage leftImage;
	PImage rightImage;

	@Override
	public void settings() {
		size(640, 480);
	}

	@Override
	public void setup() {
		
		sourceImage = loadImage(FileSystem.getApplicationPath("resources/pixelmap-app-output.png"));
		
		PGraphics gg = createGraphics(81, 26);
		gg.beginDraw();
		gg.background(255, 0, 0);
		gg.translate(81, 0);
		gg.rotate(radians(90));
		gg.image(sourceImage, 0, 0);
		gg.endDraw();
		
		bufferImage = createImage(81, 26, PConstants.RGB);
		bufferImage = gg.get();
		
		// Create left
		leftImage = createImage(81, 16, PConstants.RGB);
		leftImage.copy(bufferImage, 0, 0, 81, 14, 0, 0, 81, 14);
		leftImage.save(FileSystem.getApplicationPath("resources/out-left.png"));
		
		// Create right
		rightImage = createImage(81, 16, PConstants.RGB);
		rightImage.copy(bufferImage, 0, 15, 81, 12, 0, 0, 81, 12);
		rightImage.save(FileSystem.getApplicationPath("resources/out-right.png"));
		
		imageMode(PConstants.CENTER);
		
	}

	@Override
	public void draw() {
		
		translate(width / 2, 140);
		image(sourceImage, 0, 0);
		
		translate(0, 140);
		image(bufferImage, 0, 0);
		
		translate(-120, 120);
		image(leftImage, 0, 0);
		
		translate(240, 0);
		image(rightImage, 0, 0);

		
	}

	public static void main(String[] args) {
		// Program execution starts here
		PApplet.main(MappingTest.class.getName());
	}

}
