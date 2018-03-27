package nl.sensorlab.videowall.animation;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import com.cleverfranke.util.PColor;
import nl.sensorlab.videowall.walldriver.WallGeometry;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import weathermetrics.LED;
import weathermetrics.TriggerObject;

public class WeatherMetrics extends BaseCanvasAnimation {

	private ArrayList<LED> leds;

	// Remember led indexes 
	private List<Integer> referenceListIndicators = new ArrayList<Integer>();  // <-- LED's that function as indicators
	private List<Integer> referenceListCanvas = new ArrayList<Integer>(); // <-- LED's that function as the canvas

	
	// Demo
	private TriggerObject testobjectTop, testobjectRight, testobjectBottom, testobjectLeft, testobjectMove;
	
	// Indicator updater
	private int currentIndicatorIndex = 0;
	private boolean sequenceComplete = false;
	
	private int patrickcounter = 0;

	public WeatherMetrics(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);

		// Create new Arraylist containing the LED object
		leds = new ArrayList<LED>();
		float wCancas = 440;
		float hCanvas = 120;
		float h = 10;
		float w = 25;
		
		testobjectTop = new TriggerObject(new PVector(0,0), new PVector(wCancas, 0), new PVector(wCancas,h), new PVector(0,h), new PVector(-wCancas, 0));
		
		testobjectRight = new TriggerObject(new PVector(wCancas - w,  0), new PVector(wCancas, 0), new PVector(wCancas, hCanvas), new PVector(wCancas - w, hCanvas), new PVector(0, -hCanvas));
		testobjectRight.speed = 2;

		testobjectLeft = new TriggerObject(new PVector(0,  0), new PVector(w, 0), new PVector(w, hCanvas), new PVector(0, hCanvas), new PVector(0, hCanvas));
		testobjectLeft.toggleDirection();
		testobjectLeft.speed = 2;
		
		testobjectBottom = new TriggerObject(new PVector(0, hCanvas - h), new PVector(wCancas, hCanvas - h), new PVector(wCancas,hCanvas), new PVector(0,hCanvas), new PVector(wCancas, 0));
		testobjectBottom.toggleDirection();
		
		w = 50;
		testobjectMove =  new TriggerObject(new PVector(wCancas - w,  0), new PVector(wCancas, 0), new PVector(wCancas, hCanvas), new PVector(wCancas - w, hCanvas), new PVector(-wCancas, 0));
		testobjectMove.speed = 2;
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
			int color = PColor.color(0,191,255);
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
				leds.get(i).alpha = 0; //applet.random(0, 255);
				//leds.get(i).speed = (float) applet.random(10, 30);
			}
	
			applet.println("x" +p.x + "y" + p.y + "" + DEFAULT_SCALE);
		}
//		applet.exit();

	}

	protected void update() {
		updateIndicator();		
		for(int i = 0; i < referenceListCanvas.size(); i++) {
			LED led = leds.get(referenceListCanvas.get(i));
			led.update();
			if(led.doneUpdating) {
				led.doneUpdating = false;
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
			if(currentLed.doneUpdating) {
				
				currentLed.doneUpdating = false; // <-- Reset so we can re-activate animation
				
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
			
				if(currentLed.doneUpdating) {
					currentLed.direction = 1; // <-- Reset direction to FadeIn
					currentLed.doneUpdating = false; // <-- Reset LED
					
					currentIndicatorIndex--;
					
					if(currentIndicatorIndex < 0) {
						currentIndicatorIndex = 0;
						sequenceComplete = false;
					}
				}
			}
		}

	}
	
	public void updateObject() {
//		int speed = 10;
//		testobject.positionB.x += speed;
//		testobject.positionC.x +=speed;
//	
//		// Update A and D to create a fade out effect
//		if(testobject.positionB.x > applet.width/5 || testobject.positionC.x > applet.width/5) {
//			testobject.positionA.x+=speed;
//			testobject.positionD.x+=speed;
//		}
//	
//		// Reset
//		if(testobject.positionB.x > applet.width || testobject.positionC.x > applet.width) {
//			testobject.positionB.x = 0;
//			testobject.positionC.x = 0;
//			testobject.positionA.x = 0;
//			testobject.positionD.x = 0;
//		}
	}
	
	public void drawObject(PGraphics g) {
//		testobject.drawTriggerObject(g);
//		testobjectTop.updateObjectPosition();
		
//		testobjectMove.drawTriggerObject(g);
		
		
//		testobjectLeft.updateObjectPosition();
		
//		
//		
//		testobjectTop.drawTriggerObject(g);
//		testobjectBottom.drawTriggerObject(g);
//		testobjectRight.drawTriggerObject(g);
//		testobjectLeft.drawTriggerObject(g);
//		
//		if(testobjectTop.doneUpdating) {
//			testobjectBottom.updateObjectPosition();
////			applet.println("doneUpdating");
////			testobjectTop.doneUpdating = false;
////			testobjectTop.positionOffsetTarget.x = testobjectTop.positionOffsetTarget.x == 0 ? -440 : 0;
////			testobjectTop.toggleDirection();
//		}
//		
		if(!testobjectTop.doneUpdating) testobjectTop.updateObjectPosition();
		if(testobjectTop.doneUpdating) {
			testobjectRight.updateObjectPosition();
			if(testobjectRight.doneUpdating) {
				testobjectBottom.updateObjectPosition();
				if(testobjectBottom.doneUpdating) {
					testobjectLeft.updateObjectPosition();
					
					if(testobjectLeft.doneUpdating) {
						
						patrickcounter++;
						if(patrickcounter >= 60) {
						// Reset
						testobjectTop.resetObjectPosition();
						testobjectRight.resetObjectPosition();
						testobjectBottom.resetObjectPosition();
						testobjectLeft.resetObjectPosition();
						patrickcounter = 0;
						}
					}
				}
			}
		}
		
		
		testobjectMove.updateObjectPosition();
		if(testobjectMove.doneUpdating) {
			
			testobjectMove.resetObjectPosition();
		}

		// Check if LED is within bounds; if so fadeIn
		for(LED led:leds) {
			if(testobjectTop.isInBounds(led) || testobjectRight.isInBounds(led) ||testobjectBottom.isInBounds(led) ||testobjectLeft.isInBounds(led) ) {
				led.fadeIn(10);
				led.color = PColor.color(255,0,0);
			}else {
				if(testobjectMove.isInBounds(led)) {
//					applet.println("doneUpdating");	
					led.fadeIn(10);
//					led.color = PColor.color(255,0,255);
				}else {
					led.fadeOut(10);
//					led.color = PColor.color(255,255,0);
				}
			}
			led.update();
		}

	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		g.background(0);
		// Update the leds
		//update();
		
		drawObject(g);
		
		// Just simple draw the LEDS; all changes, e.g. alpha and colors are being updated within the update function (depending on which sequence is selected)
		g.noFill();
		for(LED led:leds) {
			g.stroke(led.color, led.alpha);
			g.line(led.positionA.x, led.positionA.y, led.positionB.x, led.positionB.y);
		}
	}
}
