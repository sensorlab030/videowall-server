package alphabet;

public class LetterPixel extends Block {
	public LetterPixel(int x, int y) {
		super(x, y, AlphabetGeometry.getInstance().getLetterWidth(), AlphabetGeometry.getInstance().getLetterHeight());
	}
}
