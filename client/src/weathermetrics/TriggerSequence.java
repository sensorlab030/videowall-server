package weathermetrics;

import java.util.ArrayList;

import processing.core.PGraphics;

public class TriggerSequence {
	
	public ArrayList<TriggerObject> triggerobjects;
	int currentTriggerObjectIndex = 0;
	
	int delayCounter = 0;
	int delayStart = 0; // <-- Delay between end and restart sequence
	int delayEnd = 0; // <-- Delay between end and restart sequence
	
	public boolean doneUpdating = false;
	
	public TriggerSequence(int _delayStart, int _delayEnd) { 
		this.delayStart = _delayStart;
		this.delayEnd = _delayEnd;
		this.triggerobjects = new ArrayList<TriggerObject>();
	}
	
	public void addTriggerObject(TriggerObject _triggerObject) {
		triggerobjects.add(_triggerObject);
	}
	
	// Sequencing
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
			System.err.println((currentTriggerObjectIndex + 1) + " of " + (triggerobjects.size()) + " done");
			// Set to done when all triggerObjects are done updating.
			if(currentTriggerObjectIndex == triggerobjects.size()-1) {
				System.err.println("updateSequence() sequence completed");
				doneUpdating = true;
				delayCounter = 0;
			}else {
				currentTriggerObjectIndex++;
			}
		}
	}
	
	// Animate all
	public void updateAll() {
		for(TriggerObject to : triggerobjects) { 
			if(!to.doneUpdating) {
				to.updateObjectPosition();
			}else {
				if(currentTriggerObjectIndex == triggerobjects.size()-1) {
					System.err.println("updateAll() sequence completed");
					doneUpdating = true;
					delayCounter = 0;
				}else {
					currentTriggerObjectIndex++;
				}
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
	
	public void reverseSequence() {
		if(delayCounter >= delayEnd) {
			System.err.println("sequence reset");
			for(TriggerObject to : triggerobjects) to.reverseObjectPosition(); // <-- Reset all objects
			currentTriggerObjectIndex = 0; // <-- Reset index
			delayCounter = 0; // <-- Reset delay counter to be used in start delay
			doneUpdating = false; // <-- Content needs to be updated again
		}else {
			delayCounter++;
		}
	}
	
	public void resetSequence() {
		if(delayCounter >= delayEnd) {
			System.err.println("sequence reset");
			for(TriggerObject to : triggerobjects) to.resetObjectPosition(); // <-- Reset all objects
			currentTriggerObjectIndex = 0; // <-- Reset index
			delayCounter = 0; // <-- Reset delay counter to be used in start delay
			doneUpdating = false; // <-- Content needs to be updated again
		}else {
			delayCounter++;
		}
	}
}
