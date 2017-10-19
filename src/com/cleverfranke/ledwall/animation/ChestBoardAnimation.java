
package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;
import de.looksgood.ani.*;

public class ChestBoardAnimation extends Animation{
	public static final float PANEL_WIDTH[] = getPixelWidthsOfPanels(); // Width of each panel
	ChestBoard chestboard;

	/**
	 * A Square is a class  that represents one square on the chestboard
	 */
	public class Square {
		private int i;		// Panel ID
		private float yPos; // Top left corner position of the square
		private int color;	// Square color
		private int alpha;
		
		public Square(int i, float yPos, int alpha) {
			this.i = i;
			this.yPos = yPos;
			this.alpha = alpha;
			this.color = generateRandomRGBAColor(this.alpha);
		}
		
		public void setColor(int alpha){
			this.color = generateRandomRGBAColor(alpha);
		}
		
		public void drawSquare(PGraphics g, int alpha){
			this.setColor(alpha);
			ChestBoardAnimation.drawSquare(g, this.i, this.yPos, this.color);
		}
	}
	
	/**
	 * A ChestBoard is a collection of squares drawn on several panels of the wall
	 */
	public class ChestBoard {
		private int leftPanelIndex;		// Index of the extreme left panel
		private int rightPanelIndex;	// Index of the extreme right panel
		private int stateManager;		// Change animation depending on its value
		private Square[][] squares; 	// Square collection [Panel ID][Row ID]
		private int nbSquares;			// Total number of squares on the board
		AniSequence aniChestBoard;		// Animation
		
		public ChestBoard(int nbSquares, int leftPanelIndex, int rightPanelIndex, int stateManager) {
			this.nbSquares = nbSquares;
			this.leftPanelIndex = leftPanelIndex;
			this.rightPanelIndex = rightPanelIndex;
			this.stateManager = stateManager;
			this.setSquares();
		}
		
		/**
		 * Stores the y position of each squares for each panel (except the two extreme panels)
		 * @param PANEL_WIDTH: An array containing the width of each panel
		 */
		public void setSquares(){
			this.squares = new Square[WallConfiguration.PANEL_COUNT][this.nbSquares];
			
			for (int i = leftPanelIndex; i < rightPanelIndex; i++) {
				for (int j = 0; j < this.nbSquares; j++) {
					this.squares[i][j] = new Square(i, PANEL_WIDTH[i] * j, 0);
				}
			}
			
		}
		
		public void drawSquare(PGraphics g, int i, int j){
			Square square = this.squares[i][j];
			square.drawSquare(g, 255);
		}
		
		public void setAniChestBoard(){
			this.aniChestBoard.beginSequence();
			
			this.aniChestBoard.add(Ani.to(this, (float) 0, (float) 0, "stateManager", 6));
			this.aniChestBoard.add(Ani.to(this, (float) 0, (float) 1, "stateManager", 4));
			this.aniChestBoard.add(Ani.to(this, (float) 0, (float) 1, "stateManager", 1));
			this.aniChestBoard.add(Ani.to(this, (float) 0, (float) 1, "stateManager", 5));
			this.aniChestBoard.add(Ani.to(this, (float) 0, (float) 1, "stateManager", 2));
			this.aniChestBoard.add(Ani.to(this, (float) 0, (float) 1, "stateManager", 4));
			this.aniChestBoard.add(Ani.to(this, (float) 0, (float) 1, "stateManager", 3));
			this.aniChestBoard.add(Ani.to(this, (float) 0, (float) 1, "stateManager", 5));
			this.aniChestBoard.add(Ani.to(this, (float) 0, (float) 1, "stateManager", 0));
			this.aniChestBoard.add(Ani.to(this, (float) 0, (float) 1, "stateManager", 6));
			
			this.aniChestBoard.endSequence();
			this.aniChestBoard.start();
		}
		
	}
	

	
	public ChestBoardAnimation(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, applet);
		
		// Calculate number of squares per panel
		int nbSquares = (int) Math.floor(WallConfiguration.SOURCE_IMG_HEIGHT / PANEL_WIDTH[1]);
		
		// Start animation library
		Ani.init(applet);
		
		// Create ChestBoard
		chestboard = new ChestBoard(nbSquares, 1, WallConfiguration.PANEL_COUNT-1, 0);
		chestboard.aniChestBoard = new AniSequence(applet);
		chestboard.setAniChestBoard();
	}
	

	@Override
	public void drawAnimationFrame(PGraphics g) {
		g.smooth();
		g.background(255);
		g.noStroke();
		
		// Draw animation depending on stateManager value
		for (int i = chestboard.leftPanelIndex; i < chestboard.rightPanelIndex; i++) {
			for (int j = 0; j < chestboard.nbSquares; j++) {	
				 switch (chestboard.stateManager) {
		            case 0:
		            	if (i%2 == 0 && j%2 ==0) {
		            		chestboard.drawSquare(g, i, j);
		            	}
                     	break;
		            case 1:
		            	if (i%2 == 0 && j%2 != 0) {
		            		chestboard.drawSquare(g, i, j);
		            	}
                     	break;
		            case 2:
		            	if (i%2 != 0 && j%2 != 0) {
		            		chestboard.drawSquare(g, i, j);
		            	}
                     	break;
		            case 3:
		            	if (i%2 != 0 && j%2 == 0) {
		            		chestboard.drawSquare(g, i, j);
		            	}
                     	break;
		            case 4:
		            	if (i%2 != 0 && j%2 == 0) {
		            		chestboard.drawSquare(g, i, j);
		            	}
		            	
		            	if (i%2 == 0 && j%2 != 0) {
		            		chestboard.drawSquare(g, i, j);
		            	}
                     	break;
		            case 5:
		            	if (i%2 == 0 && j%2 == 0) {
		            		chestboard.drawSquare(g, i, j);
		            	}
		            	
		            	if (i%2 != 0 && j%2 != 0) {
		            		chestboard.drawSquare(g, i, j);
		            	}
                     	break;
		            case 6:
		            	chestboard.drawSquare(g, i, j);
                     	break;
		            default:
		            	chestboard.drawSquare(g, i, j);
		            	break;
		        }
				
			}
		}

	}
	
	@Override
	public boolean isDone() {
		System.out.println("Chestboardanimation: " + chestboard.aniChestBoard.isEnded());
		return chestboard.aniChestBoard.isEnded();
	}


	@Override
	public void prepareForQueueRotation() {
		chestboard.aniChestBoard.start();
	}

}
