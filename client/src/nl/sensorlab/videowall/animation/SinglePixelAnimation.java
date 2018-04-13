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

public class SinglePixelAnimation extends BaseAnimation {

	PApplet parent;

	private PVector position;
	
	private float speed = 1f;
	
	private int directionX = 1;
	private int directionY = 1;

	public SinglePixelAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;

		// Set position
		position = new PVector(0,0);
	}

	protected void update() {
		position.y += (speed * directionY);

		// Update
		if(position.y > PIXEL_RESOLUTION_Y) {
			directionY = -1;
			position.x += (speed * directionX);
		}else if(position.y < 0) {
			directionY = 1;
			position.x += (speed * directionX);
		}

		if(position.x > PIXEL_RESOLUTION_X) {
			directionX = -1;
		}else if(position.x < 0) {
			directionX = 1;
		}
	}

	@Override
	protected void drawAnimationFrame(PGraphics g) {
		update();

		// Fade
		g.noStroke();
		g.fill(0, 2);
		g.rect(0, 0, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y);

		// Pixel
		g.noStroke();
		g.fill(220,20,147);
		g.rect(position.x, position.y, 1, 1);
	}
}
