package nl.sensorlab.videowall.animation.baseanimations.sorting;

public class BubbleSort extends Sort {

	protected int current;

	public BubbleSort(int[] arr) {
		super(arr);
		current = 0;
		num = 1;
	}

	public void sortStep() {
		if (current + 1 == valuesArray.length) current = 0;
		if (valuesArray[current] > valuesArray[current + 1]) swapValues(current, current + 1);
		current++;
	}
}