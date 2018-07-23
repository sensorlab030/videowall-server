package nl.sensorlab.videowall.animation.canvasanimations;

import java.awt.Rectangle;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseCanvasAnimation;
import nl.sensorlab.videowall.walldriver.WallGeometry;
import processing.core.PApplet;
import processing.core.PGraphics;

public class DebugCanvasAnimation extends BaseCanvasAnimation {

	private int[] panelColors;
	
	public DebugCanvasAnimation(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);
		
		panelColors = new int[3];
		panelColors[0] = PColor.color(255, 0, 0);
		panelColors[1] = PColor.color(0, 255, 0);
		panelColors[2] = PColor.color(0, 0, 255);
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g, double dt) {
		drawPanelBars(g);
	}
	
	private void drawPanelBars(PGraphics g) {
		
		g.noStroke();
		g.background(0);
		
		// Draw panels
		for (int i = 0; i < WallGeometry.getInstance().getPanelCount(); i++) {
			Rectangle r = WallGeometry.scaleRectangleRounded(WallGeometry.getInstance().getPanelGeometry(i), BaseCanvasAnimation.DEFAULT_SCALE);
			g.fill(panelColors[i % panelColors.length]);
			g.rect(r.x, r.y, r.width, r.height);
		}
		
		// Draw beams
//		for (int i = 0; i < WallGeometry.getInstance().getBeamCount(); i++) {
//			Rectangle r = WallGeometry.scaleRectangleRounded(WallGeometry.getInstance().getBeamGeometry(i), BaseCanvasAnimation.DEFAULT_SCALE);
//			g.fill(255, 0, 255);
//			g.rect(r.x, r.y, r.width, r.height);
//		}
		
	}

}
