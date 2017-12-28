package com.cleverfranke.ledwall;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import com.cleverfranke.ledwall.animation.BaseAnimation;
import com.cleverfranke.ledwall.animation.Preview;
import com.cleverfranke.ledwall.ui.MainWindow;
import com.cleverfranke.ledwall.walldriver.WallDriver;
import com.cleverfranke.ledwall.walldriver.WallDriverPort;
import com.cleverfranke.ledwall.walldriver.WallGeometry;
import com.cleverfranke.util.Settings;

import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.video.Movie;

public class LedWallApplication extends PApplet {
	
	private WallDriver driver;
	private Preview preview;
	private MainWindow mainWindow;
	
	private boolean previewEnabled = true;
	private boolean blackOutEnabled;
	
	@Override
	public void settings() {
		Rectangle previewRect = WallGeometry.scaleRectangleRounded(WallGeometry.getInstance().getWallGeometry(), Preview.SCALE);
		size(previewRect.width, previewRect.height);
	}

	@Override
	public void setup() {
		frameRate(WallDriverPort.FRAMERATE);
		surface.setTitle("Preview");
		
		// Initialize animation manager
		AnimationManager.initialize(this);
		
		// Setup preview
		preview = new Preview(this);
		
		// Create mainWindow
		mainWindow = new MainWindow(this);
		
		// Configure wall driver
		driver = new WallDriver(this, 
				Settings.getValue("driverPort1"), 
				Settings.getValue("driverPort2"));
		
		// Initialize ANI
		Ani.init(this);
		
	}

	@Override
	public void draw() {
		
		// Fetch current animation
		BaseAnimation animation =  AnimationManager.getInstance().getCurrentAnimation();
		if (animation == null) {
			background(0);
			return;
		}
		
		// Draw animation frame
		animation.draw();
		
		// Render and display preview
		if (previewEnabled) {
			image(preview.renderPreview(animation.getImage()), 0, 0);
		} else {
			background(0);
		}
		
		// Send image to driver
		driver.displayImage(animation.getImage());
		
	}
	
	/**
	 * Handle movie events (used by VideoAnimation class), read
	 * a new frame from the movie
	 * 
	 * @param m
	 */
	public void movieEvent(Movie m) {
		m.read();
	}
	
	/**
	 * Handle key presses
	 */
	public void keyPressed() {
		switch (keyCode) {
			case KeyEvent.VK_F1:
				mainWindow.toggle();
				break;
			case KeyEvent.VK_F2:
				previewEnabled = !previewEnabled;
				System.out.println("Preview enabled: " + previewEnabled);
				break;
			case KeyEvent.VK_SPACE:
				blackOutEnabled = !blackOutEnabled;
				driver.setBlackOutEnabled(blackOutEnabled);
				System.out.println("Black out enabled: " + blackOutEnabled);
				break;
		}

	}
	
	/**
	 * Set wall brightness 
	 * 
	 * @param brightness [0, 255] (0 = off, 255 is brightest possible)
	 */
	public void setWallBrightness(int brightness) {
		driver.setBrightness(brightness);
	}
	
	public static void main(String[] args) {
		
		// Load settings (either first argument or settings.json by default)
		String settingsFile = args.length > 0 ? args[0] : "settings.json";
		if (!Settings.loadSettings(settingsFile)) {
			System.exit(1);
		}
		
		// Program execution starts here
		PApplet.main(LedWallApplication.class.getName());
	}

}
