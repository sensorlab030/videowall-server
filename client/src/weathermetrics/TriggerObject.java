package weathermetrics;

import processing.core.PGraphics;
import processing.core.PVector;

// TriggerObjects trigger animations and set 
public class TriggerObject {
	
	// Snapping
	public int columns; // <-- X
	public int rows; // <-- Y
	
	public boolean doneUpdating = false;
	
	public float speed = 4; // Default
	public int direction = 1;
	
	// Positions to create the trigger vertex; use dist() to control opacity etc. of the LEDs
	public PVector positionA, positionB, positionC, positionD;
	public PVector positionOffset; // <-- Move the whole TriggerObject (vertex)
	public PVector positionOffsetTarget; // <-- Target movement
	public PVector positionOffsetOriginal; // <-- So we can reset the animation
	
	public TriggerObject(PVector _positionA, PVector _positionB, PVector _positionC, PVector _positionD, PVector _positionOffset) {
		this.positionA = _positionA;
		this.positionB = _positionB;
		this.positionC = _positionC;
		this.positionD = _positionD;
		this.positionOffset = _positionOffset;
		this.positionOffsetTarget = new PVector(0, 0); // <-- Make sure it's a new PVector
		this.positionOffsetOriginal = new PVector(_positionOffset.x, _positionOffset.y);
	}
	
	public boolean isInBounds(LED _led) {
		if((_led.positionA.x > (positionA.x + positionOffset.x) && _led.positionA.x < (positionB.x + positionOffset.x)) 
				&& (_led.positionB.x > (positionA.x + positionOffset.x) && _led.positionB.x < (positionB.x  + positionOffset.x))
				&& (_led.positionA.y > (positionB.y + positionOffset.y) && _led.positionA.y < (positionD.y + positionOffset.y)) 
				&& (_led.positionB.y > (positionB.y + positionOffset.y) && _led.positionB.y < (positionD.y  + positionOffset.y))) return true;
		return false;
	}
	
	public void updateObjectPosition() {
		if(PVector.dist(positionOffset, positionOffsetTarget) == 0) {
			doneUpdating = true;
		}else {
			if(PVector.dist(new PVector(positionOffset.x, (float) 0), new PVector(positionOffsetTarget.x, (float) 0)) != 0) positionOffset.x += (speed * direction);
			if(PVector.dist(new PVector((float) 0, positionOffset.y), new PVector((float) 0, positionOffsetTarget.y)) != 0) positionOffset.y += (speed * direction);
		}
	}
	
	public void toggleDirection() {
		direction = direction == -1 ? 1 : -1;
	}
	
	public void resetObjectPosition() {
		positionOffset = new PVector(positionOffsetOriginal.x, positionOffsetOriginal.y);
		doneUpdating = false;
	}
	
	public void drawTriggerObject(PGraphics g) {
		g.pushStyle();
		g.fill(255);
		g.noStroke();
		g.beginShape();
		g.vertex(positionA.x + positionOffset.x, positionA.y + positionOffset.y);
		g.vertex(positionB.x + positionOffset.x, positionB.y + positionOffset.y);
		g.vertex(positionC.x + positionOffset.x, positionC.y + positionOffset.y);
		g.vertex(positionD.x + positionOffset.x, positionD.y + positionOffset.y);
		g.endShape();
		g.popStyle();
	}
}
