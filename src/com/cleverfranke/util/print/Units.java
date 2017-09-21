package com.cleverfranke.util.print;

/**
 * Class to convert between points and other units of measure
 */
public class Units {
	
	// Conversion constants
	private static final float MM_PER_PT = 0.3528f;
	private static final float INCH_PER_PT = 0.01389f;
	
	/**
	 * Convert mm to points
	 * 
	 * @param mm
	 * @return
	 */
	public static float mmToPt(float mm) {
		return mm / MM_PER_PT; 
	}
	
	/**
	 * Convert points to mm
	 * 
	 * @param pt
	 * @return
	 */
	public static float ptToMm(float pt) {
		return pt * MM_PER_PT;
	}
	
	/**
	 * Convert inches to points
	 * 
	 * @param inch
	 * @return
	 */
	public static float inchToPt(float inch) {
		return inch / INCH_PER_PT;
	}
	
	/**
	 * Convert points to inches
	 * 
	 * @param pt
	 * @return
	 */
	public static float ptToInch(float pt) {
		return pt * INCH_PER_PT;
	}

}
