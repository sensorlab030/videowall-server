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

public class HorizontalScanAnimation extends BaseAnimation {

	PApplet parent;

	private float speed = 0.15f;
	private float x = 0;
	private int direction = 1;

	public HorizontalScanAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double t) {
		g.noStroke();
		g.fill(0,5);
		g.rect(0, 0, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y);

		// Update
		x += speed * direction;

		// Toggle direction
		direction = x > PIXEL_RESOLUTION_X ? -1 : x < 0 ? 1 : direction;

		// Color
		float c = parent.map(x, 0, PIXEL_RESOLUTION_X, 0, 255);

		g.strokeWeight(2);
		g.stroke(c, 200, 150, 128);
		g.line(x, 0, x, PIXEL_RESOLUTION_Y);
	}
}
