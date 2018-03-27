package weathermetrics;

import java.util.ArrayList;

import processing.core.PGraphics;

public class TriggerSequence {
	
	private ArrayList<TriggerObject> triggerobjects;
	int currentTriggerObjectIndex = 0;
	
	int delayCounter = 0;
	int delayEnd = 60; // <-- Delay between end and restart sequence

	
	public TriggerSequence() { 
		triggerobjects = new ArrayList<TriggerObject>();
	}
	
	public void addTriggerObject(TriggerObject _triggerObject) {
		triggerobjects.add(_triggerObject);
	}
	
	public void update() {
		TriggerObject currentTriggerObject = triggerobjects.get(currentTriggerObjectIndex);
		currentTriggerObject.updateObjectPosition();
		if(currentTriggerObject.doneUpdating) {
			System.err.println("done");
			// Reset all
			if(currentTriggerObjectIndex == triggerobjects.size()-1) {
				if(delayCounter >= delayEnd)	{
					System.err.println("reset sequence");
					resetSequence();
					delayCounter = 0;
				}
				delayCounter++;
			}else {
				currentTriggerObjectIndex++;
			}
		}
	}
	
	public void drawTriggerObjects(PGraphics g) {
		for(TriggerObject to : triggerobjects) {
			to.drawTriggerObject(g);
		}
	}
	
	public void resetSequence() {
		for(TriggerObject to : triggerobjects) {
			to.resetObjectPosition();
		}
	}
	
//	test.updateObjectPosition();
//	if(test.doneUpdating) {
//		applet.println("done");
//		// Restart
//		test.resetObjectPosition();
//	}
//	
	
	public boolean sequenceDone() {
		// Check if sequence is done
		return false;
	}
	
	public int getTotalFrameCount() {
		// Return the amount of frames (time) when sequence is done
		return 0;
	}

}
