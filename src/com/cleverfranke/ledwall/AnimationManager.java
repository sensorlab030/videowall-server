package com.cleverfranke.ledwall;

import java.util.HashMap;
import java.util.Map;

import com.cleverfranke.ledwall.animation.BasePixelAnimation;

/**
 * Class that handles the animation queue and transitions between them.
 */
public class AnimationManager {

	private BasePixelAnimation currentAnimation;
	private Map<String, BasePixelAnimation> availableAnimations = new HashMap<>();
	
	public BasePixelAnimation getCurrentAnimation() {
		return currentAnimation;
	}
	
	public void addAnimation(String id, String label, BasePixelAnimation animation) {
		
	}
	
	public Map<String, BasePixelAnimation> getAvailableAnimations() {
		return availableAnimations;
	}
	
	public class AnimationEntry {
		private String key;
		private String label;
		private String data;
		private BasePixelAnimation animation;
		
		private AnimationEntry(String key, String label, String data, BasePixelAnimation animation) {
			this.key = key;
			this.label = label;
			this.data = data;
			this.animation = animation;
		}
		
		public AnimationEntry createAnimationEntry(String key, String label, BasePixelAnimation animation) {
			return new AnimationEntry(key, label, null, animation);
		}
		
		public AnimationEntry createAnimationEntry(String key, String label, String data, BasePixelAnimation animation) {
			return new AnimationEntry(key, label, data, animation);
		}

		public String getKey() {
			return key;
		}

		public String getLabel() {
			return label;
		}

		public String getData() {
			return data;
		}
		
		public BasePixelAnimation getAnimation() {
			return animation;
		}
		
		
	}

}
