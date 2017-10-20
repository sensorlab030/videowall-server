
package com.cleverfranke.ledwall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;
import java.util.concurrent.ThreadLocalRandom;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.looksgood.ani.*;

public class LineFlowAnimation extends Animation{
	private float DURATION = 5;
	private List<Line> lines = new ArrayList<>();
	private List<Boolean> LinesDoneDrawing = new ArrayList<>();
	private int repeatCount = 2;
	
	public class Line {
		// Coordinates
		private int start;
		private int end;
		private int startx1;
		private int startx2;
		private int finalx1;
		private int finalx2;
		private int y1;
		
		// Attributes
		private int color;
		private int index;
		private float delay;
		
		// Animations
		Ani aniX1;
		Ani aniX2;
				
		
		// Constructor
		public Line(int y1, int start, int end, float delay, int index) {
			this.start = start;
			this.end = end;
			this.y1 = y1;
			this.delay = delay;
			this.index = index;
			
			this.color = this.setColor();
			this.setCoordinates();
			this.aniX1 = this.setAniX1();	
			this.aniX2 = this.setAniX2();
		}
		
		
		public int setColor() {
			int color = generateRandomRGBColor();
			return color;
		}
		
		
		/**
		 *  Calculate start and final x coordinates 
		 *  Start X coordinates are offset to the left of the canvas so that they are hidden before the animation
		 */
		public void setCoordinates() {
			double coin = Math.random();
			// X coordinates are offset of at least the size of the canvas to the left, at most twice the size of the canvas to the left
			startx1 = (int) (start - (coin + 1) * WallConfiguration.COLUMNS_COUNT);
			startx2 = (int) ((end - 1) - (coin + 1) * WallConfiguration.COLUMNS_COUNT);
			
			// Final X coordinates are offset of at least the size of the canvas to the right, at most twice the size of the canvas to the right
			finalx1 = (int) (start + (coin + 1) * WallConfiguration.COLUMNS_COUNT);
			finalx2 = (int) ((end - 1) + (coin + 1) * WallConfiguration.COLUMNS_COUNT);
		}
		
		
		/**
		 * Animation of the x1 coordinates of the lines
		 * @return animation
		 */
		public Ani setAniX1(){
			Ani animation = new Ani(this, DURATION, this.delay, "startx1", finalx1, Ani.LINEAR);
			animation.start();
			return animation;
		}
		
		
		/**
		 * Animation of the x2 coordinates of the lines
		 * @return animation
		 */
		public Ani setAniX2(){
			Ani animation = new Ani(this, DURATION, this.delay, "startx2", finalx2, Ani.LINEAR);
			animation.start();
			return animation;
		}
		
		
		public void drawLine(PGraphics g) {
			g.stroke(color);
			g.line(startx1, y1, startx2, y1);
			
			// If both animation ended, change done flag to true
			if (aniX1.isEnded() && aniX2.isEnded()) {
				LinesDoneDrawing.set(index, true);
			}
		}
	}
		
	
	public LineFlowAnimation(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, true, applet);
		this.applet = applet;
				
		// Generate particle lines
		generateLine();
	}
	
	
	/**
	 * Generate a random number of lines on a random number of rows
	 */
	private void generateLine() {
		// Get width of each panels sides
		int nbColumns = WallConfiguration.COLUMNS_COUNT;
		int minSpace = 1;
		int index = 0;
		
		// For each row, randomly decide to draw or not lines on the row
		for (int i = 3; i < WallConfiguration.ROWS_COUNT; i++){	
			if (Math.random() > 0.6) {
				int start = 0;																	// Start panel index
				int nbLines = ThreadLocalRandom.current().nextInt(1, 3 + 1); 					// Get a random number of lines between 0 and 3
				int minSize = 1; 																// Minimum possible size of a line			
				int maxSize = nbColumns - (nbLines - 1) * minSpace - (nbLines - 1) * minSize;  	// Maximum possible size of a line, counting the spaces between the lines and the minimum size of a line
				float delay = (float) Math.random() * DURATION;									// Delay of the lines on the row
				
				// For each line, get the panel index where it starts and where it ends
				for (int j = nbLines - 1; j >= 0; j--) {
					int end = ThreadLocalRandom.current().nextInt(minSize, maxSize); 			// Get random end index in the range of the available line sizes
					
					// Add the line the list of lines
					this.lines.add(new Line(i, start, start + end, delay, index));
					// Set the animation done flag to false
					this.LinesDoneDrawing.add(false);
					
					// Update the values for next line in the row
					start = start + end + 1;
					index++;
					maxSize = nbColumns - start - (j - 1) * minSpace - (j - 1) * minSize;
				}
			}
			
		}
		
		
	}

	
	/**
	 * When all the line animations are done, generate new lines and renew the animation
	 */
	public void isDoneDrawing() {
		repeatCount--;
		lines.clear();
		LinesDoneDrawing.clear();
		generateLine();
	}
	
	
	@Override
	public void drawAnimationFrame(PGraphics g) {
		g.background(255);
		g.strokeWeight(1);
		
		for (Line line: lines){
			line.drawLine(g);
		}
		
		if (areAllTrue(LinesDoneDrawing)) { isDoneDrawing(); }
	}
	
	
	public boolean isDone() {
		return (repeatCount == 0);
	}
	
	@Override
	public void prepareForQueueRotation() {
		isDoneDrawing();
		repeatCount = 2;
	}

}