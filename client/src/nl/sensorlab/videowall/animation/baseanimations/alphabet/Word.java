package nl.sensorlab.videowall.animation.baseanimations.alphabet;

import java.util.ArrayList;
import java.util.List;

import processing.core.PGraphics;

/**
 * A Word represents a collection of Letters
 *
 */
public class Word {

	private List<Letter> letters;	// List of Letters of the word
	private int wordLength = 0;		// Total length, on the pixel grid, of the word (including spacing)


	public Word(String word) {

		String uppercaseWord = word.toUpperCase();
		letters = new ArrayList<Letter>();
		setLetters(uppercaseWord);

	}


	/**
	 * For each letter in a word, create a Letter object
	 * @param s: the word
	 */
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
		    this.setWordLength(this.getWordLength() + letter.getLetterLength() + 2 * AlphabetGeometry.getInstance().LETTER_PIXEL_WIDTH);

		}

	}


	/**
	 * Draw the word
	 * @param g
	 */
	public void draw(PGraphics g) {

		for(Letter letter: letters) {

			letter.draw(g);

			// Translate and add a whitespace for next letter
			int offset = letter.getLetterLength() + 2 * AlphabetGeometry.getInstance().LETTER_PIXEL_WIDTH;
			g.translate(offset, 0);

		}

	}


	/**
	 * Get the word total length, in pixels
	 * @return
	 */
	public int getWordLength() {

		return wordLength;

	}


	/**
	 * Set the word total length, in pixels
	 * @param wordLength
	 */
	public void setWordLength(int wordLength) {

		this.wordLength = wordLength;

	}
}
