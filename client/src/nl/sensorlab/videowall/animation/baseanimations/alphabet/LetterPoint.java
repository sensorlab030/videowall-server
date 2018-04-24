package nl.sensorlab.videowall.animation.baseanimations.alphabet;

/**
 * A LetterPoint represent the top left position (x, y) of a LetterPixel
 *
 */
public class LetterPoint {

	// Coordinates
	private int x;
	private int y;


	public LetterPoint(int x, int y) {

		this.setX(x);
		this.setY(y);

	}


	/**
	 * Get x coordinate
	 * @return
	 */
	public int getX() {

		return x;

	}


	/**
	 * Set x coordinate
	 * @param x
	 */
	public void setX(int x) {

		this.x = x;

	}


	/**
	 * Get y coordinate
	 * @return
	 */
	public int getY() {

		return y;

	}


	/**
	 * Set y coordinate
	 * @param y
	 */
	public void setY(int y) {

		this.y = y;

	}
}
