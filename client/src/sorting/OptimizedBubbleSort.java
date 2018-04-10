package sorting;

public class OptimizedBubbleSort extends BubbleSort {

	private int c = 0;

	OptimizedBubbleSort(int[] arr) {
		super(arr);
		int c = 0;
		num = 2;
	}

	void sortStep() {
		if (current+1 == array.length-c) {
			current = 0;
			c++;
		}
		super.sortStep();
	}
}