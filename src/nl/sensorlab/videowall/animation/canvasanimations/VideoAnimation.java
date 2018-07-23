package nl.sensorlab.videowall.animation.canvasanimations;

import java.io.File;
import java.io.FilenameFilter;

import com.cleverfranke.util.ConfigurationLoader;

import nl.sensorlab.videowall.animation.BaseCanvasAnimation;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.video.Movie;

/**
 * Animation that displays a video file on the wall
 */
public class VideoAnimation extends BaseCanvasAnimation {
	
	private Movie movie;
	
	public VideoAnimation(PApplet applet) {
		super(applet, DEFAULT_SCALE, CANVAS_MODE_2D);
	}

	@Override
	protected void drawCanvasAnimationFrame(PGraphics g, double dt) {
		g.background(0);
		
		if (movie == null || movie.width == 0) {
			return;
		}
		
		// Get and resize movie frame
		PImage frame = movie.get();
		frame.resize(g.width, g.height);
		
		// Draw movie frame
		g.imageMode(PConstants.CENTER);
		g.translate(g.width / 2, g.height / 2);
		g.image(frame, 0, 0);
	}
	
	public void setVideoFile(String videofile) {
		movie = new Movie(applet, videofile);
		movie.playbin.setVolume(0);
	}
	
	@Override
	public void isStarting() {
		if (movie != null) {
			movie.loop();
		}
	}
	
	@Override
	public void isStopping() {
		if (movie != null) {
			movie.stop();
		}
	}
	
	@Override
	public void setData(String data) {
		setVideoFile(data);
	}

	/**
	 * Fetch list of compatible video files in the video directory
	 * 
	 * @return
	 */
	public static File[] getVideoFileList() {
		
		// Fetch video dir from settings
		String videoPath = ConfigurationLoader.get().getString("path.video", null);
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
					|| lcName.endsWith(".3gp")
					|| lcName.endsWith(".avi");
			}
		});
		
	}

}
