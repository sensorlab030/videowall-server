package sorting;

public class OddEvenSort extends Sort {

	protected int current;
	private boolean even;

	OddEvenSort(int[] arr) {
		super(arr);
		current = 0;
		num = 4;
		even = true;
	}

	void sortStep() {
		if (current+1 >= array.length) {
			even = !even;
			current = even ? 0 : 1;
			return;
		}

		if (array[current] > array[current+1])
			swap(current, current+1);

		current += 2;
	}
}
