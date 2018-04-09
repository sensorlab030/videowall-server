package nl.sensorlab.videowall.animation;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import jonas.Pixel;
import jonas.TriggerObject;
import jonas.TriggerSequence;
import nl.sensorlab.videowall.walldriver.WallGeometry;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.opengl.PShader;

public class ShaderAnimation extends BaseCanvasAnimation {

	PApplet parent;

	public PShader currentShader;
	public PImage texture;

	public float speed;
	public boolean hasTexture;


	public ShaderAnimation(PApplet applet, String _shaderName, float _speed, boolean _hasTexture, String _textureName) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_3D);

		this.parent = applet;
		this.speed = _speed;
		this.hasTexture = _hasTexture;

		// Load the shader
		currentShader = applet.loadShader("data/shaders/" + _shaderName + ".glsl");

		// Get the canvas dimensions
		Rectangle canvasRect = getGeometry();

		// Set resolution
		currentShader.set("resolution", (float) canvasRect.width, (float) canvasRect.height);   

		// Load texture if needed
		if(hasTexture) {
			parent.textureWrap(parent.REPEAT);
			texture = parent.loadImage("data/textures/"+_textureName+".jpg");
		}
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		// Update the shader
		currentShader.set("time", (float) (parent.millis() / speed));
		
		// Draw the shader
		g.shader(currentShader);

		// Map the shader to..
		if(hasTexture) {
			// Use mouse for distortion
			//			currentShader.set("mouse", (float)parent.random(-1,1), (float)parent.random(-1,1));
			g.image(texture, 0, 0, g.width, g.height);
		}else {
			g.rect(0,0, g.width, g.height);
		}
	}
}
