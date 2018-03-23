package weathermetrics;

import processing.core.PGraphics;
import processing.core.PVector;

// TriggerObjects trigger animations and set 
public class TriggerObject {
	
	// Snapping
	public int columns; // <-- X
	public int rows; // <-- Y
	
	public boolean doneUpdating = false;
	
	public float speed;
	public int direction = 1;
	
	// Positions to create the trigger vertex; use dist() to control opacity etc. of the LEDs
	public PVector positionA, positionB, positionC, positionD;
	public PVector positionOffset; // <-- Move the whole TriggerObject (vertex)
	
	public TriggerObject(PVector _positionA, PVector _positionB, PVector _positionC, PVector _positionD, PVector _positionOffset) {
		this.positionA = _positionA;
		this.positionB = _positionB;
		this.positionC = _positionC;
		this.positionD = _positionD;
		this.positionOffset = _positionOffset;
	}
	
	public boolean isInBounds(LED _led) {
		if((_led.positionA.x > (positionA.x + positionOffset.x) && _led.positionA.x < (positionB.x + positionOffset.x)) && (_led.positionB.x > (positionA.x + positionOffset.x) && _led.positionB.x < (positionB.x  + positionOffset.x))) return true;
		return false;
	}
	
	public void updatePosition(float _targetX) {
		positionOffset.x += (speed * direction);
		if(positionOffset.x >= _targetX) {
			positionOffset.x = _targetX;
			doneUpdating = true;
		}
	}
	
	public void drawTriggerObject(PGraphics g) {
		g.fill(255, 0 , 0);
		g.noStroke();
		g.beginShape();
		g.vertex(positionA.x + positionOffset.x, positionA.y + positionOffset.y);
		g.vertex(positionB.x + positionOffset.x, positionB.y + positionOffset.y);
		g.vertex(positionC.x + positionOffset.x, positionC.y + positionOffset.y);
		g.vertex(positionD.x + positionOffset.x, positionD.y + positionOffset.y);
		g.endShape();
	}
}
