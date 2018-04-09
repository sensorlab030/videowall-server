package nl.sensorlab.videowall.animation;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.walldriver.WallGeometry;
import processing.core.PApplet;
import processing.core.PGraphics;


/**
 * The ExampleBaseAnimation is a simple animation
 * which illustrates how to make an animation extending the BaseAnimation
 * and how to use the grid geometry.
 *
 * The BaseAnimation has exactly the dimensions of the LED grid.
 *
 */
public class ExampleBaseAnimation extends BaseAnimation {

	// Grid iterators
	private int i = 0;
	private int j = 0;



	/**
	 * Initialize ExampleBaseAnimation
	 * @param applet
	 */
	public ExampleBaseAnimation(PApplet applet) {
		super(applet);
	}


	/**
	 * Draws an entire column of one pixel width and one row of one pixel width
	 * at a changing positions
	 *
	 * @param g
	 */
	@Override
	protected final void drawAnimationFrame(PGraphics g) {

		// Drawing settings
		g.background(0);
		g.noStroke();
		g.fill(PColor.getRandomColor());

		// Draw one row and one column at the given i,j position
		drawRow(g, i);
		drawColumn(g, j);

		// Update i and j
		i = (i + 1 > PIXEL_RESOLUTION_Y) ? 0 : i + 1;
		j = (j + 1 > PIXEL_RESOLUTION_X) ? 0 : j + 1;


	}

	private void drawColumn(PGraphics g, int x) {

		// A column is 1px wide and goes from top to bottom (0 to totalRows).
		g.rect(x, 0, 1, PIXEL_RESOLUTION_Y);

	}

	private void drawRow(PGraphics g, int y) {

		// A row is 1px wide and goes from left to rigth (0 to totalColumns).
		g.rect(0, y, PIXEL_RESOLUTION_X, 1);

	}
}
