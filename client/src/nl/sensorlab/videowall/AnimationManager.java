package nl.sensorlab.videowall;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.ConfigurationLoader;
import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseAnimation;
import nl.sensorlab.videowall.animation.baseanimations.BouncyPixelsAnimation;
import nl.sensorlab.videowall.animation.baseanimations.ColorAnimation;
import nl.sensorlab.videowall.animation.baseanimations.HorizontalScanAnimation;
import nl.sensorlab.videowall.animation.baseanimations.HorizontalWavesAnimation;
import nl.sensorlab.videowall.animation.baseanimations.LiquidColumnsAnimation;
import nl.sensorlab.videowall.animation.baseanimations.alphabet.Alphabet;
import nl.sensorlab.videowall.animation.baseanimations.flocking.FlockingAnimation;
import nl.sensorlab.videowall.animation.baseanimations.sorting.SortingAnimation;
import nl.sensorlab.videowall.animation.canvasanimations.ImageAnimation;
import nl.sensorlab.videowall.animation.canvasanimations.SensorlabLogoAnimation;
import nl.sensorlab.videowall.animation.canvasanimations.VideoAnimation;
import nl.sensorlab.videowall.animation.canvasanimations.VideoStreamAnimation;
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

		// Full black
		ColorAnimation black = new ColorAnimation(applet);
		black.setData(String.valueOf(PColor.color(0)));
		addAnimation("COL: Black", black);

		// Full white
		ColorAnimation white = new ColorAnimation(applet);
		white.setData(String.valueOf(PColor.color(255)));
		addAnimation("COL: White", white);

		// All Applet based animation
		addAnimation("Sensorlab logo", new SensorlabLogoAnimation(applet));
		addAnimation("Alphabet", new Alphabet(applet));
		addAnimation("Horizontal Waves", new HorizontalWavesAnimation(applet));
		addAnimation("Video stream", new VideoStreamAnimation(applet));
		addAnimation("Swarm Animation (flocking)", new FlockingAnimation(applet));
		addAnimation("Bouncy Pixels Animation", new BouncyPixelsAnimation(applet));
		addAnimation("Dark Shadow (Liquid Columns)", new LiquidColumnsAnimation(applet));
		addAnimation("Bar Sorting (visualizing sorting methods)", new SortingAnimation(applet));
		addAnimation("Horizontal Scan", new HorizontalScanAnimation(applet));

		// Video stream options
		for (String host: ConfigurationLoader.get().getString("streaming.hosts", "").split(",")) {
			host = host.trim();
			if (!host.isEmpty()) {
			    addAnimation("STR: " + host, host, new VideoStreamAnimation(applet));
			}
		}
		
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
