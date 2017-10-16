
package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.event.KeyEvent;
import de.looksgood.ani.*;

public class ChestBoardAnimation extends Animation{
	int nbSquares;
	AniSequence seq1;
	AniSequence seq2;
	float xWidth[] = getPixelWidthsOfPanels();;
	float yPos[][] = new float[WallConfiguration.PANEL_COUNT][WallConfiguration.PANEL_COUNT];
	int stateManager;
	ChestBoard chestboard;
	
	public class Square {
		private int i;
		private float yPos;
		private int color;
		
		// Constructor
		public Square(int i, float yPos) {
			this.i = i;
			this.yPos = yPos;
			this.color = generateRandomRGBColor();
		}
		
		public void drawSquare(){
			ChestBoardAnimation.drawSquare(this.i, this.yPos, this.color);
		}
	}
	
	public class ChestBoard {
		private int leftPanelIndex;
		private int rightPanelIndex;
		private int stateManager;
		private Square[][] squares; // [Panel ID][Y coordinates of top left corner]
		private int nbSquares;
		Ani aniChestBoard;
		
		// Constructor
		public ChestBoard(int nbSquares, int leftPanelIndex, int rightPanelIndex, int stateManager) {
			this.nbSquares = nbSquares;
			this.leftPanelIndex = leftPanelIndex;
			this.rightPanelIndex = rightPanelIndex;
			this.setSquares();
			this.stateManager = stateManager;
		}
		
		/**
		 * Stores the y position of each squares for each panel (except the two extreme panels)
		 * @param xWidth: An array containing the width of each panel
		 */
		public void setSquares(){
			this.squares = new Square[WallConfiguration.PANEL_COUNT][this.nbSquares];
			
			for (int i = leftPanelIndex; i < rightPanelIndex; i++) {
				for (int j = 0; j < this.nbSquares; j++) {
					this.squares[i][j] = new Square(i, xWidth[i] * j);
				}
			}
			
		}
		
		public void drawSquare(int i, int j){
			Square square = this.squares[i][j];
			square.drawSquare();
		}
		
		public void setAniChestBoard(){
			//** ANIMATION NOT WORKING ! **//
			this.aniChestBoard = new Ani(this, (float) 2, "stateManager", 5, Ani.LINEAR); 
			Ani.to(this, (float) 2, "stateManager", 1, Ani.LINEAR);
			Ani.to(this, (float) 2, "stateManager", 5, Ani.LINEAR);
			
			this.aniChestBoard.start();
		}
		
	}
	
	
	public ChestBoardAnimation(PApplet applet) {
		super(applet);
		this.applet = applet;
		
		// Calculate number of squares per panel
		int nbSquares = (int) Math.floor(WallConfiguration.SOURCE_IMG_HEIGHT / xWidth[1]);
		
		// Create ChestBoard
		chestboard = new ChestBoard(nbSquares, 1, WallConfiguration.PANEL_COUNT-1, 1);
		
		Ani.init(applet);
	}
	

	public void keyEvent(KeyEvent event) {
		char key = event.getKey();
		if (key == 's' || key == 'S') {
			System.out.println("pressed");
			// start the whole seq1uence
			  seq1.start();
		}
	}
	
	@Override
	protected void drawAnimationFrame(PGraphics g) {
		g.smooth();
		g.background(255);

		// Draw all squares chest board
		for (int i = chestboard.leftPanelIndex; i < chestboard.rightPanelIndex; i++) {
			for (int j = 0; j < chestboard.nbSquares; j++) {	
				//chestboard.drawSquare(i, j);
				 switch (chestboard.stateManager) {
		            case 0:
		            	if (i%2 == 0 && j%2 ==0) {
		            		chestboard.drawSquare(i, j);
		            	}
                     	break;
		            case 1:
		            	if (i%2 == 0 && j%2 != 0) {
		            		chestboard.drawSquare(i, j);
		            	}
                     	break;
		            case 2:
		            	if (i%2 != 0 && j%2 != 0) {
		            		chestboard.drawSquare(i, j);
		            	}
                     	break;
		            case 3:
		            	if (i%2 != 0 && j%2 == 0) {
		            		chestboard.drawSquare(i, j);
		            	}
                     	break;
		            case 4:
		            	if (i%2 != 0 && j%2 == 0) {
		            		chestboard.drawSquare(i, j);
		            	}
		            	
		            	if (i%2 == 0 && j%2 != 0) {
		            		chestboard.drawSquare(i, j);
		            	}
                     	break;
		            case 5:
		            	if (i%2 == 0 && j%2 == 0) {
		            		chestboard.drawSquare(i, j);
		            	}
		            	
		            	if (i%2 != 0 && j%2 != 0) {
		            		chestboard.drawSquare(i, j);
		            	}
                     	break;
		            default:
		            	System.out.println("In switch default");
		            	break;
		        }
				
			}
		}
		
//		if (seq1.isEnded()) {
//			seq1.start();
//		}
	}

}
