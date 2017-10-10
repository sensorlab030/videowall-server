package com.cleverfranke.ledwall.animation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;
import com.opencsv.CSVReader;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;

public class RainBarsAnimation extends Animation {
	// PARAMETERS
	int NBVALUES = 0; // Total number of bars
	int rainStackStep = 10 * WallConfiguration.PHYSICAL_PIXEL_PITCH_CM; // Bar loading speed (load 10 levels at once)
	int rainColor = PColor.color(19, 172, 206);
	
	// VARIABLES
	float maxRain = Integer.MIN_VALUE;
	float minRain = Integer.MAX_VALUE;
	float[] barCurrentHeight =  null;
	float[] lineCurrentHeight =  null;
	float[] finalHeight =  null;
	float[] xPos = null;
	int[] lineStep = null;

	
	// Data structure
	Map<String, String> rainDataSorted = new TreeMap<String, String>();
	
	/**
	 * Load CSV containing rain data and stores it in a Hashmap
	 * Hashmap key is the year column in the CSV
	 */
	private void loadRainData(String url) {
		// Initialize reader
		CSVReader reader = null;	
		Map<String, String> rainData = new HashMap<String, String>();
		
		// Open CSV data file
		try {
			reader = new CSVReader(new FileReader(url), ',');
			
			// Parse CSV data file
			String [] nextLine;
		     try {
				// Skip header
				reader.readNext();
				
				while ((nextLine = reader.readNext()) != null) {
				    // Insert data point in map
					rainData.put(nextLine[1], nextLine[2]);
					
				    // Find min and max in RAIN VALUES
					float rain = Float.parseFloat(nextLine[2]);
				    if (rain < minRain) { minRain = rain; }
				    if (rain > maxRain) { maxRain = rain; }
				 }
			} catch (IOException e) {
				e.printStackTrace();
			}
		     
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Sort Hashmap by year (the key)
		rainDataSorted = new TreeMap<String, String>(rainData);
	}
	
	/**
	 * Set the size of the arrays storing positions and height
	 */
	private void setVariables() {
		NBVALUES = rainDataSorted.size();
		barCurrentHeight = new float[NBVALUES];
		lineCurrentHeight = new float[NBVALUES];
		finalHeight = new float[NBVALUES];
		xPos = new float[NBVALUES + 1];
		lineStep = new int[NBVALUES];
		
		for (int i = 0; i < NBVALUES; i++) {
			barCurrentHeight[i] = (float)WallConfiguration.SOURCE_IMG_HEIGHT;
		}
		
		generateRandomLineSteps((float)WallConfiguration.SOURCE_IMG_HEIGHT);
	}
	
	private void generateRandomLineSteps(Float maxHeight) {
		for (int i = 0; i < NBVALUES; i++) {
			lineStep[i] = (int) PApplet.map((float)Math.random(), 0, 1, 10, maxHeight/2);
		}
	}
	
	@SuppressWarnings("deprecation")
	public RainBarsAnimation(PApplet applet) {
		super(applet);
		
		// Load data
		loadRainData("/Users/agathelenclen/Projects/led-wall/src/data/NL_Cumulative_Rain_04-17.csv");
		setVariables();
		
		// Initial left x position
		xPos[0] = 0;
			
		for(int i = 0; i < NBVALUES; i++) {
			// Get x right position of each panel
		    if (i != 0) {
		    	xPos[i] = WallConfiguration.PHYSICAL_PANEL_WIDTH_CM[i-1] * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO + xPos[i-1];
		    }
		}
		
		// Initial right end position
		xPos[NBVALUES] = WallConfiguration.PHYSICAL_PANEL_WIDTH_CM[NBVALUES-1] * WallConfiguration.SOURCE_CM_TO_PIXEL_RATIO + xPos[NBVALUES-1];
	}
	
	
	protected void drawAnimationFrame(PGraphics g) {		
		g.background(255);
		// Get color
		g.stroke(rainColor);
		g.fill(rainColor);
		
		int i = 0;
		
		for (String year : rainDataSorted.keySet()) {
 			float rain = Float.parseFloat(rainDataSorted.get(year));
 			
			// Get y coordinates and height
			finalHeight[i] = PApplet.map(rain, 0, maxRain, g.height, 0);
			
			// If bars are not fully drawn yet
			if (barCurrentHeight[i] != finalHeight[i]) {
				// Rain drops
				PShape line = g.createShape();
				line.beginShape();
				
				line.vertex(xPos[i], lineCurrentHeight[i]);
				line.vertex(xPos[i + 1], lineCurrentHeight[i]);

				line.endShape();
				g.shape(line);
				
				// If line has not reached the bar yet, move the line one step further
				if (lineCurrentHeight[i] + lineStep[i] < barCurrentHeight[i]) {
					lineCurrentHeight[i] = lineCurrentHeight[i] + lineStep[i];
				} else {
					// Reset line height
					lineCurrentHeight[i] = 0;
					
					// Set new bar current height
					barCurrentHeight[i] = barCurrentHeight[i] - rainStackStep;
					
					// Generate a new random line step
					generateRandomLineSteps(g.height - barCurrentHeight[i]);
				}
								
				// If bar has reached the finalHeight, do not add another level to the bar
				if (barCurrentHeight[i] - rainStackStep < finalHeight[i]) {
					barCurrentHeight[i] = finalHeight[i];
				}
			}
			
			
			// Rain bars
			// Create shape
			PShape rect = g.createShape();
			rect.beginShape();
			
			// Add the 4 points to form a rectangle
			rect.vertex(xPos[i], barCurrentHeight[i]);
			rect.vertex(xPos[i + 1], barCurrentHeight[i]);
			rect.vertex(xPos[i + 1], g.height);
			rect.vertex(xPos[i], g.height);
		
			// Draw shape
			rect.endShape();
			g.shape(rect);
			
			i++;
		}

	}

}
