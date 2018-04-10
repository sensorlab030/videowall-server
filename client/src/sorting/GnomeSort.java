package sorting;

public class GnomeSort extends Sort {

	private int i, j;

	GnomeSort(int[] arr) {
		super(arr);
		i = 1;
		j = 1;

		num = 5;
	}

	void sortStep() {
		if (i == array.length) return;

		if (j > 0 && array[j] < array[j-1]) {
			swap(j, j-1);
			j--;
		} else {
			i++;
			j = i;
		}
	}
}