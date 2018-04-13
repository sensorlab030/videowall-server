package nl.sensorlab.videowall.animation;

import java.util.ArrayList;

import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PGraphics;
import sorting.BozoSort;
import sorting.BubbleSort;
import sorting.CocktailSort;
import sorting.GnomeSort;
import sorting.InsertionSort;
import sorting.OddEvenSort;
import sorting.OptimizedBubbleSort;
import sorting.Sort;

public class PongGameAnimation extends BaseAnimation {

	PApplet parent;
	
//	Server server;
//	Client client;

	public PongGameAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
	}


	@Override
	protected void drawAnimationFrame(PGraphics g) {

		// Fade
		g.noStroke();
		g.fill(0, 20);
		g.rect(0, 0, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y);
	}
}
