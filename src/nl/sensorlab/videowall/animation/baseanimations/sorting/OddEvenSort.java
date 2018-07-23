package nl.sensorlab.videowall.animation.baseanimations.sorting;

public class OddEvenSort extends Sort {

	protected int current;
	private boolean even;

	public OddEvenSort(int amountValues, int color) {
		super(amountValues, color);
		reset();
	}
	
	public void reset() {
		current = 0;
		even = true;
	}

	public void sortStep() {
		if (current + 1 >= valuesArray.length) {
			even = !even;
			current = even ? 0 : 1;
			return;
		}

		if (valuesArray[current] > valuesArray[current+1]) {
			swapValues(current, current+1);
		}

		current += 2;
	}
}