package sorting;

public class BozoSort extends Sort {

	boolean sorted;

	public BozoSort(int[] arr) {
		super(arr);
		sorted = false;
		num = 6;
	}

	public void sortStep() {
		if (sorted) return;

		int i = (int)(Math.random() * array.length-1);
		int j = (int)(Math.random() * array.length-1);
		swap(i, j);

		int a = 0;
		for (int x : array)
			if (x < a) {
				sorted = false;
				return;
			} else a = x;
	}
}