package sorting;

public class InsertionSort  extends Sort {

	private int i, j;
	private int value;

	public InsertionSort(int[] arr) {
		super(arr);
		i = 1;
		j = 1;
		value = array[1];

		num = 7;
	}

	public void sortStep() {
		if (i == array.length) return;

		if (j > 0 && value < array[j-1]) {
			array[j] = array[j-1];
			j--;
		} else {
			array[j] = value;
			i++;
			j = i;
			if (i != array.length) value = array[i];
		}
	}
}
