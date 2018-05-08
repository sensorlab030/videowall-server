package nl.sensorlab.videowall.animation.baseanimations.horizontalwaves;
import java.util.ArrayList;
import com.cleverfranke.util.PColor;
import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;

public class HorizontalWavesAnimation extends BaseAnimation {
	
	// Examples based on: https://www.openprocessing.org/sketch/133048 and https://www.openprocessing.org/sketch/500317
	PApplet parent;

	// Settings
	private static final int[] WAVE_COLORS = {PColor.color(7, 93, 144), PColor.color(24, 147, 196), PColor.color(108, 208, 198), PColor.color(235, 232, 225)};
	private static final int AMOUNT_OF_WAVES = 3;
	private ArrayList<HorizontalWave> waves;

	public HorizontalWavesAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		this.waves = new ArrayList<HorizontalWave>();

		// Create the waves
		generateWaves(AMOUNT_OF_WAVES);
	}

	protected void generateWaves(int amountOfWaves) {
		for(int i = 0; i < amountOfWaves; i++) {
			// Set random color
			int color = WAVE_COLORS[(int)Math.floor(Math.random()*WAVE_COLORS.length)];
			
			// How much can the line deviate vertically
			float variation = (float) (0.25f + Math.random() * 0.35f);
			
			// How quickly the wave moves vertically 
			float speed =  (float) (0.0025f + Math.random() * 0.0075f);

			waves.add(new HorizontalWave(this, variation, speed, color));
		}
	}

	protected void drawWaves(PGraphics g) {
		for(HorizontalWave wave : waves) {
			wave.draw(g);
		}
	}

	protected void drawAnimationFrame(PGraphics g, double dt) {
		// Add some fade effect
		g.fill(0, 40);
		g.noStroke();
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);

		// Draw the waves
		drawWaves(g);
	}
}
