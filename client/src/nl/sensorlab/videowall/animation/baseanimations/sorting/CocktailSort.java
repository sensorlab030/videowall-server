package nl.sensorlab.videowall.animation.baseanimations.sorting;

public class CocktailSort extends Sort {

	private int current;
	private int a, b;
	private int direction;

	public CocktailSort(int amountValues, int color) {
		super(amountValues, color);

	}
	
	public void reset() {
		current = 0;
		a = 0;
		b = 0;
		direction = 1;
		num = 3;
	}

	public void sortStep() {
		if (direction == 1) {
			if (current+1 >= valuesArray.length-b) {
				direction = -1;
				b++;
			} else if (valuesArray[current] > valuesArray[current+1])
				swapValues(current, current+1);
		} else {
			if (current-1 < a) {
				direction = 1;
				a++;
			} else if (valuesArray[current] < valuesArray[current-1])
				swapValues(current, current-1);
		}
		current += direction;
	}
}