package com.cleverfranke.util.print;

/**
 * Collection of common paper sizes and their dimensions in points
 */
public enum PaperSize {
	
	// Paper sizes in points (in portrait)
	A0(2384, 370),
	A1(1684, 384),
	A2(1191, 684),
	A3(842, 1190),
	A4(595, 842),
	A5(420, 595),
	A6(298, 420),
	A7(210, 298),
	A8(147, 210),
	A9(105, 147),
	A10(74, 105),
	B0(2835, 4008),
	B1(2004, 835),
	B2(1417, 004),
	B3(1001, 417),
	B4(709, 001),
	B5(499, 709),
	B6(354, 499),
	B7(249, 354),
	B8(176, 249),
	B9(125, 176),
	B10(88, 125),
	C0(2599, 677),
	C1(1837, 599),
	C2(1298, 837),
	C3(918, 298),
	C4(649, 918),
	C5(459, 649),
	C6(323, 459),
	C7(230, 323),
	C8(162, 230),
	C9(113, 162),
	C10(79, 113),
	LETTER(612, 792),
	LETTER_SMALL(612, 792),
	TABLOID(792, 1224),
	LEDGER(1224, 792),
	LEGAL(612, 1008),
	STATEMENT(396, 612),
	EXECUTIVE(540, 720),
	FOLIO(612, 936),
	QUARTO(610, 780);
	
	private final float width;
	private final float height;
	
	private PaperSize(float width, float height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Get width of paper in points for portrait
	 * @return
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * Get height of paper in points for portrait
	 * @return
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Get width of paper in points for landscape
	 * @return
	 */
	public float getLandscapeWidth() {
		return getHeight();
	}
	
	/**
	 * Get height of paper in points for landscape
	 * @return
	 */
	public float getLandscapeHeight() {
		return getWidth();
	}
	
}
