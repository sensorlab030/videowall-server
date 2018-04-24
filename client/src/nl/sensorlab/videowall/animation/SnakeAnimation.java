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

public class SnakeAnimation extends BaseAnimation {

	PApplet parent;

	// Example
	// https://github.com/YuriyGuts/snake-ai-reinforcement
	// https://github.com/chuyangliu/Snake
	// https://www.openprocessing.org/sketch/524998

	public SnakeAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double t) {
		// Fade
		g.noStroke();
		g.fill(0, 2);
		g.rect(0, 0, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y);

	}
}
