package alphabet;

import java.util.ArrayList;
import java.util.List;

import processing.core.PGraphics;

public class Letter {
	private List<LetterPixel> letterPixels;
	private int maxX = 0;

	public Letter(List<LetterPoint> points) {
		letterPixels = new ArrayList<LetterPixel>(points.size());

		for(LetterPoint point: points) {
			this.maxX = (point.getX() > this.maxX) ? point.getX() : this.maxX;
			letterPixels.add(new LetterPixel(point.getX(), point.getY()));
		}
	}

	public void draw(PGraphics g) {
		for(LetterPixel letterPixel: letterPixels) {
			letterPixel.draw(g);
		}
	}

	public void offsetLeft(int value) {
		for(LetterPixel letterPixel: letterPixels) {
			letterPixel.offsetLeft(value);
		}
	}

	public void offsetRight(int value) {
		for(LetterPixel letterPixel: letterPixels) {
			letterPixel.offsetRight(value);
		}
	}

	public int getMaxX() {
		return this.maxX;
	}

//	public void animate(PGraphics g) {
//
//		for(LetterPixel letterPixel: letterPixels) {
//			letterPixel.draw(g);
//			letterPixel.offsetLeft(AlphabetGeometry.getInstance().LETTER_WIDTH);
//		}
//	}
}
