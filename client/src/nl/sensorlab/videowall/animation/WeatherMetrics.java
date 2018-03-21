package nl.sensorlab.videowall.animation;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.walldriver.WallGeometry;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import weathermetrics.LED;

public class WeatherMetrics extends BaseCanvasAnimation {

	private ArrayList<LED> leds;

	// Remember led indexes 
	private List<Integer> referenceListIndicators = new ArrayList<Integer>();  // <-- LED's that function as indicators
	private List<Integer> referenceListCanvas = new ArrayList<Integer>(); // <-- LED's that function for the canvas

	// Indicator updater
	private int currentIndicatorIndex = 0;
	private int currentAmountAnimated = 0;
	private boolean sequenceComplete = false;

	public WeatherMetrics(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);

		// Create new Arraylist containing the LED object
		leds = new ArrayList<LED>();

		// Generate the LEDs
		generateLEDs();
	}

	protected void generateLEDs() {
		// Create the LED instances using the WallGeometry	 
		int amountLeds = WallGeometry.getInstance().getPixelCount();
		// Determine the offset for the LED's (x - w = towards left; else towards right)
		int width = (int) Math.round(15f * DEFAULT_SCALE);

		// Loop over the pixels
		for(int i = 0; i < amountLeds; i++) {
			// Get the pixel info
			Point p =  WallGeometry.scalePointRounded(WallGeometry.getInstance().getPixelCoordinates(i), DEFAULT_SCALE);

			// Determine the X offset; either left or right
			float xOffset = ((i % 2 == 0) ? width : -width);

			// Some random settings
			int color = PColor.color(255, 0, 0);
			float a = applet.random(20, 200);

			// Add the new instance to the list
			leds.add(new LED(this, i, new PVector(p.x, p.y), new PVector(p.x + xOffset, p.y), color, a));

			// Add the index of the LED to the specific list
			if(p.y <= 2 || p.y >= 119 || p.x <= 3 || p.x >= 428) {
				referenceListIndicators.add(i);
				// Set to white
				leds.get(i).color = PColor.color(255);
				leds.get(i).alpha = 0;
			}else {
				referenceListCanvas.add(i);
				// Set random alpha
				leds.get(i).alpha = applet.random(0, 255);
				leds.get(i).speed = (float) applet.random(10, 50);
			}
		}

	}

	protected void update() {
		updateIndicator();		
		for(int i = 0; i < referenceListCanvas.size(); i++) {
			LED led = leds.get(referenceListCanvas.get(i));
			led.update();
//			led.alpha = applet.random(20, 150);
			if(led.doneAnimating) {
				led.doneAnimating = false;
				led.direction = led.direction == -1 ? 1 : -1;
			}
			
		}
	}

	protected void updateIndicator() {
		// Update individually
		if(!sequenceComplete) {
			// Get the index of the indicator that needs to be updated
			int index = referenceListIndicators.get(currentIndicatorIndex);

			// Get the LED that needs to be updated
			LED currentLed = leds.get(index);

			// Update the current LED
			currentLed.speed = 200; // <-- Increase speed when individual
			currentLed.update();

			// Update the index when the current LED is done animating
			if(currentLed.doneAnimating) {
				
				currentLed.doneAnimating = false; // <-- Reset so we can re-activate animation
				
				currentIndicatorIndex++; // Update the index so the next led will light up
					
				// when all animations have been done fade all back to black and restart.
				if(currentIndicatorIndex >= referenceListIndicators.size()) {
					sequenceComplete = true;
				}
			}

		// Update all indicators
		}else if (sequenceComplete) {

			for(int i = 0; i < referenceListIndicators.size(); i++){
				LED currentLed = leds.get(referenceListIndicators.get(i));
				
				currentLed.speed = 5; // <-- Set slow speed
				currentLed.direction = -1; // Set to fadeOut
				currentLed.update();
			
				if(currentLed.doneAnimating) {
					currentLed.direction = 1; // <-- Reset direction to FadeIn
					currentLed.doneAnimating = false; // <-- Reset LED
					
					currentIndicatorIndex--;
					
					if(currentIndicatorIndex < 0) {
						currentIndicatorIndex = 0;
						sequenceComplete = false;
					}
				}
			}
		}

	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		g.background(0);
		// Update the leds
		update();
		// Just simple update the LEDS; all changes, e.g. alpha and colors are being updated within the update function (depending on which sequence is selected)
		g.noFill();
		for(LED led:leds) {
			g.stroke(led.color, led.alpha);
			g.line(led.positionA.x, led.positionA.y, led.positionB.x, led.positionB.y);
		}
	}

}
