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
		
		// Start the shape
		g.beginShape();
		for(int x = 0; x <= parent.PIXEL_RESOLUTION_X; x++) {
			// Returns the Perlin noise value at specified coordinates. 
			// Perlin noise is a random sequence generator producing a more natural, harmonic succession of numbers than that of the standard random() function. 
			// It was developed by Ken Perlin in the 1980s and has been used in graphical applications to generate procedural textures, shapes, terrains, and other seemingly organic forms.
			float perlinNoise = parent.parent.noise(offsetIntensity, offsetSpeed);
			float y = parent.parent.map(perlinNoise, 0, 1, 0, parent.PIXEL_RESOLUTION_Y);
			
			// Set the vertex
			g.vertex(x, y);
			
			// Update the offset intensity
			offsetIntensity += offsetIntensityIncrements;
		}
		// Close the shape
		g.endShape();
		
		// Increase the offset so it creates a wave
		offsetSpeed += offsetSpeedIncrements;
	}
}
