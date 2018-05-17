package nl.sensorlab.videowall.animation.canvasanimations;

import java.awt.Rectangle;

import nl.sensorlab.videowall.animation.BaseCanvasAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PShader;


public class ShaderAnimation extends BaseCanvasAnimation {

	PApplet parent;
	private PShader currentShader;
	private float speed;

	public ShaderAnimation(PApplet applet, String shaderName, float speed) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_3D);	
		this.parent = applet;
		this.speed = speed;
		
		// Load the Shader
		this.currentShader = applet.loadShader(getResource("shaders/" + shaderName + ".glsl"));

		// Get the canvas dimensions
		Rectangle canvasRect = getGeometry();

		// Set resolution
		currentShader.set("resolution", (float) canvasRect.width, (float) canvasRect.height);   
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g, double dt) {
		// Update the shader
		currentShader.set("time", (float) (parent.millis() / speed));

		// Draw the shader
		g.shader(currentShader);
		g.rect(0, 0, g.width, g.height);
	}

}
