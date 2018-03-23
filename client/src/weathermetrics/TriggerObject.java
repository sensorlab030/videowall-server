package weathermetrics;

import processing.core.PVector;

// TriggerObjects trigger animations and set 
public class TriggerObject {
	
	// Snapping
	public int columns; // <-- X
	public int rows; // <-- Y
	
	// Positions to create the trigger vertex; use dist() to control opacity etc. of the LEDs
	PVector pA, pB, pD, pC;
	
	public TriggerObject() {
		
	}
	
	public void checkLED(LED _led) {
		// Check if the LED is within bounds; if so add to related index.
		
	}
	
	public void update() {
	}
}
