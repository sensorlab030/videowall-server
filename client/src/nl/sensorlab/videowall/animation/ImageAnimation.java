package nl.sensorlab.videowall.animation;

import java.awt.Rectangle;
import java.io.File;
import java.io.FilenameFilter;

import com.cleverfranke.util.Settings;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

public class ImageAnimation extends BaseCanvasAnimation {
	
	private PImage image;

	public ImageAnimation(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		g.background(0);
		
		if (image == null) {
			return;
		}
		
		// Draw image
		g.imageMode(PConstants.CENTER);
		g.translate(g.width / 2, g.height / 2);
		g.image(image, 0, 0);
		
	}
	
	public void setData(String data) {
		image = applet.loadImage(data);
		
		// Resize mage
		if (image != null) {
			
			Rectangle canvasGeometry = getGeometry();
			image.resize(canvasGeometry.width, canvasGeometry.height);
			
		}
	}
	
	/**
	 * Fetch list of compatible image files in the image directory
	 * 
	 * @return
	 */
	public static File[] getImageFileList() {
		
		// Fetch image dir from settings
		String imagePath = Settings.getValue("imageDir");
		if (imagePath == null || imagePath.isEmpty()) {
			return new File[0];
		}
		
		// Check for valid dir
		File imageDir = new File(imagePath);
		if (!imageDir.isDirectory()) {
			return new File[0];
		}
		
		// Fetch list of compatible files
		return imageDir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				String lcName = name.toLowerCase();
				return lcName.endsWith(".png") 
					|| lcName.endsWith(".jpg")
					|| lcName.endsWith(".jpeg")
					|| lcName.endsWith(".bmp");
			}
		});
		
	}

}
