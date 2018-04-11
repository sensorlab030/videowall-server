package nl.sensorlab.videowall.animation;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PGraphics;
import sorting.BozoSort;
import sorting.BubbleSort;
import sorting.CocktailSort;
import sorting.GnomeSort;
import sorting.InsertionSort;
import sorting.OddEvenSort;
import sorting.OptimizedBubbleSort;
import sorting.Sort;

public class SortingAnimation extends BaseAnimation {

	PApplet parent;
	
	
	ArrayList<Sort> sortingmethods;
	private int sortingIndex = 0;

	// 26 - 2 = 24 / 2 = 12
	private int amountColumns = 12;
	
	private int updateSortingEvery = 2;
	private int updateCounter = 0;
	
	private int updateMethodEvery = 300;
	private int updateMethodCounter = 0;
	
	private int maxValue; // <-- Get the max value; so it's easier to map to (y)

	public SortingAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
	
		// Add the sorting methods to the list
		this.sortingmethods = new ArrayList<Sort>();
		
		// Add the sorting methods
		sortingmethods.add(new BubbleSort(randomIntArray(amountColumns)));
		sortingmethods.add(new OptimizedBubbleSort(randomIntArray(amountColumns)));
		sortingmethods.add(new CocktailSort(randomIntArray(amountColumns)));
		sortingmethods.add(new OddEvenSort(randomIntArray(amountColumns)));
		sortingmethods.add(new GnomeSort(randomIntArray(amountColumns)));
		sortingmethods.add(new BozoSort(randomIntArray(amountColumns)));
		sortingmethods.add(new InsertionSort(randomIntArray(amountColumns)));
	}
 
	private int[] randomIntArray(int n) {
		int[] data = new int[n];  
		for (int i = 0; i < n; i++) {
			data[i] = (int)(1 + Math.random() * (1 + n));
			maxValue = Math.max(maxValue, data[i]);
		}
		return data;
	}

	@Override
	protected void drawAnimationFrame(PGraphics g) {

		// Fade
		g.noStroke();
		g.fill(0, 20);
		g.rect(0, 0, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y);

		// Update the sorting
		if(updateCounter >= updateSortingEvery) {
			updateCounter = 0;
			sortingmethods.get(sortingIndex).sortStep();
		}else {
			updateCounter++;
		}

		// Draw the sorting
		sortingmethods.get(sortingIndex).drawArray(g, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y, maxValue);
		
		// Update the sorting method
		if(updateMethodCounter >= updateMethodEvery) {
			updateMethodCounter = 0;
			sortingIndex++;
			// Keep in bounds
			if(sortingIndex >= sortingmethods.size()) sortingIndex = 0;
			// Reset the data
			sortingmethods.get(sortingIndex).resetData(randomIntArray(amountColumns));
			
		}else{
			updateMethodCounter++;
		}
			
	}
}
