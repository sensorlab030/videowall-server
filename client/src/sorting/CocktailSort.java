package sorting;

public class CocktailSort extends Sort {

	private int current;
	private int a, b;
	private int dir;

	CocktailSort(int[] arr) {
		super(arr);
		current = 0;
		a = 0;
		b = 0;
		dir = 1;

		num = 3;
	}

	void sortStep() {
		if (dir == 1) {
			if (current+1 >= array.length-b) {
				dir = -1;
				b++;
			} else if (array[current] > array[current+1])
				swap(current, current+1);
		} else {
			if (current-1 < a) {
				dir = 1;
				a++;
			} else if (array[current] < array[current-1])
				swap(current, current-1);
		}

		current += dir;
	}
}