package alphabet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that contains the wall geometry used for CanvasAnimation and
 * the animation previews.
 */
public class AlphabetGeometry {

	// Geometries (in pixels)
	public final int LETTER_WIDTH = 2;
	public final int LETTER_HEIGHT = 16;

	// Alphabet
	private static final Map<String, List<LetterPoint>> ALPHABET = new HashMap<String, List<LetterPoint>>();

	// Letters
	private static final int[][] LETTER_A = new int[][] {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 0}, {1, 2}, {2, 0}, {2, 2}, {3, 1}, {3, 2}, {3, 3}, {3, 4}};

	// Singleton instance
	private static AlphabetGeometry instance = null;

	/**
	 * Create instance and calculate geometries
	 * based on real world measurements
	 */
	protected AlphabetGeometry() {
		ALPHABET.put("A", getLetterAsLetterPoints(LETTER_A));
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


	/**
	 * Converts the letter coordinates to pixel positions on the canvas
	 * @param letter, array of coordinates on a grid of 4 x 5
	 * @return a list of Points, projection of coordinates on a 26 * 81 grid, with one block being of width LETTER_WIDTH and height LETTER_HEIGHT
	 */
	private List<LetterPoint> getLetterAsLetterPoints(int[][] letter) {
		List<LetterPoint> points = new ArrayList<LetterPoint>(letter.length);

		for(int[] coord: letter) {
			points.add(new LetterPoint(coord[0] * LETTER_WIDTH, coord[1] * LETTER_HEIGHT));
		}

		return points;
	}

	/**
	 * Get letter as a list of letter points from the Alphabet
	 * @param letter
	 * @return
	 */
	public List<LetterPoint> getLetter(String letter) {
		return ALPHABET.get(letter);
	}
}
