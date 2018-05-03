package nl.sensorlab.videowall.animation.baseanimations;
import java.util.ArrayList;
import nl.sensorlab.videowall.animation.BaseAnimation;
import nl.sensorlab.videowall.animation.baseanimations.sorting.BozoSort;
import nl.sensorlab.videowall.animation.baseanimations.sorting.BubbleSort;
import nl.sensorlab.videowall.animation.baseanimations.sorting.CocktailSort;
import nl.sensorlab.videowall.animation.baseanimations.sorting.GnomeSort;
import nl.sensorlab.videowall.animation.baseanimations.sorting.InsertionSort;
import nl.sensorlab.videowall.animation.baseanimations.sorting.OddEvenSort;
import nl.sensorlab.videowall.animation.baseanimations.sorting.OptimizedBubbleSort;
import nl.sensorlab.videowall.animation.baseanimations.sorting.Sort;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Visualization of different sorting methods
 */
public class SortingAnimation extends BaseAnimation {

	private PApplet parent;

	private ArrayList<Sort> sortingmethods;

	// Settings
	private int sortingIndex = 0; // <-- Which methods to select
	private int amountColumns = 12;
	
	// Update step
	private int updateSortingEvery = 2;
	private int updateCounter = 0;
	
	// Update sorting method
	private int updateMethodEvery = 300;
	private int updateMethodCounter = 0;
	
	private int maxValue; // <-- Get the max value; so it's easier to map to (y)

	public SortingAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;

		// Create the sorting methods list
		this.sortingmethods = new ArrayList<Sort>();

		// Add the sorting methods
		this.sortingmethods.add(new BubbleSort(randomIntArray(amountColumns)));
		this.sortingmethods.add(new OptimizedBubbleSort(randomIntArray(amountColumns)));
		this.sortingmethods.add(new CocktailSort(randomIntArray(amountColumns)));
		this.sortingmethods.add(new OddEvenSort(randomIntArray(amountColumns)));
		this.sortingmethods.add(new GnomeSort(randomIntArray(amountColumns)));
		this.sortingmethods.add(new BozoSort(randomIntArray(amountColumns)));
		this.sortingmethods.add(new InsertionSort(randomIntArray(amountColumns)));
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
		// Fade background
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
		sortingmethods.get(sortingIndex).draw(g, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y, maxValue, 255);

		// Update the sorting method
		if(updateMethodCounter >= updateMethodEvery) {
			updateMethodCounter = 0;
			sortingIndex++;

			// Keep in bounds
			if(sortingIndex >= sortingmethods.size()) sortingIndex = 0;

			// Reset the data (randomize)
			sortingmethods.get(sortingIndex).setData(randomIntArray(amountColumns));
		}else{
			updateMethodCounter++;
		}
	}
}