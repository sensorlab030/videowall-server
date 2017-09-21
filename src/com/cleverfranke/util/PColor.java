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
		return color(grey * 255f, a * 255f);
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
		return color(r * 255f, g * 255f, b * 255f, a * 255f);
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

}

