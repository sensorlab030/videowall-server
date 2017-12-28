package com.cleverfranke.ledwall;

import java.awt.Rectangle;

import com.cleverfranke.ledwall.animation.BaseAnimation;
import com.cleverfranke.ledwall.animation.Preview;
import com.cleverfranke.ledwall.ui.MainWindow;
import com.cleverfranke.ledwall.walldriver.WallDriver;
import com.cleverfranke.ledwall.walldriver.WallDriverPort;
import com.cleverfranke.ledwall.walldriver.WallGeometry;

import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.video.Movie;

public class MainController extends PApplet {
	
	private WallDriver driver;
	private Preview preview;
	
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
		new MainWindow();
		
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
		image(preview.renderPreview(animation.getImage()), 0, 0);
		
		// Send image to driver
		driver.displayImage(animation.getImage());
		
	}
	
	public static void main(String[] args) {
		
		// Load settings (either first argument or settings.json by default)
		String settingsFile = args.length > 0 ? args[0] : "settings.json";
		if (!Settings.loadSettings(settingsFile)) {
			System.exit(1);
		}
		
		// Program execution starts here
		PApplet.main(MainController.class.getName());
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

}
