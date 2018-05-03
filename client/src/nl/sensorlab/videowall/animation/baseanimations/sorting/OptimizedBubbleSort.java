package nl.sensorlab.videowall.animation.baseanimations.sorting;

public class OptimizedBubbleSort extends BubbleSort {

	private int c = 0;

	public OptimizedBubbleSort(int[] arr) {
		super(arr);
		int c = 0;
		num = 2;
	}

	public void sortStep() {
		if (current + 1 == valuesArray.length-c) {
			current = 0;
			c++;
		}
		super.sortStep();
	}
}