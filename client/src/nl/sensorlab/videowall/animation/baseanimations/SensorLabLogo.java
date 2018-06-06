package nl.sensorlab.videowall.animation.baseanimations;

import java.util.ArrayList;
import java.util.Iterator;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.BaseAnimation;
import processing.core.PApplet;
import processing.core.PGraphics;

public class SensorLabLogo extends BaseAnimation {
	
	private final static int BAR_COUNT = 13;
	
	private final static int[] BAR_SIZES = {27, 0, 54, 0, 81, 0, 54, 0};
	
	private int currentBarSizeIndex = 0;
	private int currentBarIndex = 0;
	
	private ArrayList<BarRect> bars = new ArrayList<BarRect>();
	
	public SensorLabLogo(PApplet applet) {
		super(applet);
		addBar();
	}
	
	@Override
	protected void drawAnimationFrame(PGraphics g, double dt) {
		g.background(0);
		
		// Update and draw bars
		g.noStroke();
		Iterator<BarRect> it = bars.iterator();
		while (it.hasNext()) {
			BarRect b = it.next();
			
			b.update(dt);
			
			if (b.isDecayed()) {
				it.remove();
			} else {
				b.draw(g);
			}
		}
		
		// Check if we need to add a new bar
		if (bars.get(bars.size() - 1).isDoneGrowing()) {
			addBar();
		}
		
	}
	
	private void addBar() {
		bars.add(new BarRect(currentBarIndex * BarRect.WIDTH, BAR_SIZES[currentBarSizeIndex]));
		
		// Update indices
		currentBarIndex = (currentBarIndex < BAR_COUNT - 1) ? currentBarIndex + 1 : 0;
		currentBarSizeIndex = (currentBarSizeIndex < BAR_SIZES.length - 1) ? currentBarSizeIndex + 1 : 0;
		
	}
	
	private class BarRect {
		
		public final static int WIDTH = 2;
		public final static float GROWTH_SPEED = 0.06f;
		public final static float OPACITY_DECAY_SPEED = 0.00015f;
		
		private int x;
		private int targetHeight;
		private float currentHeight = 0;
		private float currentOpacity = 1f;
		private boolean isDoneGrowing = false;
		private boolean isDecayed = false;
		
		public BarRect(int x, int targetHeight) {
			this.x = x;
			this.targetHeight = targetHeight;
		}
		
		public void update(double dt) {
			
			if (!isDoneGrowing) {
				
				// Update height
				currentHeight = (float) Math.min(targetHeight, currentHeight + GROWTH_SPEED * dt);
				
				if (currentHeight >= targetHeight) {
					isDoneGrowing = true;
				}
				
			} else if (!isDecayed){
				
				// Update opacity (decay)
				currentOpacity = (float) Math.max(0, currentOpacity - OPACITY_DECAY_SPEED * dt); 
				
				if (currentOpacity <= 0) {
					isDecayed = true;
				}
				
			}
			
		}
		
		public void draw(PGraphics g) {
			
			// Draw
			g.fill(PColor.color(1f, 1f, 1f, currentOpacity));
			g.rect(x, PIXEL_RESOLUTION_Y, WIDTH, -currentHeight);
			
		}
		
		public boolean isDoneGrowing() {
			return isDoneGrowing;
		}
		
		public boolean isDecayed() {
			return isDecayed;
		}
		
	}
	
}
