package weathermetrics;

import de.looksgood.ani.Ani;
import de.looksgood.ani.easing.CustomEasing;
import de.looksgood.ani.easing.Easing;
import processing.core.PGraphics;
import processing.core.PVector;

// TriggerObjects trigger animations and set 
public class TriggerObject {
	
	public boolean doneUpdating = false;
	public boolean allowAni = true; // <-- Init Animation; this need to be set to true to make sure we can check if the Ani has ended
	
	public float speed = 2; // Default
	
	Ani AniX, AniY; // Ani object for transitioning
	Easing AniEasing; // Make the easing updatable
	
	// Positions to create the trigger vertex; use dist() to control opacity etc. of the LEDs
	public PVector positionA, positionB, positionC, positionD;
	public PVector positionOffset; // <-- Move the whole TriggerObject (vertex)
	public PVector positionOffsetTarget; // <-- Target movement
	public PVector positionOffsetOriginal; // <-- So we can reset the animation
	
	public TriggerObject(PVector _positionA, PVector _positionB, PVector _positionC, PVector _positionD, PVector _positionOffset, Easing _aniEasing) {
		this.positionA = _positionA;
		this.positionB = _positionB;
		this.positionC = _positionC;
		this.positionD = _positionD;
		this.positionOffset = _positionOffset;
		this.positionOffsetTarget = new PVector(0, 0); // <-- Make sure it's a new PVector
		this.positionOffsetOriginal = new PVector(_positionOffset.x, _positionOffset.y);
		this.AniEasing = _aniEasing;
	}
	
	public boolean isInBounds(Pixel _pixel) {
		if((_pixel.position.x >= (positionA.x + positionOffset.x) && _pixel.position.x < (positionB.x + positionOffset.x)) 
		&& (_pixel.position.y >= (positionB.y + positionOffset.y) && _pixel.position.y < (positionD.y + positionOffset.y))) return true;
		return false;
	}
	
	public void updateObjectPosition() {	
		if(!doneUpdating) {
			if(allowAni) {
				allowAni = false;
				AniX = new Ani(this.positionOffset, speed, "x", this.positionOffsetTarget.x, AniEasing);
				AniY = new Ani(this.positionOffset, speed, "y", this.positionOffsetTarget.y, AniEasing);
			}else {
				// Check if ANI is done
				if(AniX.isEnded() && AniY.isEnded()) {
					doneUpdating = true;
					allowAni = true;
				}
			}
		}
	}
	
	public void resetObjectPosition() {
		positionOffset.x = positionOffsetOriginal.x;
		positionOffset.y = positionOffsetOriginal.y;
		doneUpdating = false;
		allowAni = true;
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
