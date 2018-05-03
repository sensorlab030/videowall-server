package nl.sensorlab.videowall.animation.baseanimations.sorting;

public class GnomeSort extends Sort {

	private int i, j;

	public GnomeSort(int[] arr) {
		super(arr);
		i = 1;
		j = 1;
		num = 5;
	}

	public void sortStep() {
		if (i == valuesArray.length) return;
		if (j > 0 && valuesArray[j] < valuesArray[j-1]) {
			swapValues(j, j-1);
			j--;
		} else {
			i++;
			j = i;
		}
	}
}