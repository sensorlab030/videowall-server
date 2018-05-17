package nl.sensorlab.videowall.animation.canvasanimations;

import nl.sensorlab.videowall.animation.BaseCanvasAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;

public class ShaderAnimation extends BaseCanvasAnimation {


	public ShaderAnimation(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_3D);	
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g, double dt) {
	}

}
