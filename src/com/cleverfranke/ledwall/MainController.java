package com.cleverfranke.ledwall;

import java.io.File;

import com.cleverfranke.ledwall.animation.BaseAnimation;
import com.cleverfranke.ledwall.animation.BeachballAnimation;
import com.cleverfranke.ledwall.animation.LineWaveAnimation;
import com.cleverfranke.ledwall.animation.Preview;
import com.cleverfranke.ledwall.animation.VideoAnimation;
import com.cleverfranke.ledwall.ui.MainWindow;
import com.cleverfranke.ledwall.walldriver.WallDriver;
import com.cleverfranke.ledwall.walldriver.WallDriverPort;

import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.video.Movie;

public class MainController extends PApplet {
	
	private WallDriver driver;
	private AnimationManager animationManager = AnimationManager.getInstance();
	private Preview preview;
	
	@Override
	public void settings() {
		size(1024, 768);
	}

	@Override
	public void setup() {
		frameRate(WallDriverPort.FRAMERATE);
		
		// Initialize ANI
		Ani.init(this);
		
		// Setup animation manager
		animationManager.addAnimation("Beach ball", new BeachballAnimation(this));
		animationManager.addAnimation("Line wave", new LineWaveAnimation(this));
//		animationManager.addAnimation("Spectrum analyzer", new SpectrumAnalyzerAnimation(this));
		
		// Add videos to animation manager
		VideoAnimation videoAnimation = new VideoAnimation(this);
		for (File f : VideoAnimation.getVideoFileList()) {
			String filename = f.getName();
			filename = filename.substring(0, filename.lastIndexOf('.'));
			animationManager.addAnimation("VID: " + filename, f.getAbsolutePath(), videoAnimation);
		}
		
		// Setup preview
		preview = new Preview(this);
		
		// Create mainWindow
		new MainWindow();
		
		// Configure wall driver
//		driver = new WallDriver(this, 
//				Settings.getValue("driverPort1"), 
//				Settings.getValue("driverPort2"));
	}

	@Override
	public void draw() {
		
		// Fetch current animation
		BaseAnimation animation = animationManager.getCurrentAnimation();
		if (animation == null) {
			return;
		}
		
		// Draw animation frame
		animation.draw();
		
		// Render and display preview
		image(preview.renderPreview(animation.getImage()), 0, 0);
		
		// Send image to driver
//		driver.displayImage(animation.getImage());
		
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
	
	// Called every time a new frame is available to read
	public void movieEvent(Movie m) {
	  m.read();
	}

}
