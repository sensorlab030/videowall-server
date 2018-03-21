package nl.sensorlab.videowall.animation;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.walldriver.WallGeometry;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import weathermetrics.Led;

public class WeatherMetrics extends BaseCanvasAnimation {

	private ArrayList<Led> leds;
	
//	PGraphics source;
	
	private int amountColumns = 26;
	private int amountRows = 81;
	private int indexIndicator = 0;
	

	
	public WeatherMetrics(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);
		
		// Create new Arraylist containing the led object
		leds = new ArrayList<Led>();
		
		// Create the leds
		createLEDS();
	}
	
	protected void createLEDS() {

//		// Generate the different LED's
//		for (int x = 0; x < amountColumns; x++) {
//			for (int y = 0; y < amountRows; y++) {
//				
//				
//				leds.add(new Led(this, new PVector(x, y), 1, 1, a)); 
//			}
//		}
//		System.err.println("Amount of leds:"+ leds.size());
	}
	
	protected void update() {
		updateIndicator();
	}
	
	protected void updateIndicator() {
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
	}

}
