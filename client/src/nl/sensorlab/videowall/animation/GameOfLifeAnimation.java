package nl.sensorlab.videowall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import jonas.Node;
import jonas.Pixel;
import jonas.TriggerObject;
import jonas.TriggerSequence;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class GameOfLifeAnimation extends BaseAnimation {

	public PApplet parent;

	// Based on Processing example: GameOfLife

	public int[][] cells;
	public int[][] cellsPrevious;

	public float probabilityOfAliveAtStart = 20; // Percentage
	public int updateCounter = 0;
	public int updateTrigger = 15;

	public GameOfLifeAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;

		// Set size 2D arrays
		cells = new int[PIXEL_RESOLUTION_X][PIXEL_RESOLUTION_Y];
		cellsPrevious = new int[PIXEL_RESOLUTION_X][PIXEL_RESOLUTION_Y];

		// Generate cells
		generateCells();
		
		// Update cells so one cycle is already calculated
		updateCells();
	}

	public void generateCells() {
		for(int x = 0; x < PIXEL_RESOLUTION_X; x++) {
			for(int y = 0; y < PIXEL_RESOLUTION_Y; y++) {
				// Determine if dead or alive start
				if((Math.random() * 100) > probabilityOfAliveAtStart) { 
					cells[x][y] = 0; // Dead 
				}else {
					cells[x][y] = 1; // Alive
				}
			}
		}
	}

	public void updateCells() {
		// Make a copy of the 2D array; so we can evolve the cell array and check with the previous state
		for(int x = 0; x < PIXEL_RESOLUTION_X; x++) {
			for(int y = 0; y < PIXEL_RESOLUTION_Y; y++) {
				cellsPrevious[x][y] = cells[x][y];
			}
		}

		// Check the amount of neighbors for each cell; evolve the cells
		for(int x = 0; x < PIXEL_RESOLUTION_X; x++) {
			for(int y = 0; y < PIXEL_RESOLUTION_Y; y++) {
				int amountOfNeigbors = 0;
				// Check surrounding X cells
				for(int sx = x - 1; sx <= x + 1; sx++) {
					// Check surrounding Y cells
					for(int sy = y - 1; sy <= y + 1; sy++) {
						// Make sure the sx and sy are within the canvas bounds; and make sure it is ignoring itself
						if((sx >= 0) && (sx < PIXEL_RESOLUTION_X) && (sy >= 0) && (sy < PIXEL_RESOLUTION_Y) && !(x == sx && y == sy)) {
							// If neighbor is alive update state.
							if(cellsPrevious[sx][sy] == 1) amountOfNeigbors++;
						}
					}
				}
				// Check if cells is allowed to stay alive
				if(cellsPrevious[x][y] == 1) {
					if(amountOfNeigbors < 2 || amountOfNeigbors > 3) {
						cells[x][y] = 0;
					}
				}else {
					if(amountOfNeigbors == 3) {
						cells[x][y] = 1;
					}
				}
			}
		}

	}

	public void drawCells(PGraphics g) {
		for(int x = 0; x < PIXEL_RESOLUTION_X; x++) {
			for(int y = 0; y < PIXEL_RESOLUTION_Y; y++) {
				// Only draw when alive.
				if(cells[x][y] == 1) {
					g.noFill();
					g.stroke(255,255,255, 75);
					g.point(x, y);
				}
			}
		}
	}


	@Override
	protected void drawAnimationFrame(PGraphics g) {
		// Add some fade effect
		g.noStroke();
		g.fill(0, 10);
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);

		// Update the cells; check if alive or dead
		if(updateCounter >= updateTrigger) {
			updateCells();
			updateCounter = 0;
		}else {
			updateCounter++;
		}

		// Draw Game of life
		drawCells(g);
	}
}