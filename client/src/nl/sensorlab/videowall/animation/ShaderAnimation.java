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

public class ShaderAnimation extends BaseAnimation {

	PApplet parent;

	public PShader currentShader;
	public PGraphics container;
	public PImage texture;
	
	public float speed;
	public boolean hasTexture;

	public ShaderAnimation(PApplet applet, String _shaderName, float _speed, boolean _hasTexture, String _textureName) {
		super(applet);
		
		this.parent = applet;
		this.speed = _speed;
		this.hasTexture = _hasTexture;

		// Create graphics so we can use shaders.
		container = parent.createGraphics(PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y, parent.P2D);

		// Load the shader
		currentShader = applet.loadShader("data/shaders/" + _shaderName + ".glsl");

		// Set resolution
		currentShader.set("resolution", (float)PIXEL_RESOLUTION_X, (float)PIXEL_RESOLUTION_Y);
		
		if(hasTexture) {
			texture = applet.loadImage("data/textures/"+_textureName+".jpg");
		}
	}

	@Override
	protected void drawAnimationFrame(PGraphics g) {
		// Update the shader
		currentShader.set("time", parent.millis() / speed);

		// Create the 'canvas'
		container.beginDraw();
		container.shader	(currentShader);
		if(hasTexture) container.image(texture, 0, 0,PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
		if(!hasTexture)container.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);
		container.endDraw();

		//		// Draw the output
		g.image(container, 0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);	
	}
}
