package alphabet;

import processing.core.PGraphics;

/**
 * A LetterPixel represents one 'pixel block' of a letter.
 * A Letter is composed of several LetterPixel.
 *
 */
public class LetterPixel {

	// Top left coordinates of the pixel block
	private int x;
	private int y;


	public LetterPixel(int x, int y) {

		this.x = x;
		this.y = y;

	}


	/**
	 * Draw a rectangle at the given (x, y) coordinates
	 * With a width and height equal to the letter pixels width and height
	 * @param g
	 */
	public void draw(PGraphics g) {

		g.rect(this.x, this.y, AlphabetGeometry.getInstance().LETTER_PIXEL_WIDTH, AlphabetGeometry.getInstance().LETTER_PIXEL_HEIGHT);

	}

	/**
	 * Get x coordinate value
	 * @param newX
	 */
	public void setX(int newX) {
		this.x = newX;
	}


	/**
	 * Set x coordinate value
	 * @return
	 */
	public int getX() {

		return this.x;

	}
}
