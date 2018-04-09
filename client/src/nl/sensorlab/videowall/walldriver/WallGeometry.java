package nl.sensorlab.videowall.walldriver;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.stream.IntStream;

/**
 * Class that contains the wall geometry used for CanvasAnimation and
 * the animation previews.
 */
public class WallGeometry {
	
	// Real world measurements in cm
	private static final int PIXEL_Y_COUNT = 81;	
	private static final int[] PANEL_WIDTH_CM = {52, 64, 63, 60, 60, 60, 62, 62, 63, 64, 65, 64, 67}; // Panel width (space between two horizontal pixels)
	private static final int BEAM_WIDTH_CM = 4;				// Width of a beam (between panels)
	private static final int PIXEL_PITCH_CM = 3;			// Space between two vertical pixels
	
	// Calculated outer geometry in cm
	private static final int WALL_WIDTH_CM = IntStream.of(PANEL_WIDTH_CM).sum() + (PANEL_WIDTH_CM.length + 1) * BEAM_WIDTH_CM;	// Width of wall
	private static final int WALL_HEIGHT_CM = PIXEL_Y_COUNT * PIXEL_PITCH_CM; 	// Height of wall 
	
	// Singleton instance
	private static WallGeometry instance = null; 
	
	// Computed wall geometry, based on wall constants
	private Rectangle wallGeometry;			// Complete wall, including offsets, in cm
	private Rectangle[] panelGeometries;	// Separate panels, including offsets, in cm
	private Rectangle[] beamGeometries;		// Separate beams, in cm
	private Point[] pixelCoordinates;		// Pixel locations in cm using screen coordinates. Order of list is rows: top to bottom, colums: left to right, starting top left
	
	/**
	 * Create instance and calculate geometries
	 * based on real world measurements
	 */
	protected WallGeometry() {
		
		// Construct wall geometry
		wallGeometry = new Rectangle(0, 0, WALL_WIDTH_CM, WALL_HEIGHT_CM);
		
		// Construct panel geometries
		panelGeometries = new Rectangle[PANEL_WIDTH_CM.length];
		int currentX = BEAM_WIDTH_CM;
		for (int i = 0; i < panelGeometries.length; i++) {
			
			int panelWidth = PANEL_WIDTH_CM[i];
			panelGeometries[i] = new Rectangle(currentX, 0, panelWidth, WALL_HEIGHT_CM);
			
			// Update x
			currentX += panelWidth + BEAM_WIDTH_CM;
			
		}
		
		// Construct beam geometries
		beamGeometries = new Rectangle[PANEL_WIDTH_CM.length + 1];
		currentX = 0;
		for (int i = 0; i < beamGeometries.length; i++) {
			
			beamGeometries[i] = new Rectangle(currentX, 0, BEAM_WIDTH_CM, WALL_HEIGHT_CM);
			
			// Update x by adding the beam width and the next panel width
			if (i < beamGeometries.length - 1) {
				currentX += BEAM_WIDTH_CM + PANEL_WIDTH_CM[i];
			}
			
		}
		
		// List pixel coordinates
		pixelCoordinates = new Point[getPanelCount() * 2 * PIXEL_Y_COUNT];
		int pixelCoordinateIndex = 0;
		int y = 0;
		for (int yIndex = 0; yIndex < PIXEL_Y_COUNT; yIndex++) {
			for (int panelIndex = 0; panelIndex < panelGeometries.length; panelIndex++) {
				Rectangle panelGeometry = panelGeometries[panelIndex];
				pixelCoordinates[pixelCoordinateIndex++] = new Point(panelGeometry.x + 1, y); // Left pixel in panel
				pixelCoordinates[pixelCoordinateIndex++] = new Point(panelGeometry.x + panelGeometry.width - 2, y); // Right pixel in panel
			}
			y += PIXEL_PITCH_CM;
		}
		
	}
	
	/**
	 * Get singleton instance
	 * @return
	 */
	public static WallGeometry getInstance() {
		if (instance == null) {
			instance = new WallGeometry();
		}
		return instance;
	}

	/**
	 * Get wall geometry in cm, in screen coordinates (top left is 0,0)
	 * @return
	 */
	public Rectangle getWallGeometry() {
		return wallGeometry;
	}
	
	/**
	 * Get number of panels
	 * @return
	 */
	public int getPanelCount() {
		return panelGeometries.length;
	}
	
	/**
	 * Get panel geometry in cm, in screen coordinates (top left is 0,0)
	 * @return
	 */
	public Rectangle getPanelGeometry(int index) {
		return panelGeometries[index];
	}
	
	/**
	 * Get number of beams
	 * @return
	 */
	public int getBeamCount() {
		return beamGeometries.length;
	}
	
	/**
	 * Get beam geometry in cm, in screen coordinates (top left is 0,0)
	 * @return
	 */
	public Rectangle getBeamGeometry(int index) {
		return beamGeometries[index];
	}
	
	/**
	 * Get number of pixels
	 * @return
	 */
	public int getPixelCount() {
		return pixelCoordinates.length;
	}
	
	/**
	 * Get pixel geometry in cm, in screen coordinates (top left is 0,0)
	 * @return
	 */
	public Point getPixelCoordinates(int index) {
		return pixelCoordinates[index];
	}
	
	/**
	 * Scale given rect by scale, creating a Rectangle.Float. Can be used
	 * to create a canvas render, by settings scale to a cm to pixel ratio.
	 * 
	 * @param rect
	 * @param scale
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Rectangle.Float scaleRectangle(Rectangle rect, float scale) {
		return new Rectangle.Float(
			(float) rect.x * scale,
			(float) rect.y * scale,
			(float) rect.width * scale,
			(float) rect.height * scale
		);
	}
	
	/**
	 * Scale given rect by scale, rounded to nearest integer. Can be used
	 * to create a canvas render, by settings scale to a cm to pixel ratio.
	 * 
	 * @param rect
	 * @param scale
	 * @return
	 */
	public static Rectangle scaleRectangleRounded(Rectangle rect, float scale) {
		return new Rectangle(
			Math.round((float) rect.x * scale),
			Math.round((float) rect.y * scale),
			Math.round((float) rect.width * scale),
			Math.round((float) rect.height * scale)
		);
	}
	
	/**
	 * Scale given point by scale, creating a Point.Float. Can be used
	 * to create a canvas render, by settings scale to a cm to pixel ratio.
	 * 
	 * @param rect
	 * @param scale
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Point.Float scalePoint(Point point, float scale) {
		return new Point.Float((float) point.x * scale,	(float) point.y * scale);
	}
	
	/**
	 * Scale given point by scale, rounded to nearest integer. Can be used
	 * to create a canvas render, by settings scale to a cm to pixel ratio.
	 * 
	 * @param rect
	 * @param scale
	 * @return
	 */
	public static Point scalePointRounded(Point point, float scale) {
		return new Point(Math.round((float) point.x * scale), Math.round((float) point.y * scale));
	}
	
	public static int getPixelYCount() {
		return PIXEL_Y_COUNT;
	}
}
