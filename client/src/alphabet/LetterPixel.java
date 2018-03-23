package alphabet;

public class LetterPixel extends Block {
	public LetterPixel(int x, int y) {
		super(x, y, AlphabetGeometry.getInstance().LETTER_WIDTH, AlphabetGeometry.getInstance().LETTER_HEIGHT);
	}
}
