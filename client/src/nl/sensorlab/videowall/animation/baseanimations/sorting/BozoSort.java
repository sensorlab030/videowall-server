package nl.sensorlab.videowall.animation.baseanimations.sorting;

public class BozoSort extends Sort{
	boolean isSorted;

	public BozoSort(int amountValues, int color) {
		super(amountValues, color);
		reset();
	}
	
	public void reset() {
		isSorted = false;
		num = 6;	
		
	}
	
	public void sortStep() {
		if (isSorted) {
			return;
		}

		int i = (int)(Math.random() * valuesArray.length-1);
		int j = (int)(Math.random() * valuesArray.length-1);
		swapValues(i, j);

		int a = 0;
		for (int x : valuesArray)
			if (x < a) {
				isSorted = false;
				return;
			} else {
				a = x;
			}
	}
}
