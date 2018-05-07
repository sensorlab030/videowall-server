package nl.sensorlab.videowall.animation.baseanimations.sorting;
import processing.core.PGraphics;

public abstract class Sort {
	protected int[] valuesArray;
	
	public int currentSelected = 0;
	public int num;
	
	public int color;
	public int selectedColor = 255;
	public float maxValue = 0 ;

	public Sort(int amountValues, int color) {
		this.valuesArray = randomValues(amountValues);
		this.color = color;
	}

	public abstract void sortStep();

	public void draw(PGraphics g, int width, int height) {
		g.noStroke();
	
		for(int i = 0; i < this.valuesArray.length; i++) {
			
			// Skip the first row
			int x = 1 + (2 * i);
			int columnHeight = (int) Math.ceil((height / maxValue) * valuesArray[i]) ;
			
			// Highlight the current selected (being sorted)
			if(currentSelected == i) {
				g.fill(selectedColor);
			}else {
				g.fill(color);
			}
			
			// Draw
			g.rect(x, height, 2, -columnHeight);
		}
	}

	public void resetData(int amountValues) {
		this.valuesArray = randomValues(amountValues);
	}
	
	private int[] randomValues(int n) {
		int[] data = new int[n];  
		for (int i = 0; i < n; i++) {
			data[i] = (int)(1 + Math.random() * (1 + n));
			// Get the max value so we can map the height
			this.maxValue = Math.max(this.maxValue, data[i]); 
		}
		return  data;
	}

	public void swapValues(int a, int b) {
		int tempValue = this.valuesArray[a];
		this.valuesArray[a] = this.valuesArray[b];
		this.valuesArray[b] = tempValue;
		this.currentSelected = b;
	}
	

}
