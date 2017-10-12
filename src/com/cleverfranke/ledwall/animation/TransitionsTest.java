
package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.event.KeyEvent;
import de.looksgood.ani.*;

public class TransitionsTest extends Animation{
	int nbSquares;
	AniSequence seq;
	float xWidth[] = new float[WallConfiguration.PANEL_COUNT];
	float yPos[][] = new float[WallConfiguration.PANEL_COUNT][WallConfiguration.PANEL_COUNT];
	int stateManager;
	
	/**
	 * Stores the y position of each squares for each panel (except the two extreme panels)
	 * @param xWidth: An array containing the width of each panel
	 */
	public float[][] getYCoordSquares(float[] xWidth) {
		float yPos[][] = new float[WallConfiguration.PANEL_COUNT][WallConfiguration.PANEL_COUNT];
		
		for (int i = 1; i < WallConfiguration.PANEL_COUNT-1; i++) {
			nbSquares = (int) Math.floor(WallConfiguration.SOURCE_IMG_HEIGHT / xWidth[i]);
			for (int j = 0; j< nbSquares; j++) {
				yPos[i][j] = xWidth[i] * j;
			}
		}
		
		return yPos;
	}
	
	public TransitionsTest(PApplet applet) {
		super(applet);
		this.applet = applet;
		
		xWidth = getPixelWidthsOfPanels();
		yPos = getYCoordSquares(xWidth);
		
		
		Ani.init(applet);
		
		seq = new AniSequence(applet);
		seq.beginSequence();
		
		// step 0
		seq.add(Ani.to(this, (float) 1, "stateManager:4"));
		
		// step 1
		seq.add(Ani.to(this, (float) 0.5, "stateManager:1"));
		  
		// step 2
		seq.add(Ani.to(this, (float) 0.5, "stateManager:5"));
	
		// step 3
		seq.add(Ani.to(this, (float) 0.5, "stateManager:3"));
		
		// step 4 
		seq.add(Ani.to(this, (float) 0.5, "stateManager:0"));
		
		// step 4 
		seq.add(Ani.to(this, (float) 0.5, "stateManager:4"));	

		seq.add(Ani.to(this, (float) 0.5, "stateManager:2"));	
		
		seq.endSequence();
		seq.start();
		  
	}
	

	public void keyEvent(KeyEvent event) {
		char key = event.getKey();
		if (key == 's' || key == 'S') {
			System.out.println("pressed");
			// start the whole sequence
			  seq.start();
		}
	}
	
	@Override
	protected void drawAnimationFrame(PGraphics g) {
		
		g.smooth();
		g.background(255);
		int color = generateRandomRGBColor();
		g.fill(color);
		
		// Draw all squares chest board
		for (int i = 1; i < WallConfiguration.PANEL_COUNT-1; i++) {
			for (int j = 0; j < nbSquares; j++) {				
				 switch (stateManager) {
		            case 0:
		            	if (i%2 == 0 && j%2 ==0) {
		            		drawSquare(i, yPos[i][j]);
		            	}
                     	break;
		            case 1:
		            	if (i%2 == 0 && j%2 != 0) {
		            		drawSquare(i, yPos[i][j]);
		            	}
                     	break;
		            case 2:
		            	if (i%2 != 0 && j%2 != 0) {
		            		drawSquare(i, yPos[i][j]);
		            	}
                     	break;
		            case 3:
		            	if (i%2 != 0 && j%2 == 0) {
		            		drawSquare(i, yPos[i][j]);
		            	}
                     	break;
		            case 4:
		            	if (i%2 != 0 && j%2 == 0) {
		            		drawSquare(i, yPos[i][j]);
		            	}
		            	
		            	if (i%2 == 0 && j%2 != 0) {
		            		drawSquare(i, yPos[i][j]);
		            	}
                     	break;
		            case 5:
		            	if (i%2 == 0 && j%2 == 0) {
		            		drawSquare(i, yPos[i][j]);
		            	}
		            	
		            	if (i%2 != 0 && j%2 != 0) {
		            		drawSquare(i, yPos[i][j]);
		            	}
                     	break;
		            default:
		            	System.out.println("COUCOU");
		            	break;
		        }
				
			}
		}
		
		if (seq.isEnded()) {
			seq.start();
		}
	}

}
