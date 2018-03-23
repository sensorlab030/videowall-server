package alphabet;

public class LetterPixel extends Block {
	public LetterPixel(int x, int y) {
		super(x, y, AlphabetGeometry.getInstance().LETTER_WIDTH, AlphabetGeometry.getInstance().LETTER_HEIGHT);
	}

	public void offsetLeft(int value) {
		this.setX(this.getX() - value);
	}

	public void offsetRight(int value) {
		this.setX(this.getX() + value);
	}
}
