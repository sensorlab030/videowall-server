package weathermetrics;

import processing.core.PVector;

// TriggerObjects trigger animations and set 
public class TriggerObject {
	
	// Snapping
	public int columns; // <-- X
	public int rows; // <-- Y
	
	// Positions to create the trigger vertex; use dist() to control opacity etc. of the LEDs
	public PVector positionA, positionB, positionC, positionD;
	public PVector positionOffset; // <-- Move the whole TriggerObject (vertex)
	
	public TriggerObject(PVector _positionA, PVector _positionB, PVector _positionC, PVector _positionD) {
		this.positionA = _positionA;
		this.positionB = _positionB;
		this.positionC = _positionC;
		this.positionD = _positionD;
	}
	
	public boolean isInBounds(LED _l) {
		if((_l.positionA.x > positionA.x && _l.positionA.x < positionB.x) && (_l.positionB.x > positionA.x && _l.positionB.x < positionB.x)) return true;
		return false;
	}
	
	public void update() {
	}
}
