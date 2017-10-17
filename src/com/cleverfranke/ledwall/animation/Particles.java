
package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import de.looksgood.ani.*;

public class Particles extends Animation{
	float xWidth[] = new float[WallConfiguration.PANEL_COUNT];
	int nbRows;
	ParticleLine[] particles;
	
	public class ParticleLine {
		// Fields
		private int index;
		private float x1;
		private float x2;
		private float y1;
		private int color[] = new int[3];
		private int alpha;
		Ani aniX1;
		Ani aniX2;
		
		// Constructor
		public ParticleLine(float y1, int index) {
			this.index = index;
			this.y1 = y1;
			this.color = this.setColor();
			this.alpha = 255;
			this.aniX1 = this.setAniX1();	
			this.aniX2 = this.setAniX2();
			this.setCoordinates();
		}
		
		public int[] setColor() {
			int[] color = new int[3];
			switch (this.index % 3) {
			case 0:
				color[0] = 255;
				color[1] = 255;
				color[2] = 0;
				break;
			case 1:
				color[0] = 0;
				color[1] = 255;
				color[2] = 255;
				break;
			case 2:
				color[0] = 255;
				color[1] = 0;
				color[2] = 255;
				break;
									
			}
			return color;
		}
		
		public void setCoordinates() {
			double coin = Math.random();

			if (coin > 0.5) {
				this.x1 = (float) Math.random() * (WallConfiguration.SOURCE_IMG_WIDTH) - WallConfiguration.SOURCE_IMG_WIDTH;
				this.x2 = (float) (this.x1 + Math.random() * (WallConfiguration.SOURCE_IMG_WIDTH-this.x1) - WallConfiguration.SOURCE_IMG_WIDTH);
			} else {
				this.x1 = 0;
				this.x2 = 0;
			}
		}
		
		public Ani setAniX1(){
			float finalx1 = this.x1 + 3*WallConfiguration.SOURCE_IMG_WIDTH;
			float duration = (float) (this.index * Math.random());
			Ani animation = new Ani(this, (float) duration, (float) 0.1, "x1", finalx1, Ani.LINEAR);
			Ani.to(this, (float) 0, duration, "x1", this.x1, Ani.LINEAR);
			animation.repeat(3);
			animation.start();
			return animation;
		}
		
		public Ani setAniX2(){
			float finalx2 = this.x2 + 3*WallConfiguration.SOURCE_IMG_WIDTH;
			Ani animation = new Ani(this, (float) 2, (float) 0.1, "x2", finalx2, Ani.LINEAR);
			Ani.to(this, (float) 0, 2, "x2", this.x2, Ani.LINEAR);
			animation.repeat(3);
			animation.start();
			return animation;
		}

		public void drawLine(PGraphics g) {
			g.stroke(PColor.color(color[0], color[1], color[2], alpha));
			g.line(x1, y1, x2, y1);
		}
		
		public void randomize(){
			System.out.println("Coucou");
		}
	}
		
	public Particles(boolean inDefaultRotation, PApplet applet) {
		super(15, inDefaultRotation, applet);
		this.applet = applet;
		
		// Get width of each panels
		xWidth = getPixelWidthsOfPanels();
		
		// Get total number of rows and their height
		nbRows = (int) Math.floor(WallConfiguration.PHYSICAL_WALL_HEIGHT_CM / WallConfiguration.PHYSICAL_PIXEL_PITCH_CM);
		int rowHeight = (int) Math.floor(WallConfiguration.SOURCE_IMG_HEIGHT / nbRows);
		
		Ani.init(applet);
		
		// Initialize particles list
		particles = new ParticleLine[nbRows]; 
		
		// Create one line per row
		for (int i=0; i<nbRows; i++){
			ParticleLine line = new ParticleLine(i*rowHeight, i);
			particles[i] = line;
		}

	}
	
	
	@Override
	public void drawAnimationFrame(PGraphics g) {
		g.background(255);
		g.strokeWeight(4);
		
		for (int i=0; i<nbRows; i++){
			particles[i].drawLine(g);
		}
	}

}
