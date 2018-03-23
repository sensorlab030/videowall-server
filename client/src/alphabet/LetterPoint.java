package alphabet;

/**
 * A letterPoint represent the top left position (x, y) of a LetterPixel
 * @author agathelenclen
 *
 */
public class LetterPoint {

	private int x;
	private int y;


	public LetterPoint(int x, int y) {
		this.setX(x);
		this.setY(y);
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}
}
