package nl.sensorlab.videowall.animation;

import java.util.ArrayList;

import com.cleverfranke.util.PColor;

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
	private int[] sortingColors = {}; // <-- Different colors for each sorting methods (just to make it more cleer)
	private int sortingIndex = 0;

	// ((26 - 2) = (24 / 2)) = 12
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

		// Create colors
		sortingColors = new int[sortingmethods.size()];

		sortingColors[0] = PColor.color(255,20,147);
		sortingColors[1] = PColor.color(255,105,180);
		sortingColors[2] = PColor.color(199,21,133);
		sortingColors[3] = PColor.color(153,50,204);
		sortingColors[4] = PColor.color(65,105,225);
		sortingColors[5] = PColor.color(135,206,250);
		sortingColors[6] = PColor.color(127,255,212);
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
	protected void drawAnimationFrame(PGraphics g, double t) {

		// Fade
		g.noStroke();
		g.fill(0, 50);
		g.rect(0, 0, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y);

		// Update the sorting
		if(updateCounter >= updateSortingEvery) {
			updateCounter = 0;
			sortingmethods.get(sortingIndex).sortStep();
		}else {
			updateCounter++;
		}

		// Draw the sorting
		sortingmethods.get(sortingIndex).drawArray(g, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y, maxValue, sortingColors[sortingIndex]);

		// Update the sorting method
		if(updateMethodCounter >= updateMethodEvery) {
			updateMethodCounter = 0;
			sortingIndex++;
			
			// Keep in bounds
			if(sortingIndex >= sortingmethods.size()) sortingIndex = 0;
			
			// Reset the data (randomize)
			sortingmethods.get(sortingIndex).resetData(randomIntArray(amountColumns));

		}else{
			updateMethodCounter++;
		}

	}
}
