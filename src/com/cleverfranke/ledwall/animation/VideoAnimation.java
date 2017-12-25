package com.cleverfranke.ledwall.animation;

import java.io.File;
import java.io.FilenameFilter;

import com.cleverfranke.ledwall.Settings;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.video.Movie;

/**
 * Animation that displays a video file on the wall
 */
public class VideoAnimation extends BaseCanvasAnimation {
	
	private Movie movie;
	
	public VideoAnimation(String file, PApplet applet) {
		super(applet);

		// Load image
		movie = new Movie(applet, file);
		movie.loop();
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g) {
		g.background(0);
		
		if (movie == null) {
			return;
		}
		
		// Draw movie image 
		// @TODO scale image to size
		g.imageMode(PConstants.CENTER);
		g.translate(g.width / 2, g.height / 2);
		g.image(movie, 0, 0);

	}

	/**
	 * Fetch list of compatible video files in the video directory
	 * 
	 * @return
	 */
	public static File[] getVideoFileList() {
		
		// Fetch video dir from settings
		String videoPath = Settings.getValue("videoDir");
		if (videoPath == null || videoPath.isEmpty()) {
			return new File[0];
		}
		
		// Check for valid dir
		File videoDir = new File(videoPath);
		if (!videoDir.isDirectory()) {
			return new File[0];
		}
		
		// Fetch list of compatible files
		return videoDir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				String lcName = name.toLowerCase();
				return lcName.endsWith(".mp4") 
					|| lcName.endsWith(".mov")
					|| lcName.endsWith(".mpeg")
					|| lcName.endsWith(".mpg")
					|| lcName.endsWith(".avi");
			}
		});
		
	}
	
}
