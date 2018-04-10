package sorting;

public class BubbleSort extends Sort {

	protected int current;

	BubbleSort(int[] arr) {
		super(arr);
		current = 0;
		num = 1;
	}

	void sortStep() {
		if (current+1 == array.length) current = 0;

		if (array[current] > array[current+1])
			swap(current, current+1);

		current++;
	}
}