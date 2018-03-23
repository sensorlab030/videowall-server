package nl.sensorlab.videowall.animation;

import com.cleverfranke.util.Settings;

import alphabet.Phrase;
import nl.sensorlab.videowall.walldriver.WallGeometry;
import processing.core.PApplet;
import processing.core.PGraphics;

public class Alphabet extends BaseAnimation {
	Phrase phrase;
	int offset = WallGeometry.getInstance().getPanelCount() * 2;
	int totalOffset = 0;
	long tStart = -1;
	int speed = 100;

	/**
	 * Initialize a PixelAnimation
	 *
	 * @param applet
	 */
	public Alphabet(PApplet applet) {
		super(applet);
		phrase = new Phrase(Settings.getValue("phrase"));
	}

	/**
	 * Implementation of generating the animation frame by child classes. Classes
	 * extending BaseAnimation are required to implement this method and use
	 * the supplied PGraphics context to draw the animation frame
	 *
	 * @param g
	 */
	@Override
	protected final void drawAnimationFrame(PGraphics g) {
		if (tStart == -1) {
			tStart = System.currentTimeMillis();
		}

		g.background(0);
		g.fill(255, 0 ,0);
		g.noStroke();

		g.translate(offset, 0);
		phrase.draw(g);

		movePhraseOffset();
		loopPhrase(g);

	}

	private long getElapsedTime() {
		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		return tDelta;
	}

	private void movePhraseOffset() {
		if (getElapsedTime() > speed) {
			// Move phrase
			offset--;
			totalOffset++;

			// Reset timer
			tStart = System.currentTimeMillis();
		}

	}

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
