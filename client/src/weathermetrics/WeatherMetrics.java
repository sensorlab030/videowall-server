package weathermetrics;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseCanvasAnimation;
import nl.sensorlab.videowall.walldriver.WallGeometry;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class WeatherMetrics extends BaseCanvasAnimation {

	private ArrayList<Led> leds;
	
	private int amountColumns = 26;
	private int amountRows = 81;
	private int indexIndicator = 0;
	
//	private int[] xCords = {0,
//			112,
//			248,
//			382,
//			510,
//			638,
//			766,
//			898,
//			1030,
//			1164,
//			1300,
//			1438,
//			1574,
//			1716};
	
//	private WallGeometry wallGeometry = WallGeometry.getInstance();
	
	public WeatherMetrics(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);
		
		// Create new Arraylist containing the led object
		leds = new ArrayList<Led>();
		
		// Create the leds
		generateLEDS();
	}
	
	protected void generateLEDS() {
		// Get the width and height
		float w = (float) applet.width/(amountColumns);
		float h = (float) applet.height/(amountRows);
		// Generate the different LED's
		for (int x = 0; x < amountColumns; x++) {
			for (int y = 0; y < amountRows; y++) {
				float a = applet.map(x * y, 0,  (amountColumns * amountRows), 20, 255);

				leds.add(new Led(this, new PVector(x * w, y * h), (int) w, (int) h, a)); 
			}
		}
		System.err.println("Amount of leds:"+ leds.size());
	}
	
	protected void update() {
		updateIndicator();
	}
	
	protected void updateIndicator() {
			Led led = leds.get(indexIndicator);
			//led.update();
			if(led.doneAnimation) {
				indexIndicator++;
				if(indexIndicator > leds.size()) indexIndicator = 0;
			}
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		update();
		// Draw content
		g.background(0);
		g.noStroke();
		for(Led led : leds) {;
			g.fill(255,255,255, led.alpha);
			g.rect(led.position.x, led.position.y, led.width, led.height);
		}
	}

}
