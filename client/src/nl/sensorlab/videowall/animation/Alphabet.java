package nl.sensorlab.videowall.animation;

import com.cleverfranke.util.PColor;
import com.cleverfranke.util.Settings;

import alphabet.Phrase;
import nl.sensorlab.videowall.walldriver.WallGeometry;
import processing.core.PApplet;
import processing.core.PGraphics;


/**
 * The Alphabet animation takes a sentence from the settings file
 * and display it on the led wall, from left to right.
 *
 * The color of the phrase can also be supplied from the settings file.
 *
 */
public class Alphabet extends BaseAnimation {

	private final static int SLIDING_TIMER_MS = 200;						// Time countdown after which the phrase animates one step to the left

	private Phrase phrase;													// Phrase to animate on the wall
	private int offset = WallGeometry.getInstance().getPanelCount() * 2;	// Offset value to position the left edge of the phrase (is set to right edge of the pixel grid)
	private int totalOffset = 0;											// Count the total amount of offset operated
	private long tStart = -1;												// Timer start
	private int color = PColor.color(0, 255, 0);							// Default phrase color


	/**
	 * Initialize Alphabet animation
	 *
	 * @param applet
	 */
	public Alphabet(PApplet applet) {

		super(applet);

		// Create Phrase
		phrase = new Phrase(Settings.getValue("phrase"));

		// Set color if provided
		if (Settings.getValue("phraseColor").length() != 0) {
			color = PColor.color(Settings.getValue("phraseColor"));
		}

	}


	/**
	 * Draws a moving-to-the-left phrase that comes from the right edge of the wall
	 *
	 * @param g
	 */
	@Override
	protected final void drawAnimationFrame(PGraphics g, double dt) {

		// On first draw, start the timer
		if (tStart == -1) {
			tStart = System.currentTimeMillis();
		}

		// Drawing constants
		g.background(0);
		g.fill(color);
		g.noStroke();

		// Translate phrase one step to the left
		g.translate(offset, 0);

		// Draw phrase
		phrase.draw(g);

		// Update the offset value
		movePhraseOffset();

		// Put the phrase back to the right edge of the grid
		// if the whole phrase has been shown
		loopPhrase(g);

	}


	/**
	 * Get elapsed time between now and previous timestamp
	 * @return Delta
	 */
	private long getElapsedTime() {
		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		return tDelta;
	}


	/**
	 * If the elapsedTime between last movement and now is big enough, move the phrase one step to the left
	 */
	private void movePhraseOffset() {
		if (getElapsedTime() > SLIDING_TIMER_MS) {
			// Move phrase
			offset--;
			totalOffset++;

			// Reset timer
			tStart = System.currentTimeMillis();
		}

	}

	/**
	 * Loop phrase back to its beginning when it's done moving left of the wall
	 * @param g
	 */
	private void loopPhrase(PGraphics g) {

		// If phrase has not yet disappeared to the left, continue animation
		if (!(totalOffset > WallGeometry.getInstance().getPanelCount() * 2 + phrase.getPhraseLength())) {
			return;
		}

		// Else, bring back the phrase at the right edge
		offset = WallGeometry.getInstance().getPanelCount() * 2;
		totalOffset = 0;
		g.translate(offset + phrase.getPhraseLength(), 0);

	}
}
