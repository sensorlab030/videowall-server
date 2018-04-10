package nl.sensorlab.videowall.animation;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import jonas.Brush;
import jonas.Pixel;
import jonas.TriggerObject;
import jonas.TriggerSequence;
import nl.sensorlab.videowall.walldriver.WallGeometry;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.Sort;
import processing.opengl.PShader;
import sorting.BozoSort;
import sorting.BubbleSort;
import sorting.OddEvenSort;

public class SortingAnimation extends BaseAnimation {

	PApplet parent;
	
	BubbleSort currentsort;

	// 26 - 2 = 24 / 2 = 12
	private int amountColumns = 12;
	
	private int updateEvery = 2;
	private int updateCounter = 0;
	
	private int maxValue;

	public SortingAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;

		// Init sorting
		currentsort = new BubbleSort(randomIntArray(amountColumns));
	}


	private int[] randomIntArray(int n) {
		int[] data = new int[n];  
		for (int i = 0; i < n; i++) {
			data[i] = (int)(1 + Math.random() * (1 + n));
			maxValue = Math.max(maxValue, data[i]);
		}
		return data;
	}

	@Override
	protected void drawAnimationFrame(PGraphics g) {

		// Fade
		g.noStroke();
		g.fill(0, 20);
		g.rect(0, 0, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y);

		// Update the sorting
		if(updateCounter >= updateEvery) {
			updateCounter = 0;
			currentsort.sortStep();
		}else {
			updateCounter++;
		}

		// Draw the sorting
		currentsort.drawArray(g, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y, maxValue);
	}
}
