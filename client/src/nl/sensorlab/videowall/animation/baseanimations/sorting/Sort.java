package nl.sensorlab.videowall.animation.baseanimations.sorting;
import processing.core.PGraphics;

public abstract class Sort {
	protected int[] valuesArray;
	public int currentSelected = 0;
	public int num;

	public Sort(int[] _inputValuesArray) {
		this.valuesArray = _inputValuesArray;
	}

	public abstract void sortStep();

	public void draw(PGraphics g, int _width, int _height, float _maxValue, int _color) {
		g.noStroke();
	
		for(int i = 0; i < this.valuesArray.length; i++) {
			
			// Skip the first row
			int x = 1 + (2 * i);
			int h = (int) Math.ceil((_height / _maxValue) * valuesArray[i]) ;
			
			// Highlight the current selected (being sorted)
			if(currentSelected == i) {
				g.fill(255);
			}else {
				g.fill(_color);
			}
			
			// Draw
			g.rect(x, _height, 2, -h);
		}
	}

	public void setData(int[] _a) {
		this.valuesArray = _a;
	}

	public void swapValues(int _a, int _b) {
		int tempValue = this.valuesArray[_a];
		this.valuesArray[_a] = this.valuesArray[_b];
		this.valuesArray[_b] = tempValue;
		this.currentSelected = _b;
	}

}
