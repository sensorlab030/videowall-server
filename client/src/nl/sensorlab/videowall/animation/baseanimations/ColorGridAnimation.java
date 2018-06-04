package nl.sensorlab.videowall.animation.baseanimations;

import java.util.ArrayList;
import com.cleverfranke.util.PColor;
import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class ColorGridAnimation extends BaseAnimation {

	private static final int AMOUNT_SQUARES = 16;
	private static final int SQUARE_DIMENSIONS = 4;
	private 	ArrayList<Square> squares;

	public ColorGridAnimation(PApplet applet) {
		super(applet);
		this.squares = new ArrayList<Square>();
		generateSquares();
	}

	private void generateSquares() {
		for(int x = 0; x < AMOUNT_SQUARES; x+=SQUARE_DIMENSIONS) {
			squares.add(new Square());
		}
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double dt) {
		// Add some fade effect;
		g.noStroke();
		g.fill(0, 1);
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
		
		// Update and draw the squares
		for(Square sq : squares) {
			sq.updateAndDraw(g, dt);
		}
	}

	public class Square {

		private PVector position;

		public Square() {
			this.position = new PVector(BaseAnimation.PIXEL_RESOLUTION_X/2, BaseAnimation.PIXEL_RESOLUTION_Y/2);
		}

		void updateAndDraw(PGraphics g) {
			// Keep the squares within the bounding-box
			if((position.x < 0 || position.x + SQUARE_DIMENSIONS > BaseAnimation.PIXEL_RESOLUTION_X) || (position.y < 0 || position.y + SQUARE_DIMENSIONS > BaseAnimation.PIXEL_RESOLUTION_Y)) {
				// Reset the position to the center
				position.x = BaseAnimation.PIXEL_RESOLUTION_X/2;
				position.y = BaseAnimation.PIXEL_RESOLUTION_Y/2;
			}else {
				// Move into a random position by adding a random offset
				position.x += Math.round(-SQUARE_DIMENSIONS + (Math.random() * SQUARE_DIMENSIONS * 2));
				position.y += Math.round(-SQUARE_DIMENSIONS + (Math.random() * SQUARE_DIMENSIONS * 2));
			}

			// Set the color based on the position of the square
			int red = (int)Math.abs(PApplet.map(position.y, 0, BaseAnimation.PIXEL_RESOLUTION_Y, 0, 255));
			int green = (int)Math.abs(PApplet.map(position.x, 0, BaseAnimation.PIXEL_RESOLUTION_X, 0, 255));

			// Set the color
			int color = PColor.color(red, green, 200);

			// Draw
			g.noStroke();
			g.fill(color, 75);
			g.rect(position.x,  position.y,  SQUARE_DIMENSIONS, SQUARE_DIMENSIONS);
		}
	}
}
