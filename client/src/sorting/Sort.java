package sorting;

import processing.core.PGraphics;

abstract class Sort {
	protected int[] array;
	public int num;

	Sort (int[] a) {
		array = a;
	}

	abstract void sortStep();

	public void drawArray(PGraphics g, int width, int height, int maxValue) {
		g.noStroke();
		g.fill(255);
		for (int i = 0; i < array.length; i++) {
			// Skip the first row 
			float x = 1 + (2 * i);	
			int h = (int)Math.ceil((height / maxValue) * array[i]) ;
			g.rect(x , height , 2, -h);
		}
	}

	public void swap(int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
}


