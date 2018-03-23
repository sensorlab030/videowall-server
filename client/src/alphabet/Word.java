package alphabet;

import java.util.ArrayList;
import java.util.List;

import processing.core.PGraphics;

public class Word {
	private List<Letter> letters;
	private int wordLength = 0;

	public Word(String word) {
		String uppercaseWord = word.toUpperCase();
		letters = new ArrayList<Letter>();
		setLetters(uppercaseWord);
	}

	private void setLetters(String s) {
		// Iterate over all the characters
		for (int i = 0; i < s.length(); i++){
			// Get the character in word
		    char c = s.charAt(i);

		    // Get the corresponding letter schema in alphabet
		    List<LetterPoint> letterPoint = AlphabetGeometry.getInstance().getLetter(c);

		    // Add letter to list of letters
		    Letter letter = new Letter(letterPoint);
		    this.letters.add(letter);

		    // Update word length
		    this.setWordLength(this.getWordLength() + letter.getMaxX() + 2 * AlphabetGeometry.getInstance().LETTER_WIDTH);
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

	public int getWordLength() {
		return wordLength;
	}

	public void setWordLength(int wordLength) {
		this.wordLength = wordLength;
	}
}
