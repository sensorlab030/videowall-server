package com.cleverfranke.ledwall;

import com.cleverfranke.ledwall.Settings;
import com.cleverfranke.ledwall.animation.BaseCanvasAnimation;
import com.cleverfranke.ledwall.animation.BasePixelAnimation;
import com.cleverfranke.ledwall.animation.BeachballAnimation;
import com.cleverfranke.ledwall.animation.LineWaveAnimation;
import com.cleverfranke.ledwall.animation.SpectrumAnalyzerAnimation;
import com.cleverfranke.ledwall.animation.VideoAnimation;
import com.cleverfranke.ledwall.walldriver.WallDriver;
import com.cleverfranke.ledwall.walldriver.WallDriverPort;

import de.looksgood.ani.Ani;
import processing.core.PApplet;
import processing.video.Movie;

public class MainController extends PApplet {
	
	WallDriver driver;
	private BasePixelAnimation animation;
	private Preview preview;
	
	@Override
	public void settings() {
//		Rectangle canvasGeometry = WallGeometry.scaleRectangleRounded(WallGeometry.getInstance().getWallGeometry(), CanvasAnimation.SCALE);
//		size(canvasGeometry.width, canvasGeometry.height);
		size(1024, 768);
	}

	@Override
	public void setup() {
		frameRate(WallDriverPort.FRAMERATE);
		
		// Initialize ANI
		Ani.init(this);
		
//		animation = new LineWaveAnimation(this);
//		animation = new BeachballAnimation(this);
//		animation = new VideoAnimation(this);
		animation = new SpectrumAnalyzerAnimation(this);
		
		preview = new Preview(this);
		
		// Configure wall driver
		driver = new WallDriver(this, 
				Settings.getValue("driverPort1"), 
				Settings.getValue("driverPort2"));
	}

	@Override
	public void draw() {
		
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
	
	// Called every time a new frame is available to read
	public void movieEvent(Movie m) {
	  m.read();
	}

}
