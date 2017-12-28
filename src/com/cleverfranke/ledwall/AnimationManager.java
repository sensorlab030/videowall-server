package com.cleverfranke.ledwall;

import java.util.ArrayList;

import com.cleverfranke.ledwall.animation.BaseAnimation;

/**
 * Class that handles the animation queue and transitions between them.
 */
public class AnimationManager {
	
	// Singleton instance
	private static AnimationManager instance = null; 

	private BaseAnimation currentAnimation;
	private ArrayList<AnimationEntry> availableAnimations = new ArrayList<>();
	
	protected AnimationManager() {}
	
	/**
	 * Get singleton instance
	 * @return
	 */
	public static AnimationManager getInstance() {
		if (instance == null) {
			instance = new AnimationManager();
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
	
	public ArrayList<AnimationEntry> getAvailableAnimations() {
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
		if (currentAnimation != null) {
			if (entry.getData() != null && !entry.getData().isEmpty()) {
				currentAnimation.setData(entry.getData());
			}
			
			// Send starting signal
			currentAnimation.isStarting();
		}
		
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

}
