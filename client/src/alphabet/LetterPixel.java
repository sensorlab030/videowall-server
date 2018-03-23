package alphabet;

import processing.core.PGraphics;

/**
 * A LetterPixel represents one 'pixel block' in the pixel font. A letter is composed of several letterPixel
 * @author agathelenclen
 *
 */
public class LetterPixel {

	private int x;
	private int y;


	public LetterPixel(int x, int y) {
		this.x = x;
		this.y = y;
	}


	public void draw(PGraphics g) {
		g.rect(this.x, this.y, AlphabetGeometry.getInstance().LETTER_PIXEL_WIDTH, AlphabetGeometry.getInstance().LETTER_PIXEL_HEIGHT);
	}


	public void setX(int newX) {
		this.x = newX;
	}


	public int getX() {
		return this.x;
	}
}
