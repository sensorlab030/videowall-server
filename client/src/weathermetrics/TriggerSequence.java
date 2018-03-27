package weathermetrics;

public class TriggerSequence {
	
	
	int delayCounter = 0;
	int delayEnd = 60; // <-- Delay between end and restart sequence
	
	public TriggerSequence() { 
		
	}
	
	public boolean sequenceDone() {
		// Check if sequence is done
		return false;
	}
	
	public int getTotalFrameCount() {
		// Return the amount of frames (time) when sequence is done
		return 0;
	}

}
