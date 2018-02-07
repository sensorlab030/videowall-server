package com.cleverfranke.util;

/**
 * Class to statically create Processing compatible colors without
 * relying on PGraphics or PApplet
 */
public final class PColor {

	/**
	 * Create grey scale color
	 *
	 * @param grey [0f, 1f]
	 * @return
	 */
	public static int color(float grey) {
		return color(grey * 255f);
	}

	/**
	 * Create grey scale color
	 *
	 * @param grey [0, 255]
	 * @return
	 */
	public static int color(int grey) {
		return color(grey, grey, grey, 255);
	}

	/**
	 * Create grey scale color with alpha
	 *
	 * @param grey [0f, 1f]
	 * @param a [0f, 1f]
	 * @return
	 */
	public static int color(float grey, float a) {
		return color((int) (grey * 255f), (int) (a * 255f));
	}

	/**
	 * Create grey scale color with alpha
	 *
	 * @param grey [0, 255]
	 * @param a [0, 255]
	 * @return
	 */
	public static int color(int grey, int a) {
		return color(grey, grey, grey, a);
	}

	/**
	 * Create color
	 *
	 * @param r [0f, 1f]
	 * @param g [0f, 1f]
	 * @param b [0f, 1f]
	 * @return
	 */
	public static int color(float r, float g, float b) {
		return color(r * 255f, g * 255f, b * 255f);
	}

	/**
	 * Create color
	 *
	 * @param r [0, 255]
	 * @param g [0, 255]
	 * @param b [0, 255]
	 * @return
	 */
	public static int color(int r, int g, int b) {
		return color(r, g, b, 255);
	}

	/**
	 * Create color with alpha
	 *
	 * @param r [0f, 1f]
	 * @param g [0f, 1f]
	 * @param b [0f, 1f]
	 * @param a [0f, 1f]
	 * @return
	 */
	public static int color(float r, float g, float b, float a) {
		return color((int) (r * 255f), (int) (g * 255f), (int) (b * 255f), (int) (a * 255f));
	}

