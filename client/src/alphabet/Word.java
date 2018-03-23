package alphabet;

import java.util.ArrayList;
import java.util.List;

import processing.core.PGraphics;

public class Word {
	private List<Letter> letters;

	public Word(String word) {
		String uppercaseWord = word.toUpperCase();
		letters = new ArrayList<Letter>();
		setLetters(uppercaseWord);
	}

	private void setLetters(String s) {
		// Iterate over all the characters
		for (int i = 0; i < s.length(); i++){
		    char c = s.charAt(i);
		    List<LetterPoint> letterPoint = AlphabetGeometry.getInstance().getLetter(c);
		    this.letters.add(new Letter(letterPoint));
		}
	}

	public void draw(PGraphics g) {
		for(Letter letter: letters) {
			letter.draw(g);

			// Translate and add a whitespace for next letter
			int offset = letter.getMaxX() + 2 * AlphabetGeometry.getInstance().LETTER_WIDTH;
			g.translate(offset, 0);
		}
	}
}
