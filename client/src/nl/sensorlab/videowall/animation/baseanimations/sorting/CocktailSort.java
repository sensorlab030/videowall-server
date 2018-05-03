package nl.sensorlab.videowall.animation.baseanimations.sorting;

public class CocktailSort extends Sort {

	private int current;
	private int a, b;
	private int dir;

	public CocktailSort(int[] arr) {
		super(arr);
		current = 0;
		a = 0;
		b = 0;
		dir = 1;
		num = 3;
	}

	public void sortStep() {
		if (dir == 1) {
			if (current+1 >= valuesArray.length-b) {
				dir = -1;
				b++;
			} else if (valuesArray[current] > valuesArray[current+1])
				swapValues(current, current+1);
		} else {
			if (current-1 < a) {
				dir = 1;
				a++;
			} else if (valuesArray[current] < valuesArray[current-1])
				swapValues(current, current-1);
		}

		current += dir;
	}
}