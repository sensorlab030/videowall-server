package nl.sensorlab.videowall;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.sensorlab.videowall.animation.*;
import processing.core.PApplet;

/**
 * Class that handles the animation queue and transitions between them.
 */
public class AnimationManager {

	private static AnimationManager instance = null; 						// Singleton instance
	private BaseAnimation currentAnimation;									// Current animation
	private List<AnimationEntry> availableAnimations = new ArrayList<>();	// All available animation

	private List<AnimationEventListener> eventListeners = new ArrayList<AnimationEventListener>();

	/**
	 * Instantiate AnimationManager; add animations to the available
	 * animations list
	 * 
	 * @param applet
	 */
	public AnimationManager(PApplet applet) {

		// All Applet based animation
		//		addAnimation("Beach ball", new BeachballAnimation(applet));
		addAnimation("Line wave", new LineWaveAnimation(applet));
		addAnimation("Sound animation", new SoundAnimation(applet));
		addAnimation("Complementary colors", new ComplementaryColors(applet));
		addAnimation("Weather Metrics Pixel", new WeatherAnimation(applet));
		addAnimation("Horizontal Waves", new WavesAnimation(applet));
		addAnimation("Grid System", new GridSystemAnimation(applet));
		addAnimation("Swirl System", new SwirlAnimation(applet));
		addAnimation("Game of Life", new GameOfLifeAnimation(applet));
		addAnimation("Color Grid", new ColorGridAnimation(applet));
		addAnimation("Perlin Noise", new PerlinNoiseAnimation(applet));
		addAnimation("Liquid Columns", new LiquidColumnsAnimation(applet));
		addAnimation("Flocking", new FlockingAnimation(applet));
		addAnimation("Bouncy Bubbles", new BouncyBubblesAnimation(applet));
		addAnimation("Shader Nebula", new ShaderAnimation(applet, "nebula", 1500, false, ""));
		//		addAnimation("Shader Deform", new ShaderAnimation(applet, "deform", 750, true, "tex"));
		addAnimation("Shader Monjori", new ShaderAnimation(applet, "monjori", 1200, false, ""));
		addAnimation("Shader Landscape", new ShaderAnimation(applet, "landscape", 3500, false, ""));
		addAnimation("Brush Canvas", new BrushAnimation(applet));
		addAnimation("Horizontal Scan", new HorizontalScanAnimation(applet));
		addAnimation("Column Pixel", new SinglePixelAnimation(applet));
		addAnimation("Sorting Animations", new SortingAnimation(applet));	
		addAnimation("Fish Tank", new FishTankAnimation(applet));	
		

		// Add videos to animation manager
		VideoAnimation videoAnimation = new VideoAnimation(applet);
		for (File f : VideoAnimation.getVideoFileList()) {
			String filename = f.getName();
			filename = filename.substring(0, filename.lastIndexOf('.'));
			addAnimation("VID: " + filename, f.getAbsolutePath(), videoAnimation);
		}

		// Add images to animation manager
		ImageAnimation imageAnimation = new ImageAnimation(applet);
		for (File f : ImageAnimation.getImageFileList()) {
			String filename = f.getName();
			filename = filename.substring(0, filename.lastIndexOf('.'));
			addAnimation("IMG: " + filename, f.getAbsolutePath(), imageAnimation);
		}

		// Debug
		// addAnimation("Debug animation", new DebugCanvasAnimation(applet));

		// Start with the latest animation (in the list)
		startAnimation(availableAnimations.size()-1);
	}

	/**
	 * Initialize Animation. This method should be called before calling
	 * AnimationManager.getInstance() and subsequent methods
	 * 
	 * @return
	 */
	public static void initialize(PApplet applet) {
		if (instance != null) {
			System.err.println("Cannot initialize AnimationManager twice, this call is ignored");
		} else {
			instance = new AnimationManager(applet);
		}
	}

	/**
	 * Get singleton instance
	 * @return
	 */
	public static AnimationManager getInstance() {
		if (instance == null) {
			System.err.println("AnimationManager not initialized; first cal AnimationManager.initialize");
		}
		return instance;
	}

	public BaseAnimation getCurrentAnimation() {
		return currentAnimation;
	}

	public int addAnimation(String label, String data, BaseAnimation animation) {
		if (availableAnimations.add(new AnimationEntry(label, data, animation))) {
			return availableAnimations.size() - 1;
		} else {
			return -1;
		}
	}

	public int addAnimation(String label, BaseAnimation animation) {
		return addAnimation(label, null, animation);
	}

	public List<AnimationEntry> getAvailableAnimations() {
		return availableAnimations;
	}

	public int getAvailableAnimationCount() {
		return availableAnimations.size();
	}

	public void startAnimation(int index) {
		if (index > availableAnimations.size() - 1) {
			System.err.println("Cannot start animation: Invalid animation index");
			return;
		}

		// Send stop signal to current animation
		if (currentAnimation != null) {
			currentAnimation.isStopping();
		}

		// Set current animation to animation with given index
		AnimationEntry entry = availableAnimations.get(index);
		currentAnimation = entry.getAnimation();
		if (currentAnimation == null) {
			return;
		}

		// Set data (if any data is available)
		if (entry.getData() != null && !entry.getData().isEmpty()) {
			currentAnimation.setData(entry.getData());
		}

		// Send starting signal
		currentAnimation.isStarting();

		// Send change event
		for (AnimationEventListener listener : eventListeners) {
			listener.onCurrentAnimationChanged(index);
		}

	}

	public void addListener(AnimationEventListener listener) {
		eventListeners.add(listener);
	}

	public class AnimationEntry {
		private String label;
		private String data;
		private BaseAnimation animation;

		private AnimationEntry(String label, String data, BaseAnimation animation) {
			this.label = label;
			this.data = data;
			this.animation = animation;
		}

		public AnimationEntry createAnimationEntry(String label, BaseAnimation animation) {
			return new AnimationEntry( label, null, animation);
		}

		public AnimationEntry createAnimationEntry(String label, String data, BaseAnimation animation) {
			return new AnimationEntry( label, data, animation);
		}

		public String getLabel() {
			return label;
		}

		public String getData() {
			return data;
		}

		public BaseAnimation getAnimation() {
			return animation;
		}

	}

	/**
	 * Interface to subscribe to event changes
	 */
	public interface AnimationEventListener {

		/**
		 * Method that is called when the current animation
		 * has changed
		 * 
		 * @param index
		 */
		void onCurrentAnimationChanged(int index);
	}

}
