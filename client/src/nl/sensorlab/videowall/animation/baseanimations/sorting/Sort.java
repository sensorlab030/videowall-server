package nl.sensorlab.videowall.animation.baseanimations.sorting;

public abstract class Sort {
	protected int[] valuesArray;
	public int num;
	
	public Sort(int[] _inputValuesArray) {
		this.valuesArray = _inputValuesArray;
	}
	
	public abstract void sortStep();
	
	public void draw() {	
	}
	
	public void setData(int[] _a) {
		this.valuesArray = _a;
	}
	
	public void swapValues(int _a, int _b) {
		int tempValue = this.valuesArray[_a];
		this.valuesArray[_a] = this.valuesArray[_b];
		this.valuesArray[_b] = tempValue;
	}

}
