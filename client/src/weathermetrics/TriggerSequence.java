package weathermetrics;

import java.util.ArrayList;

import processing.core.PGraphics;

public class TriggerSequence {
	
	private ArrayList<TriggerObject> triggerobjects;
	int currentTriggerObjectIndex = 0;
	
	int delayCounter = 0;
	int delayStart = 20; // <-- Delay between end and restart sequence
	int delayEnd = 60; // <-- Delay between end and restart sequence
	
	public boolean doneUpdating = false;
	
	public TriggerSequence() { 
		triggerobjects = new ArrayList<TriggerObject>();
	}
	
	public void addTriggerObject(TriggerObject _triggerObject) {
		triggerobjects.add(_triggerObject);
	}
	
	public void updateSequence() {
		TriggerObject currentTriggerObject = triggerobjects.get(currentTriggerObjectIndex);
		
		// Update the triggerObject
		if(!currentTriggerObject.doneUpdating && delayCounter >= delayStart) { // <-- Delay only relevant for first object
			currentTriggerObject.updateObjectPosition();
		}else {
			delayCounter++;
		}
		
		// Update the index when the current triggerObject is done
		if(currentTriggerObject.doneUpdating) {
			System.err.println("done");
			// Set to done when all triggerObjects are done updating.
			if(currentTriggerObjectIndex == triggerobjects.size()-1) {
				System.err.println("sequence completed");
				doneUpdating = true;
				delayCounter = 0;
			}else {
				currentTriggerObjectIndex++;
			}
		}
	}
	
	public boolean isInBounds(Pixel _pixel) {
		// Check if pixels falls within any of the trigger objects
		for(TriggerObject to : triggerobjects) if(to.isInBounds(_pixel)) return true;
		return false;
	}
	
	public void drawTriggerObjects(PGraphics g) {
		// Draw trigger objects as reference
		for(TriggerObject to : triggerobjects) {
			to.drawTriggerObject(g);
		}
	}
	
	public void resetSequence() {
		if(delayCounter >= delayEnd) {
			for(TriggerObject to : triggerobjects) to.resetObjectPosition(); // <-- Reset all object
			currentTriggerObjectIndex = 0; // <-- Reset index
			delayCounter = 0; // <-- Reset delay counter to be used in start delay
			doneUpdating = false; // <-- Content needs to be updated again
		}else {
			delayCounter++;
		}
	}
	
	public boolean sequenceDone() {
		// Check if sequence is done
		return doneUpdating;
	}
	
	public int getTotalFrameCount() {
		// Return the amount of frames (time) when sequence is done
		return 0;
	}

}
