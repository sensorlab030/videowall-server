package nl.sensorlab.videowall.animation;

import nl.sensorlab.videowall.walldriver.WallGeometry;
import processing.core.PApplet;
import processing.core.PGraphics;


/**
 * The ExampleBaseAnimation
 * and display it on the led wall, from left to right.
 *
 * The color of the phrase can also be supplied from the settings file.
 *
 */
public class ExampleBaseAnimation extends BaseAnimation {

	// Pixel grid dimensions
	private final int totalColumns = WallGeometry.getInstance().getPanelCount() * 2;
	private final int totalRows = WallGeometry.getPixelYCount();


	/**
	 * Initialize ExampleBaseAnimation
	 * @param applet
	 */
	public ExampleBaseAnimation(PApplet applet) {
		super(applet);
	}


	/**
	 * Draws a moving-to-the-left phrase that comes from the right edge of the wall
	 *
	 * @param g
	 */
	@Override
	protected final void drawAnimationFrame(PGraphics g) {
		g.fill(255);
		drawColumn(g, 3);
		drawRow(g, 3);

	}

	private void drawColumn(PGraphics g, int x) {
		g.noStroke();
		g.fill(255);
		g.rect(x, 0, 1, totalRows);
	}

	private void drawRow(PGraphics g, int y) {
		g.noStroke();
		g.fill(255);
		g.rect(0, y, totalColumns, 1);
	}
}
