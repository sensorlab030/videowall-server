package alphabet;


/**
 * Class that contains the wall geometry used for CanvasAnimation and
 * the animation previews.
 */
public class AlphabetGeometry {

	private static final int LETTER_WIDTH = 2;
	private static final int LETTER_HEIGHT = 16;

	// Singleton instance
	private static AlphabetGeometry instance = null;

	/**
	 * Create instance and calculate geometries
	 * based on real world measurements
	 */
	protected AlphabetGeometry() {

	}

	/**
	 * Get singleton instance
	 * @return
	 */
	public static AlphabetGeometry getInstance() {
		if (instance == null) {
			instance = new AlphabetGeometry();
		}
		return instance;
	}

	public int getLetterWidth() {
		return LETTER_WIDTH;
	}

	public int getLetterHeight() {
		return LETTER_HEIGHT;
	}
}
