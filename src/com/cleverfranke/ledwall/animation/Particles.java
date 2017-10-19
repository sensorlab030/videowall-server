
package com.cleverfranke.ledwall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;
import java.util.concurrent.ThreadLocalRandom;

import processing.core.PApplet;
import processing.core.PGraphics;
import de.looksgood.ani.*;

public class Particles extends Animation{
	int nbRows;
	List<ParticleLine> particles = new ArrayList<>();
	float xPanelCoord[] = getXCoordOfPanelSides();
	float DURATION = 5;
	List<Boolean> isDoneDrawing = new ArrayList<>();
	
	public class ParticleLine {
		// Fields
		private int start;
		private int end;
		private float startx1;
		private float startx2;
		private float finalx1;
		private float finalx2;
		private float delay;
		private float y1;
		private int color;
		private int index;
		Ani aniX1;
		Ani aniX2;
				
		// Constructor
		public ParticleLine(float y1, int start, int end, float delay, int index) {
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
		
		public void setCoordinates() {
			double coin = Math.random();
			
			this.startx1 = (float) (xPanelCoord[this.start] - (coin + 1) * WallConfiguration.SOURCE_IMG_WIDTH);
			this.startx2 = (float) (xPanelCoord[this.end - 1] - (coin + 1) * WallConfiguration.SOURCE_IMG_WIDTH);
			
			this.finalx1 = (float) (xPanelCoord[this.start] + (coin + 1) * WallConfiguration.SOURCE_IMG_WIDTH);
			this.finalx2 = (float) (xPanelCoord[this.end - 1] + (coin + 1) * WallConfiguration.SOURCE_IMG_WIDTH);
			
			// System.out.println("start: " + this.start + " x1: " + this.x1 + "  x2: " + this.x2 + " y1: " + this.y1);	
//			System.out.println("start: " + this.start + " end: " + this.end);	
		}
		
		public Ani setAniX1(){
			Ani animation = new Ani(this, DURATION, this.delay, "startx1", finalx1, Ani.LINEAR);
			animation.start();
			return animation;
		}
		
		public Ani setAniX2(){
			Ani animation = new Ani(this, DURATION, this.delay, "startx2", finalx2, Ani.LINEAR);
			animation.start();
			return animation;
		}

		public void drawLine(PGraphics g) {
			g.stroke(PColor.color(color));
			g.line(this.startx1, y1, this.startx2, y1);
			
			if (this.aniX1.isEnded() && this.aniX2.isEnded()) {
				isDoneDrawing.set(this.index, true);
//				this.aniX1.start();
//				this.aniX2.start();
			}
		}
	}
		
	public Particles(boolean inDefaultRotation, PApplet applet) {
		super(105, inDefaultRotation, applet);
		this.applet = applet;
				
		// Get total number of rows and their height
		nbRows = (int) Math.floor(WallConfiguration.PHYSICAL_WALL_HEIGHT_CM / WallConfiguration.PHYSICAL_PIXEL_PITCH_CM);
		this.generateParticle();
		
		Ani.init(applet);
	}
	
	public void generateParticle() {
		int rowHeight = (int) Math.floor(WallConfiguration.SOURCE_IMG_HEIGHT / this.nbRows);
		
		// Get width of each panels sides
		int nbColumns = xPanelCoord.length;
		int minSpace = 1;
		int index = 0;
		
		// Create one line per row
		for (int i = 3; i < this.nbRows; i++){
			double coin = Math.random();
			
			if (coin > 0.6) {
				// Get a random number of lines between 0 and 3
				int nbLines = ThreadLocalRandom.current().nextInt(1, 3 + 1);
				int minSize = 1;
				int maxSize = nbColumns - (nbLines - 1) * minSpace - (nbLines - 1) * minSize; // Maximum possible size of a line, counting the spaces between the lines and the minimum size of a line
				int start = 0;
				
				for (int j = nbLines - 1; j >= 0; j--) {
					int end = ThreadLocalRandom.current().nextInt(minSize, maxSize);
					float delay = (float) Math.random() * DURATION;
					ParticleLine line = new ParticleLine(i*rowHeight, start, start + end, delay, index);
					particles.add(line);
					isDoneDrawing.add(false);
					
					// Update for next particle
					start = start + end + 1;
					index++;
					maxSize = nbColumns - start - (j - 1) * minSpace - (j - 1) * minSize;
				}
			}
			
		}
		
		
	}

	public static boolean areAllTrue(List<Boolean> array) {
	    for(Boolean b : array) if(!b) return false;
	    return true;
	}
	
	@Override
	public void drawAnimationFrame(PGraphics g) {
		g.background(255);
		g.strokeWeight(4);
		
		for (ParticleLine particleLine: particles){
			particleLine.drawLine(g);
		}
		
		if (areAllTrue(isDoneDrawing)) {
			this.particles.clear();
			this.isDoneDrawing.clear();
			this.generateParticle();
		}
	}

}
