package nl.sensorlab.videowall.animation.baseanimations.sorting;

public class InsertionSort  extends Sort {

	private int i, j;
	private int value;

	public InsertionSort(int[] arr) {
		super(arr);
		i = 1;
		j = 1;
		value = valuesArray[1];
		num = 7;
	}

	public void sortStep() {
		if (i == valuesArray.length) return;

		if (j > 0 && value < valuesArray[j-1]) {
			valuesArray[j] = valuesArray[j-1];
			j--;
		} else {
			valuesArray[j] = value;
			i++;
			j = i;
			if (i != valuesArray.length) value = valuesArray[i];
		}
	}
}
