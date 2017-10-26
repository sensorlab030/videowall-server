package com.cleverfranke.ledwall;

import processing.core.PGraphics;
import java.util.LinkedList;
import com.cleverfranke.ledwall.animation.Animation;
import de.looksgood.ani.Ani;
import de.looksgood.ani.easing.Easing;

/**
 * Class that handles the animation queue and transitions between them.
 */
public class AnimationManager {

	LinkedList<Animation> queue = new LinkedList<Animation>();	// List of the animations to queue
	AnimationTransition transition;								// Transition instance
	Animation currentVisualization;								// Current visualization member
	int currentVisualizationFrameCount;							// Frame counter for current visualization, updated by update()


	public AnimationManager() {

		this.transition = new AnimationTransition();

	}

	/**
	 * Route the drawing call from the given renderer
	 * to the required visualization(s)
	 *
	 * @param g
	 */
	public void draw(PGraphics g) {

		// Update the manager
		update();

		// Route draw call
		if (transition.isInProgress()) {
			transition.draw(g);
		} else {
			// Draw animation frame to image
			currentVisualization.draw();
		}

	}

	/**
	 * Add visualization to the end of the queue
	 *
	 * @param visualization
	 */
	public void queueVisualization(Animation visualization) {

		if (currentVisualization == null) {
			visualization.prepareForQueueRotation();
			currentVisualization = visualization;
		} else {
			queue.add(visualization);
		}

	}


	/**
	 * Update to keep the manager keeping track of the queue
	 */
	private void update() {

		// Don't keep track of visualization rotation if we have just one visualization
		if (queue.size() == 0) {
			return;
		}

		if (!transition.isInProgress()) {
			// Update frame counter and switch if needed
			currentVisualizationFrameCount++;
			if (currentVisualization.isDone()) {
				gotoNextVisualization();
			}

		}

	}

	private void handleTransitionDone() {

		// Queue the current visualization if it's in default rotation
		if (currentVisualization.isInDefaultRotation()) {
			queueVisualization(currentVisualization);
		}

		// Set next vis as current and reset frame counter
		currentVisualization = transition.toVisualization;
		currentVisualizationFrameCount = 0;

	}

	/**
	 * Transition to the next visualization in the queue
	 */
	private void gotoNextVisualization() {

		// Fetch target visualization and prepare it
		Animation newVisualization;
		boolean availableForQueueRotation = false;
		do {
			// Get the visualization
			newVisualization = queue.removeFirst();
			availableForQueueRotation = newVisualization.isAvailableForQueueRotation();

			// Requeue if not available now, but in default rotations
			if (newVisualization.isInDefaultRotation() && !availableForQueueRotation) {
				queueVisualization(newVisualization);
			}
		} while (!availableForQueueRotation);
		newVisualization.prepareForQueueRotation();

		// Start transition
		transition.start(currentVisualization, newVisualization);

	}

	/**
	 * Class to handle a fade to black and back between two
	 * visualizations
	 */
	public class AnimationTransition {

		private final float TRANSITION_DURATION = 0.3f; // Duration in seconds of the transition between visualizations

		private Animation fromVisualization;
		private Animation toVisualization;

		private Ani outAnimation;
		private Ani inAnimation;
		private float fadeValue;

		/**
		 * Start fade between <from> to <to>
		 * @param from
		 * @param to
		 */
		public void start(Animation from, Animation to) {
			fromVisualization = from;
			toVisualization = to;

			fadeValue = 0;
			outAnimation = new Ani(this, TRANSITION_DURATION * 0.5f, 0, "fadeValue", 1f, Easing.LINEAR, this, "onEnd:outAnimationDone");
			outAnimation.start();
		}

		/**
		 * Callback for the outAnimation when it's done
		 */
		@SuppressWarnings("unused")
		private void outAnimationDone() {
			outAnimation = null;

			fadeValue = 1f;
			inAnimation = new Ani(this, TRANSITION_DURATION * 0.5f, 0, "fadeValue", 0f, Easing.LINEAR, this, "onEnd:inAnimationDone");
			inAnimation.start();
		}

		/**
		 * Callback for the inAnimation when it's done
		 */
		@SuppressWarnings("unused")
		private void inAnimationDone() {
			inAnimation = null;
			toVisualization.inAnimationDone();
			handleTransitionDone();
		}

		/**
		 * Get if transition is in progress
		 */
		public boolean isInProgress() {
			return (outAnimation != null || inAnimation != null);
		}

		/**
		 * Draw the relevant visualization and the fade
		 * on top
		 *
		 * @param g
		 */
		public void draw(PGraphics g) {
			// Draw underlying animation
			if (outAnimation != null) {
				fromVisualization.draw();
			} else if (inAnimation != null) {
				toVisualization.draw();
			}

			// Draw black fade
			g.noStroke();
			g.fill(26, 27, 33, (int) (fadeValue * 255f));
			g.rect(0, 0, g.width, g.height);
		}

	}

}
