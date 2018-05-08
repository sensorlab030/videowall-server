package nl.sensorlab.videowall.animation.baseanimations.horizontalwaves;

import processing.core.PGraphics;

public class HorizontalWave {
	
	HorizontalWavesAnimation parent;

	private float offsetIntensity = 0; // How much can the line deviate vertically
	private float offsetIntensityIncrements;
	private float offsetSpeed = 0; // How quickly the wave moves vertically
	private float offsetSpeedIncrements;

	private int color;

	public HorizontalWave(HorizontalWavesAnimation parent, float offsetIntensityIncrements, float offsetSpeedIncrements, int color) {
		this.parent = parent;
		this.offsetIntensityIncrements = offsetIntensityIncrements;
		this.offsetSpeedIncrements = offsetSpeedIncrements;
		this.color = color;
	}

	public void draw(PGraphics g) {
		// Reset intensity to keep within bounds
		offsetIntensity = 0;
		
		g.noFill();
		g.stroke(color);
		g.strokeWeight(1);
		g.beginShape();
		
		for(int x = 0; x <= parent.PIXEL_RESOLUTION_X; x++) {
			float noise = parent.parent.noise(offsetIntensity, offsetSpeed);
			float y = parent.parent.map(noise, 0, 1, 0, parent.PIXEL_RESOLUTION_Y);
			
			g.vertex(x, y);
			
			// Update the offset intensity
			offsetIntensity += offsetIntensityIncrements;
		}
		g.endShape();
		
		// Increase the offset so it creates a wave
		offsetSpeed += offsetSpeedIncrements;
	}
}
