package alphabet;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import processing.core.PGraphics;

/**
 * A phrase is a collection of words
 * @author agathelenclen
 *
 */
public class Phrase {

	private List<Word> words;		// List of words in the phrase
	private int phraseLength = 0;	// Total phrase length, in pixel


	public Phrase(String phrase) {
		words = new ArrayList<Word>();
		setWords(phrase);
	}


	/**
	 * Identifies the different words in the phrase, and save them to the list of words
	 * @param s
	 */
	private void setWords(String s) {
		Pattern p = Pattern.compile("[\\w'!.?<>+-=_#%/;:()]+");
		Matcher m = p.matcher(s);

		while ( m.find() ) {
			// Add word to list of words
			Word word = new Word(s.substring(m.start(), m.end()));
		    words.add(word);

		    // Update phrase length
		    setPhraseLength(getPhraseLength() + word.getWordLength() + 2 * AlphabetGeometry.getInstance().LETTER_PIXEL_WIDTH);
		}
	}


	/**
	 * Draw the phrase
	 * @param g
	 */
	public void draw(PGraphics g) {
		for(Word word: words) {
			word.draw(g);

			// Translate and add a whitespace for next letter
			g.translate(2 * AlphabetGeometry.getInstance().LETTER_PIXEL_WIDTH, 0);
		}
	}


	public int getPhraseLength() {
		return phraseLength;
	}


	public void setPhraseLength(int phraseLength) {
		this.phraseLength = phraseLength;
	}
}
