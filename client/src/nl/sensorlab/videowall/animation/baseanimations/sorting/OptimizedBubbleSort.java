package nl.sensorlab.videowall.animation.baseanimations.sorting;

public class OptimizedBubbleSort extends BubbleSort {

	private int c = 0;

	public OptimizedBubbleSort(int amountValues, int color) {
		super(amountValues, color);
		reset();
	}
	
	public void reset() {
		c = 0;
		num = 2;
	}

	public void sortStep() {
		if (current + 1 == valuesArray.length - c) {
			current = 0;
			c++;
		}
		super.sortStep(); // Update self (recursive)
	}
}