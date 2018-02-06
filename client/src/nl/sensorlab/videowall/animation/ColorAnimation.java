package nl.sensorlab.videowall.animation;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Animation that draws a solid color on the wall 
 */
public class ColorAnimation extends BaseAnimation {

	private int color;
	
	public ColorAnimation(PApplet applet) {
		super(applet);
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double dt) {
		g.background(color);
	}
	
	@Override
	public void setData(String data) {
		color = Integer.valueOf(data);
	}
	
}
