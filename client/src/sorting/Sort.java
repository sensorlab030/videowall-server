package sorting;

import processing.core.PGraphics;

public abstract class Sort {
	protected int[] array;
	public int num;

	public Sort (int[] a) {
		array = a;
	}

	public abstract void sortStep();

	public void drawArray(PGraphics g, int width, int height, int maxValue, int color) {
		g.noStroke();
		g.fill(color);
		for (int i = 0; i < array.length; i++) {
			// Skip the first row 
			float x = 1 + (2 * i);	
			int h = (int)Math.ceil((height / maxValue) * array[i]) ;
			g.rect(x , height , 2, -h);
		}
	}
	
	public void resetData(int[] a) {
		array = a;
	}

	public void swap(int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
}


