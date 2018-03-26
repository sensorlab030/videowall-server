package alphabet;

import java.util.ArrayList;
import java.util.List;

import processing.core.PGraphics;


/**
 * The Letter class represents a letter as a list of LetterPixel
 * which visually forms the letter
 *
 */
public class Letter {

	private List<LetterPixel> letterPixels;		// A list of LetterPixel, representing each 'pixel' in the pixel font
	private int letterLength = 0;				// Maximum length of a letter in pixels


	public Letter(List<LetterPoint> points) {

		letterPixels = new ArrayList<LetterPixel>(points.size());

		for(LetterPoint point: points) {

			// For each 'pixel point' in the letter, create a LetterPixel
			letterPixels.add(new LetterPixel(point.getX(), point.getY()));

			// Calculate length of the letter, length being the number of horizontal letterPixels it can contain
			this.letterLength = (point.getX() > this.letterLength) ? point.getX() : this.letterLength;

		}

	}


	/**
	 * Draw the letter
	 * @param g
	 */
	public void draw(PGraphics g) {

		for(LetterPixel letterPixel: letterPixels) {
			letterPixel.draw(g);
		}

	}


	/**
	 * Get the letter visual representation length
	 * in pixels
	 */
	public int getLetterLength() {

		return this.letterLength;

	}
}
