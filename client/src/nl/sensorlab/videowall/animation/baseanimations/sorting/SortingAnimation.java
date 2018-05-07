package nl.sensorlab.videowall.animation.baseanimations.sorting;
import java.util.ArrayList;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Visualization of different sorting methods
 */
public class SortingAnimation extends BaseAnimation {

	private ArrayList<Sort> sortingmethods;

	// Settings
	private int sortingMethodIndex = 0; // <-- Which methods to select
	private final static int AMOUNT_COLUMNS = 12;

	// Update step
	private static final int UPDATE_SORTING_EVERY_MILLIS = 100;
	private int updateCounterMillis = 0;

	// Update sorting method -> loop through the sorting methods
	private static final int UPDATE_METHOD_EVERY_MILLIS = 16000;
	private int updateMethodCounterMillis = 0;

	public SortingAnimation(PApplet applet) {
		super(applet);

		// Create the sorting methods list
		this.sortingmethods = new ArrayList<Sort>();

		// Add the sorting methods
		this.sortingmethods.add(new BubbleSort(AMOUNT_COLUMNS,  PColor.color(255,20,147)));
		this.sortingmethods.add(new OptimizedBubbleSort(AMOUNT_COLUMNS,  PColor.color(255,105,180)));
		this.sortingmethods.add(new CocktailSort(AMOUNT_COLUMNS, PColor.color(199,21,133)));
		this.sortingmethods.add(new OddEvenSort(AMOUNT_COLUMNS, PColor.color(153,50,204)));
		this.sortingmethods.add(new GnomeSort(AMOUNT_COLUMNS,PColor.color(65,105,225)));
		this.sortingmethods.add(new BozoSort(AMOUNT_COLUMNS, PColor.color(135,206,250)));
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double dt) {
		// Fade background
		g.noStroke();
		g.fill(0, 50);
		g.rect(0, 0, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y);

		// Update the sorting
		if(updateCounterMillis >= UPDATE_SORTING_EVERY_MILLIS) {
			updateCounterMillis = 0;
			sortingmethods.get(sortingMethodIndex).sortStep();
		}else {
			updateCounterMillis += dt;
		}
		
		// Draw the sorting
		sortingmethods.get(sortingMethodIndex).draw(g, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y);

		// Update the sorting method
		if(updateMethodCounterMillis >= UPDATE_METHOD_EVERY_MILLIS) {
			updateMethodCounterMillis = 0;
			sortingMethodIndex++;

			// Keep in bounds
			if(sortingMethodIndex >= sortingmethods.size()) sortingMethodIndex = 0;

			// Reset the data (randomize)
			sortingmethods.get(sortingMethodIndex).resetData(AMOUNT_COLUMNS);
			sortingmethods.get(sortingMethodIndex).reset();
		}else{
			updateMethodCounterMillis += dt;
		}
	}
}