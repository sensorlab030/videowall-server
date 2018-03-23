package weathermetrics;
import nl.sensorlab.videowall.animation.WeatherMetrics;
import processing.core.PVector;

public class LED {
	
	WeatherMetrics parent; 
	
	public PVector positionA; // <-- Starting position
	public PVector positionB; // <-- End positions
	
	public int index;
	public int color;
	public float alpha;
	
	//	float hue, saturation, brightness;
	
	public boolean doneUpdating = false;
	
	public float speed = (float) 200; // Increase speed
	public int direction = 1; // FadeIn or FadeOut
	
	public LED(WeatherMetrics _p, int _index, PVector _positionA, PVector _positionB, int _c, float _a){
		this.parent = _p;
		this.index= _index;
		this.positionA = _positionA;
		this.positionB = _positionB;
		this.color = _c;
		this.alpha = _a;
	}
	
	public void update() {
		alpha += (speed * direction);
		if(alpha > 255 || alpha < 0) {
			alpha = alpha > 255 ? 255 : 0; // Make sure alpha is clamped so doneAnimating will not be set when exceeding
			doneUpdating = true;
		}
	}
}
