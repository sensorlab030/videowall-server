package nl.sensorlab.videowall.animation.baseanimations.liquidcolumns;
import nl.sensorlab.videowall.animation.baseanimations.LiquidColumnsAnimation;
import processing.core.PGraphics;
import processing.core.PVector;

public class PushObject {
	
	private LiquidColumnsAnimation parent;
	
	public PVector position;
	public float speed;
	public float radius;
	
	public PushObject(LiquidColumnsAnimation _p){
		this.parent = _p;
		reset();
	}
	
	public void reset() {
		this.radius = (float)(3 + Math.random() * 20);
		this.speed = (float)(0.005f + Math.random() * 0.35f);
		this.position = new PVector(-this.radius, (float)(Math.random() * parent.PIXEL_RESOLUTION_Y));
	}
	
	public void update() {
		this.position.x += this.speed;
		if(this.position.x > (parent.PIXEL_RESOLUTION_X + radius)) reset();
	}
}
