package sorting;

abstract class Sort {
	protected int[] array;
	public int num;

	Sort (int[] a) {
		array = a;
	}

	abstract void sortStep();

	void drawArray() { 



		//	    float w = width/(array.length);
		//	  
		//
		//	    fill(255,0,0,128);
		//	    stroke(255,0,0,255);
		for (int i = 0; i < array.length; i++) {
			//	      float x = (w * (i + 1)) - w;
			//	      rect(x , height , w ,  -w * array[i]);
		}
	}

	void swap(int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
}


