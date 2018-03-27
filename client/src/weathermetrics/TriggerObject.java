package weathermetrics;

import processing.core.PGraphics;
import processing.core.PVector;

// TriggerObjects trigger animations and set 
public class TriggerObject {
	
	public boolean doneUpdating = false;
	
	public float speed = 0.25f; // Default
	public int direction = 1;
	
	// Positions to create the trigger vertex; use dist() to control opacity etc. of the LEDs
	public PVector positionA, positionB, positionC, positionD;
	public PVector positionOffset; // <-- Move the whole TriggerObject (vertex)
	public PVector positionOffsetTarget; // <-- Target movement
	public PVector positionOffsetOriginal; // <-- So we can reset the animation
	
	public TriggerObject(PVector _positionA, PVector _positionB, PVector _positionC, PVector _positionD, PVector _positionOffset, int _direction) {
		this.positionA = _positionA;
		this.positionB = _positionB;
		this.positionC = _positionC;
		this.positionD = _positionD;
		this.positionOffset = _positionOffset;
		this.positionOffsetTarget = new PVector(0, 0); // <-- Make sure it's a new PVector
		this.positionOffsetOriginal = new PVector(_positionOffset.x, _positionOffset.y);
		this.direction = _direction;
	}
	
	public boolean isInBounds(Pixel _pixel) {
		if((_pixel.position.x >= (positionA.x + positionOffset.x) && _pixel.position.x < (positionB.x + positionOffset.x)) 
		&& (_pixel.position.y >= (positionB.y + positionOffset.y) && _pixel.position.y < (positionD.y + positionOffset.y)))  return true;
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
		positionOffset.x = positionOffsetOriginal.x;
		positionOffset.y = positionOffsetOriginal.y;
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
