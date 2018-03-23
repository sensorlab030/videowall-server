package alphabet;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import processing.core.PGraphics;

public class Phrase {
	private List<Word> words;
	private int phraseLength = 0;

	public Phrase(String phrase) {
		String uppercasePhrase = phrase.toUpperCase();
		words = new ArrayList<Word>();
		setWords(uppercasePhrase);
	}

	private void setWords(String s) {
		Pattern p = Pattern.compile("[\\w']+");
		Matcher m = p.matcher(s);

		while ( m.find() ) {
			// Add word to list of words
			Word word = new Word(s.substring(m.start(), m.end()));
		    words.add(word);

		    // Update phrase length
		    setPhraseLength(getPhraseLength() + word.getWordLength() + 2 * AlphabetGeometry.getInstance().LETTER_WIDTH);
		}
	}

	public void draw(PGraphics g) {
		for(Word word: words) {
			word.draw(g);

			// Translate and add a whitespace for next letter
			g.translate(2 * AlphabetGeometry.getInstance().LETTER_WIDTH, 0);
		}
	}

	public int getPhraseLength() {
		return phraseLength;
	}

	public void setPhraseLength(int phraseLength) {
		this.phraseLength = phraseLength;
	}
}
