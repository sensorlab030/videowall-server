package nl.sensorlab.videowall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import jonas.Node;
import jonas.Particle;
import jonas.Pixel;
import jonas.Square;
import jonas.TriggerObject;
import jonas.TriggerSequence;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class ColorGridAnimation extends BaseAnimation {

	public PApplet parent;

	ArrayList<Square> squares;
	
	public int dimensions = 2;

	public ColorGridAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		
		squares = new ArrayList<Square>();
		generateSquares();
	}

	
	private void generateSquares() {
		for(int x = 0; x < PIXEL_RESOLUTION_X/dimensions; x+=dimensions) {
			squares.add(new Square(this, x, dimensions));
		}
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double t) {
		// Add some fade effect;
		g.noStroke();
		g.fill(20, 3);
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
		
		for(Square sq : squares) {
			sq.update();
			sq.display(g);
		}
	}
}