	/**
	 * Create color with alpha
	 *
	 * @param r [0, 255]
	 * @param g [0, 255]
	 * @param b [0, 255]
	 * @param a [0, 255]
	 * @return
	 */
	public static int color(int r, int g, int b, int a) {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	/**
	 * Create color from HEX string
	 *
	 * @param hex #RRGGBB or #RRGGBBAA
	 * @return
	 */
	public static int color(String hex) {

		if (hex.length() == 7) {

			// #RRGGBB
			return color(
					Integer.valueOf(hex.substring(1, 3), 16),
					Integer.valueOf(hex.substring(3, 5), 16),
					Integer.valueOf(hex.substring(5, 7), 16));

		} else if (hex.length() == 9) {

			// #RRGGBBAA
			return color(
				Integer.valueOf(hex.substring(1, 3), 16),
				Integer.valueOf(hex.substring(3, 5), 16),
				Integer.valueOf(hex.substring(5, 7), 16),
				Integer.valueOf(hex.substring(7, 9), 16));

		} else {
			return 0;
		}

	}

	/**
	 * Fetch red component from color
	 *
	 * @param color
	 * @return
	 */
	public static int red(int color) {
		return (color >> 16) & 0xFF;
	}

	/**
	 * Fetch green component from color
	 *
	 * @param color
	 * @return
	 */
	public static int green(int color) {
		return (color >> 8) & 0xFF;
	}

	/**
	 * Fetch blue component from color
	 *
	 * @param color
	 * @return
	 */
	public static int blue(int color) {
		return color& 0xFF;
	}

	/**
	 * Fetch alpha component from color
	 *
	 * @param color
	 * @return
	 */
	public static int alpha(int color) {
		return (color >> 24) & 0xFF;
	}

	/**
	 * Fetch RGB color from HSB
	 *
	 * @param hue 			[0f, 1f]
	 * @param saturation	[0f, 1f]
	 * @param brightness	[0f, 1f]
	 * @return
	 */
	public static int hsb(float hue, float saturation, float brightness) {
		int r = 0, g = 0, b = 0;
		if (saturation == 0) {
			r = g = b = (int) (brightness * 255.0f + 0.5f);
		} else {
			float h = (hue - (float) Math.floor(hue)) * 6.0f;
			float f = h - (float) java.lang.Math.floor(h);
			float p = brightness * (1.0f - saturation);
			float q = brightness * (1.0f - saturation * f);
			float t = brightness * (1.0f - (saturation * (1.0f - f)));
			switch ((int) h) {
			case 0:
				r = (int) (brightness * 255.0f + 0.5f);
				g = (int) (t * 255.0f + 0.5f);
				b = (int) (p * 255.0f + 0.5f);
				break;
			case 1:
				r = (int) (q * 255.0f + 0.5f);
				g = (int) (brightness * 255.0f + 0.5f);
				b = (int) (p * 255.0f + 0.5f);
				break;
			case 2:
				r = (int) (p * 255.0f + 0.5f);
				g = (int) (brightness * 255.0f + 0.5f);
				b = (int) (t * 255.0f + 0.5f);
				break;
			case 3:
				r = (int) (p * 255.0f + 0.5f);
				g = (int) (q * 255.0f + 0.5f);
				b = (int) (brightness * 255.0f + 0.5f);
				break;
			case 4:
				r = (int) (t * 255.0f + 0.5f);
				g = (int) (p * 255.0f + 0.5f);
				b = (int) (brightness * 255.0f + 0.5f);
				break;
			case 5:
				r = (int) (brightness * 255.0f + 0.5f);
				g = (int) (p * 255.0f + 0.5f);
				b = (int) (q * 255.0f + 0.5f);
				break;
			}
		}
		return 0xff000000 | (r << 16) | (g << 8) | (b << 0);
	}

/**
 * Converts the components of a color, as specified by the default RGB
 * model, to an equivalent set of values for hue, saturation, and
 * brightness that are the three components of the HSB model.
 *
 * If the hsbvals argument is null, then a
 * new array is allocated to return the result. Otherwise, the method
 * returns the array hsbvals, with the values put into
 * that array.
 * @param     r   the red component of the color
 * @param     g   the green component of the color
 * @param     b   the blue component of the color
 * @param     hsbvals  the array used to return the
 *                     three HSB values, or null
 * @return    an array of three elements containing the hue, saturation,
 *                     and brightness (in that order), of the color with
 *                     the indicated red, green, and blue components.
 **/
public static float[] RGBtoHSB(int r, int g, int b, float[] hsbvals) {
	float hue, saturation, brightness;
	if (hsbvals == null) {
		hsbvals = new float[3];
	}
	int cmax = (r > g) ? r : g;
	if (b > cmax) cmax = b;
	int cmin = (r < g) ? r : g;
	if (b < cmin) cmin = b;

	brightness = (cmax) / 255.0f;
	if (cmax != 0)
		saturation = ((float) (cmax - cmin)) / ((float) cmax);
	else
		saturation = 0;
	if (saturation == 0)
		hue = 0;
	else {
		float redc = ((float) (cmax - r)) / ((float) (cmax - cmin));
		float greenc = ((float) (cmax - g)) / ((float) (cmax - cmin));
		float bluec = ((float) (cmax - b)) / ((float) (cmax - cmin));
		if (r == cmax)
			hue = bluec - greenc;
		else if (g == cmax)
			hue = 2.0f + redc - bluec;
		else
			hue = 4.0f + greenc - redc;
			hue = hue / 6.0f;
		if (hue < 0)
			hue = hue + 1.0f;
		}
	hsbvals[0] = hue;
	hsbvals[1] = saturation;
	hsbvals[2] = brightness;
	return hsbvals;
	}

/**
 * Fetch RGB color from HSB
 *
 * @param hue 			[0f, 1f]
 * @param saturation	[0f, 1f]
 * @param brightness	[0f, 1f]
 * @return
 */
public static int[] HSBtoRGB(float hue, float saturation, float brightness) {
	int r = 0, g = 0, b = 0;
	if (saturation == 0) {
		r = g = b = (int) (brightness * 255.0f + 0.5f);
	} else {
		float h = (hue - (float) Math.floor(hue)) * 6.0f;
		float f = h - (float) java.lang.Math.floor(h);
		float p = brightness * (1.0f - saturation);
		float q = brightness * (1.0f - saturation * f);
		float t = brightness * (1.0f - (saturation * (1.0f - f)));
		switch ((int) h) {
		case 0:
			r = (int) (brightness * 255.0f + 0.5f);
			g = (int) (t * 255.0f + 0.5f);
			b = (int) (p * 255.0f + 0.5f);
			break;
		case 1:
			r = (int) (q * 255.0f + 0.5f);
			g = (int) (brightness * 255.0f + 0.5f);
			b = (int) (p * 255.0f + 0.5f);
			break;
		case 2:
			r = (int) (p * 255.0f + 0.5f);
			g = (int) (brightness * 255.0f + 0.5f);
			b = (int) (t * 255.0f + 0.5f);
			break;
		case 3:
			r = (int) (p * 255.0f + 0.5f);
			g = (int) (q * 255.0f + 0.5f);
			b = (int) (brightness * 255.0f + 0.5f);
			break;
		case 4:
			r = (int) (t * 255.0f + 0.5f);
			g = (int) (p * 255.0f + 0.5f);
			b = (int) (brightness * 255.0f + 0.5f);
			break;
		case 5:
			r = (int) (brightness * 255.0f + 0.5f);
			g = (int) (p * 255.0f + 0.5f);
			b = (int) (q * 255.0f + 0.5f);
			break;
		}
	}
	int[] rgb =  {r, g, b};
	return rgb;
}


}

