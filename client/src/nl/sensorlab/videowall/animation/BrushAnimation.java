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
import processing.opengl.PShader;

public class BrushAnimation extends BaseCanvasAnimation {

	PApplet parent;

	// Example: https://www.openprocessing.org/sketch/110105

	private ArrayList<Brush> brushes; 
	private int amountOfBrushes = 100;

	public BrushAnimation(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);
		this.parent = applet;

		this.brushes = new ArrayList<Brush>();
		generateBrushes(amountOfBrushes);
	}

	public void generateBrushes(int _amountOfBrushes) {
		for(int i = 0; i < _amountOfBrushes; i++) {
			float x = (float)(Math.random() * parent.width);
			float y = (float)(Math.random() * parent.height);
			brushes.add(new Brush(new PVector(x, y)));
		}
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		g.noStroke();
		g.fill(0,1);
		g.rect(0, 0, g.width,  g.height);
		for(Brush cb : brushes) {
			cb.paint(g);
		}
	}
}
