package alphabet;

import java.util.ArrayList;
import java.util.List;

import processing.core.PGraphics;

public class Letter {
	private List<LetterPixel> letterPixels;

	public Letter(List<LetterPoint> points) {
		letterPixels = new ArrayList<LetterPixel>(points.size());

		for(LetterPoint point: points) {
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

//	public void animate(PGraphics g) {
//
//		for(LetterPixel letterPixel: letterPixels) {
//			letterPixel.draw(g);
//			letterPixel.offsetLeft(AlphabetGeometry.getInstance().LETTER_WIDTH);
//		}
//	}
}
