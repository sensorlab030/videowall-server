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
	private static final Map<Character, List<LetterPoint>> ALPHABET = new HashMap<Character, List<LetterPoint>>();

	// Letters
	private static final int[][] LETTER_A = new int[][] {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 0}, {1, 2}, {2, 0}, {2, 2}, {3, 1}, {3, 2}, {3, 3}, {3, 4}};
	private static final int[][] LETTER_B = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 0}, {1, 2}, {1, 4}, {2, 0}, {2, 2}, {2, 4}, {3, 1}, {3, 3}};
	private static final int[][] LETTER_C = new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 0}, {1, 4}, {2, 0}, {2, 4}, {3, 1}, {3, 3}};
	private static final int[][] LETTER_D = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 0}, {1, 4}, {2, 0}, {2, 4}, {3, 1}, {3, 2}, {3, 3}};
	private static final int[][] LETTER_E = new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 0}, {1, 2}, {1, 4}, {2, 0}, {2, 2}, {2, 4}, {3, 0}, {3, 4}};
	private static final int[][] LETTER_F = new int[][] {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 0}, {1, 2}, {2, 0}, {2, 2}, {3, 0}};
	private static final int[][] LETTER_G = new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 0}, {1, 4}, {2, 0}, {2, 4}, {3, 1}, {3, 3}, {3, 4}};
	private static final int[][] LETTER_H = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 2}, {2, 2}, {3, 0}, {3, 1}, {3, 2}, {3, 3}, {3, 4}};
	private static final int[][] LETTER_I = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}};
	private static final int[][] LETTER_J = new int[][] {{0, 0}, {0, 3}, {1, 0}, {1, 4}, {2, 0}, {2, 1}, {2, 2}, {2, 3}, {3, 0}};
	private static final int[][] LETTER_K = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 2}, {2, 1}, {2, 3}, {3, 0}, {3, 4}};
	private static final int[][] LETTER_L = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 4}, {2, 4}};
	private static final int[][] LETTER_M = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 1}, {2, 2}, {3, 1}, {4, 0}, {4, 1}, {4, 2}, {4, 3}, {4, 4}};
	private static final int[][] LETTER_N = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 1}, {2, 2}, {3, 0}, {3, 1}, {3, 2}, {3, 3}, {3, 4}};
	private static final int[][] LETTER_O = new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 0}, {1, 4}, {2, 0}, {2, 4}, {3, 1}, {3, 2}, {3, 3}};
	private static final int[][] LETTER_P = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 0}, {1, 3}, {2, 0}, {2, 3}, {3, 1}, {3, 2}};
	private static final int[][] LETTER_Q = new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 0}, {1, 4}, {2, 0}, {2, 4}, {3, 1}, {3, 2}, {3, 3}, {3, 4}, {4, 4}};
	private static final int[][] LETTER_R = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 0}, {1, 2}, {2, 0}, {2, 2}, {3, 1}, {3, 3}, {3, 4}};
	private static final int[][] LETTER_S = new int[][] {{0, 1}, {0, 4}, {1, 0}, {1, 2}, {1, 4}, {2, 0}, {2, 2}, {2, 4}, {3, 0}, {3, 3}};
	private static final int[][] LETTER_T = new int[][] {{0, 0}, {1, 0}, {1, 1}, {1, 2}, {1, 3}, {1, 4}, {2, 0}};
	private static final int[][] LETTER_U = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {1, 4}, {2, 4}, {3, 0}, {3, 1}, {3, 2}, {3, 3}};
	private static final int[][] LETTER_V = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {1, 4}, {2, 3}, {3, 0}, {3, 1}, {3, 2}};
	private static final int[][] LETTER_W = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {1, 4}, {2, 2}, {2, 3}, {3, 4}, {4, 0}, {4, 1}, {4, 2}, {4, 3}};
	private static final int[][] LETTER_X = new int[][] {{0, 0}, {0, 1}, {0, 3}, {0, 4}, {1, 2}, {2, 0}, {2, 1}, {2, 3}, {2, 4}};
	private static final int[][] LETTER_Y = new int[][] {{0, 0}, {0, 1}, {0, 2}, {1, 2}, {1, 3}, {1, 4}, {2, 0}, {2, 1}, {2, 2}};
	private static final int[][] LETTER_Z = new int[][] {{0, 0}, {0, 4}, {1, 0}, {1, 3}, {1, 4}, {2, 0}, {2, 2}, {2, 4}, {3, 0}, {3, 1}, {3, 4}};


	// Singleton instance
	private static AlphabetGeometry instance = null;

	/**
	 * Create instance and calculate geometries
	 * based on real world measurements
	 */
	protected AlphabetGeometry() {
		ALPHABET.put('A', getLetterAsLetterPoints(LETTER_A));
		ALPHABET.put('B', getLetterAsLetterPoints(LETTER_B));
		ALPHABET.put('C', getLetterAsLetterPoints(LETTER_C));
		ALPHABET.put('D', getLetterAsLetterPoints(LETTER_D));
		ALPHABET.put('E', getLetterAsLetterPoints(LETTER_E));
		ALPHABET.put('F', getLetterAsLetterPoints(LETTER_F));
		ALPHABET.put('G', getLetterAsLetterPoints(LETTER_G));
		ALPHABET.put('H', getLetterAsLetterPoints(LETTER_H));
		ALPHABET.put('I', getLetterAsLetterPoints(LETTER_I));
		ALPHABET.put('J', getLetterAsLetterPoints(LETTER_J));
		ALPHABET.put('K', getLetterAsLetterPoints(LETTER_K));
		ALPHABET.put('L', getLetterAsLetterPoints(LETTER_L));
		ALPHABET.put('M', getLetterAsLetterPoints(LETTER_M));
		ALPHABET.put('N', getLetterAsLetterPoints(LETTER_N));
		ALPHABET.put('O', getLetterAsLetterPoints(LETTER_O));
		ALPHABET.put('P', getLetterAsLetterPoints(LETTER_P));
		ALPHABET.put('Q', getLetterAsLetterPoints(LETTER_Q));
		ALPHABET.put('R', getLetterAsLetterPoints(LETTER_R));
		ALPHABET.put('S', getLetterAsLetterPoints(LETTER_S));
		ALPHABET.put('T', getLetterAsLetterPoints(LETTER_T));
		ALPHABET.put('U', getLetterAsLetterPoints(LETTER_U));
		ALPHABET.put('V', getLetterAsLetterPoints(LETTER_V));
		ALPHABET.put('W', getLetterAsLetterPoints(LETTER_W));
		ALPHABET.put('X', getLetterAsLetterPoints(LETTER_X));
		ALPHABET.put('Y', getLetterAsLetterPoints(LETTER_Y));
		ALPHABET.put('Z', getLetterAsLetterPoints(LETTER_Z));
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
	 * @param c
	 * @return
	 */
	public List<LetterPoint> getLetter(char c) {
		return ALPHABET.get(c);
	}
}
