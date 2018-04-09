package nl.sensorlab.videowall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import jonas.Pixel;
import jonas.TriggerObject;
import jonas.TriggerSequence;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PShader;

public class ShaderAnimation extends BaseCanvasAnimation {

	PApplet parent;

	public PShader currentShader;
	public PGraphics container;
	public PImage texture;
	
	public float speed;
	public boolean hasTexture;

	public ShaderAnimation(PApplet applet, String _shaderName, float _speed, boolean _hasTexture, String _textureName) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_3D);
		
		this.parent = applet;
		this.speed = _speed;
		this.hasTexture = _hasTexture;

		// Create graphics so we can use shaders.
		container = parent.createGraphics(parent.width, parent.height, parent.P3D);

		// Load the shader
		currentShader = applet.loadShader("data/shaders/" + _shaderName + ".glsl");

		// Set resolution
		currentShader.set("resolution", (float)parent.width, (float) parent.height); // Doesnt work? I think we the shader uses the canvas width and height; ignoring the set width and height.
		
		if(hasTexture) {
			texture = applet.loadImage("data/textures/"+_textureName+".jpg");
		}
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		// Update the shader
		currentShader.set("time", parent.millis() / speed);

		// Create the 'canvas'
		container.beginDraw();
		container.shader	(currentShader);
		if(hasTexture) {
			container.image(texture, 0, 0,parent.width, parent.height);
		}else {
			container.rect(0, 0, parent.width, parent.height); // ??????
		}
		container.endDraw();

		// Draw the output
		g.image(container, 0, 0, parent.width, parent.height);
	}
}
