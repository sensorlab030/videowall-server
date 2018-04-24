package nl.sensorlab.videowall.animation.baseanimations.alphabet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that contains the alphabet geometry used for the alphabet animation.
 *
 */
public class AlphabetGeometry {

	// Geometries (in pixels)
	public final int LETTER_PIXEL_WIDTH = 2;
	public final int LETTER_PIXEL_HEIGHT = 16;

	// Alphabet
	private static final Map<Character, List<LetterPoint>> ALPHABET = new HashMap<Character, List<LetterPoint>>();

	// Letters
	private static final int[][] LETTER_A = new int[][] {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 0}, {1, 2}, {2, 0}, {2, 2}, {3, 1}, {3, 2}, {3, 3}, {3, 4}};
	private static final int[][] LETTER_B = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 0}, {1, 2}, {1, 4}, {2, 0}, {2, 2}, {2, 4}, {3, 1}, {3, 3}};
	private static final int[][] LETTER_C = new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 0}, {1, 4}, {2, 0}, {2, 4}, {3, 1}, {3, 3}};
	private static final int[][] LETTER_D = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 0}, {1, 4}, {2, 0}, {2, 4}, {3, 1}, {3, 2}, {3, 3}};
	private static final int[][] LETTER_E = new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 0}, {1, 2}, {1, 4}, {2, 0}, {2, 2}, {2, 4}, {3, 0}, {3, 4}};
	private static final int[][] LETTER_F = new int[][] {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {1, 0}, {1, 2}, {2, 0}, {2, 2}, {3, 0}};
	private static final int[][] LETTER_G = new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 0}, {1, 4}, {2, 0}, {2, 2}, {2, 4}, {3, 2}, {3, 3}};
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

	// Punctuation
	private static final int[][] LETTER_EXCLAMATION = new int[][] {{0, 0}, {0, 1}, {0, 2}, {0, 4}};
	private static final int[][] LETTER_APOSTROPHE = new int[][] {{0, 0}};
	private static final int[][] LETTER_POINT = new int[][] {{0, 4}};
	private static final int[][] LETTER_INTERROGATION = new int[][] {{0, 0}, {1, 0}, {1, 2}, {1, 4}, {2, 0}, {2, 1}};
	private static final int[][] LETTER_LESSTHAN = new int[][] {{0, 2}, {1,1}, {1, 3}, {2, 0}, {2, 4}};
	private static final int[][] LETTER_MORETHAN = new int[][] {{0, 0}, {0, 4}, {1, 1}, {1, 3}, {2, 2}};
	private static final int[][] LETTER_PLUS = new int[][] {{0, 2}, {1, 1}, {1, 2}, {1, 3}, {2, 2}};
	private static final int[][] LETTER_MINUS = new int[][] {{0, 2}, {1, 2}};
	private static final int[][] LETTER_EQUAL = new int[][] {{0, 1}, {0, 3}, {1, 1}, {1, 3}, {2, 1}, {2, 3}};
	private static final int[][] LETTER_UNDERSCORE = new int[][] {{0, 4}, {1, 4}, {2, 4}};
	private static final int[][] LETTER_HASHTAG = new int[][] {{0, 1}, {0, 3}, {1, 0}, {1, 1}, {1, 2}, {1, 3}, {1, 4}, {2, 1}, {2, 3}, {3, 0}, {3, 1}, {3, 2}, {3, 3}, {3, 4}, {4, 1}, {4, 3}};
	private static final int[][] LETTER_PERCENT = new int[][] {{0, 0}, {0, 3}, {0, 4}, {1, 2}, {2, 0}, {2, 1}, {2, 4}};
	private static final int[][] LETTER_SLASH = new int[][] {{0, 4}, {1, 3}, {1, 2}, {1, 1}, {2, 0}};
	private static final int[][] LETTER_FLOATINGPOINT = new int[][] {{0, 0}, {0, 3}, {0, 4}};
	private static final int[][] LETTER_COLUMN = new int[][] {{0, 2}, {0, 4}};
	private static final int[][] LETTER_OPENBRACKET = new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 0}, {1, 4}};
	private static final int[][] LETTER_CLOSEDBRACKET = new int[][] {{1, 1}, {1, 2}, {1, 3}, {0, 0}, {0, 4}};


	// Numbers
	private static final int[][] LETTER_1 = new int[][] {{0, 1}, {1, 0}, {1, 1}, {1, 2}, {1, 3}, {1, 4}};
	private static final int[][] LETTER_2 = new int[][] {{0, 0}, {0, 4}, {1, 0}, {1, 3}, {1, 4}, {2, 0}, {2, 2}, {2, 4}, {3, 1}, {3, 4}};
	private static final int[][] LETTER_3 = new int[][] {{0, 0}, {0, 4}, {1, 0}, {1, 2}, {1, 4}, {2, 0}, {2, 2}, {2, 4}, {3, 1}, {3, 3}};
	private static final int[][] LETTER_4 = new int[][] {{0, 2}, {1, 1}, {1, 3}, {2, 0}, {2, 3}, {3, 0}, {3, 1}, {3, 2}, {3, 3}, {3, 4}};
	private static final int[][] LETTER_5 = new int[][] {{0, 1}, {0, 2}, {0, 4}, {1, 0}, {1, 2}, {1, 4}, {2, 0}, {2, 2}, {2, 4}, {3, 0}, {3, 3}};
	private static final int[][] LETTER_6 = new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 0}, {1, 2}, {1, 4}, {2, 0}, {2, 2}, {2, 4}, {3, 3}};
	private static final int[][] LETTER_7 = new int[][] {{0, 0}, {1, 0}, {1, 3}, {1, 4}, {2, 0}, {2, 2}, {3, 0}, {3, 1}};
	private static final int[][] LETTER_8 = new int[][] {{0, 1}, {0, 3}, {1, 0}, {1, 2}, {1, 4}, {2, 0}, {2, 2}, {2, 4}, {3, 1}, {3, 3}};
	private static final int[][] LETTER_9 = new int[][] {{0, 1}, {1, 0}, {1, 2}, {1, 4}, {2, 0}, {2, 2}, {2, 4}, {3, 1}, {3, 2}, {3, 3}};
	private static final int[][] LETTER_0 = new int[][] {{0, 1}, {0, 2}, {0, 3}, {1, 0}, {1, 4}, {2, 0}, {2, 4}, {3, 1}, {3, 2}, {3, 3}};


	// Singleton instance
	private static AlphabetGeometry instance = null;


	/**
	 * Create instance and assign character to its letter schema
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

		ALPHABET.put('!', getLetterAsLetterPoints(LETTER_EXCLAMATION));
		ALPHABET.put('\'', getLetterAsLetterPoints(LETTER_APOSTROPHE));
		ALPHABET.put('.', getLetterAsLetterPoints(LETTER_POINT));
		ALPHABET.put('?', getLetterAsLetterPoints(LETTER_INTERROGATION));
		ALPHABET.put('<', getLetterAsLetterPoints(LETTER_LESSTHAN));
		ALPHABET.put('>', getLetterAsLetterPoints(LETTER_MORETHAN));
		ALPHABET.put('+', getLetterAsLetterPoints(LETTER_PLUS));
		ALPHABET.put('-', getLetterAsLetterPoints(LETTER_MINUS));
		ALPHABET.put('=', getLetterAsLetterPoints(LETTER_EQUAL));
		ALPHABET.put('_', getLetterAsLetterPoints(LETTER_UNDERSCORE));
		ALPHABET.put('#', getLetterAsLetterPoints(LETTER_HASHTAG));
		ALPHABET.put('%', getLetterAsLetterPoints(LETTER_PERCENT));
		ALPHABET.put('/', getLetterAsLetterPoints(LETTER_SLASH));
		ALPHABET.put(';', getLetterAsLetterPoints(LETTER_FLOATINGPOINT));
		ALPHABET.put(':', getLetterAsLetterPoints(LETTER_COLUMN));
		ALPHABET.put('(', getLetterAsLetterPoints(LETTER_OPENBRACKET));
		ALPHABET.put(')', getLetterAsLetterPoints(LETTER_CLOSEDBRACKET));


		ALPHABET.put('1', getLetterAsLetterPoints(LETTER_1));
		ALPHABET.put('2', getLetterAsLetterPoints(LETTER_2));
		ALPHABET.put('3', getLetterAsLetterPoints(LETTER_3));
		ALPHABET.put('4', getLetterAsLetterPoints(LETTER_4));
		ALPHABET.put('5', getLetterAsLetterPoints(LETTER_5));
		ALPHABET.put('6', getLetterAsLetterPoints(LETTER_6));
		ALPHABET.put('7', getLetterAsLetterPoints(LETTER_7));
		ALPHABET.put('8', getLetterAsLetterPoints(LETTER_8));
		ALPHABET.put('9', getLetterAsLetterPoints(LETTER_9));
		ALPHABET.put('0', getLetterAsLetterPoints(LETTER_0));
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
	 * @param letter, array of coordinates (x, y) on a grid of 4 x 5 or 5 x 5
	 * @return a list of LetterPoint, projection of each coordinate on a grid of 81px height,
	 * spaced with a width LETTER_WIDTH and a height LETTER_HEIGHT
	 */
	private List<LetterPoint> getLetterAsLetterPoints(int[][] letter) {
		List<LetterPoint> points = new ArrayList<LetterPoint>(letter.length);

		for(int[] coord: letter) {
			points.add(new LetterPoint(coord[0] * LETTER_PIXEL_WIDTH, coord[1] * LETTER_PIXEL_HEIGHT));
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
