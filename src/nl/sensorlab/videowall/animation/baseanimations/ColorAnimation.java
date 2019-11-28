package nl.sensorlab.videowall.animation.baseanimations;

import java.util.List;

import nl.sensorlab.videowall.animation.BaseAnimation;
import nl.sensorlab.videowall.property.IntProperty;
import nl.sensorlab.videowall.property.Property;
import nl.sensorlab.videowall.property.Property.Scope;
import processing.core.PApplet;
import processing.core.PGraphics;

/**
 * Animation that draws a solid color on the wall 
 */
public class ColorAnimation extends BaseAnimation {

	private int color;
	
	private IntProperty red;
	
	public ColorAnimation(PApplet applet) {
		super(applet);
		red = new IntProperty(Scope.Animation, 0, "test");
	}

	@Override
	protected void drawAnimationFrame(PGraphics g, double dt) {
		g.background(color);
	}
	
	@Override
	public void setData(String data) {
		color = Integer.valueOf(data);
	}
	
	@Override
	public List<Property> getProperties() {
		List<Property> list = super.getProperties();
		list.add(red);
		return list;		
	}
	
}
