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
	int step = 40; // Bar loading speed (load 10 levels at once)
	int rainColor = PColor.color(19, 172, 206);
	
	// VARIABLES
	float maxRain = Integer.MIN_VALUE;
	float minRain = Integer.MAX_VALUE;
	float[] currentHeight =  null;
	float[] finalHeight =  null;
	float[] xPos = null;
	
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
		currentHeight = new float[NBVALUES];
		finalHeight = new float[NBVALUES];
		xPos = new float[NBVALUES + 1];
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
		g.noFill();
		int i = 0;
		
		for (String year : rainDataSorted.keySet()) {
 			float rain = Float.parseFloat(rainDataSorted.get(year));
 			
			// Get y coordinates and height
			finalHeight[i] = PApplet.map(rain, 0, maxRain, 0, g.height);
			
			// Get color
			g.fill(rainColor);
			
			// Create shape
			PShape rect = g.createShape();
			rect.beginShape();
						
			// Add the 4 points to form a rectangle
			rect.vertex(xPos[i], g.height - currentHeight[i]);
			rect.vertex(xPos[i + 1], g.height - currentHeight[i]);
			rect.vertex(xPos[i + 1], g.height);
			rect.vertex(xPos[i], g.height);
			
			// Draw shape
			rect.endShape();
			g.shape(rect);
			
			
			// If bar has not reached its final height, add a level for next draw
			if (currentHeight[i] + step < finalHeight[i]) {
				currentHeight[i] = currentHeight[i] + step;
			} else {
				currentHeight[i] = finalHeight[i];
			}
			
			i++;
		}

	}

}
