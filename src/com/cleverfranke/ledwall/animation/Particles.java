
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
		private float x1;
		private float x2;
		private float y1;
		private int color[] = new int[3];
		private int alpha;
		Ani diameterAni;
		
		// Constructor
		public ParticleLine(float x1, float x2, float y1) {
			this.x1 = x1;
			this.x2 = x2;
			this.y1 = y1;
			this.color = this.setColor();
			this.alpha = 255;
			
			// diameter animation
		    diameterAni = new Ani(this, (float) 1, "x2", 500, Ani.EXPO_IN_OUT);
		    Ani.to(this, (float) 1.5, "x2", 700, Ani.EXPO_IN_OUT);
		    // repeat yoyo style (go up and down)
		    diameterAni.setPlayMode(Ani.YOYO);
		    // repeat 3 times
		    diameterAni.repeat(3);
		}
		
		public int[] setColor() {
			int[] color = new int[3];
			color[0] = (int) (Math.random() * 255);
			color[1] = (int) (Math.random() * 255);
			color[2] = (int) (Math.random() * 255);
			return color;
		}
		
		public void drawLine(PGraphics g) {
			g.stroke(PColor.color(color[0], color[1], color[2], alpha));
			g.line(x1, y1, x2, y1);
		}
	}
		
	public Particles(PApplet applet) {
		super(applet);
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
			ParticleLine line = new ParticleLine(0, xWidth[0], i*rowHeight);
			particles[i] = line;
		}

	}
	
	
	@Override
	protected void drawAnimationFrame(PGraphics g) {
		g.background(255);
		g.strokeWeight(4);
		
		for (int i=0; i<nbRows; i++){
			particles[i].drawLine(g);
		}
	}

}
