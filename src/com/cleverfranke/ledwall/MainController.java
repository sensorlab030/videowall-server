package com.cleverfranke.ledwall;

import processing.core.PApplet;

public class MainController extends PApplet {
	
	private WallDriver driver;
	private static String[] serialPorts = {"/dev/tty1", "/dev/tty2"};
	
	public void settings() {
		size(1024, 768);
		frameRate(30);
	}
	
	public void setup() {

		// Add setup code here
		driver = new WallDriver(this);
		driver.initialize(serialPorts);
		
	}
	
	public void draw() {
		
	}

	public static void main(String[] args) {
		// Program execution starts here
		PApplet.main(MainController.class.getName());
	}

}
