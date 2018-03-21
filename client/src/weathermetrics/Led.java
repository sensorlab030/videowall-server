package weathermetrics;
import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.WeatherMetrics;
import processing.core.PVector;

public class Led {
	WeatherMetrics parent;
	
	PVector position;
	int width;
	int height;
	
	float alpha;
	
	boolean doneAnimation = false;
	
	float speed = (float) 0.5;
	int direction = 1; // <-- FadeIn or FadeOut
	
	public Led(WeatherMetrics _p, PVector _position, int _width, int _height, float _alpha){
		this.parent = _p;
		this.position = new PVector(_position.x, _position.y);
		this.width = _width;
		this.height= _height;
		this.alpha = _alpha;
	}
	
	public void update() {
//		// Update the brightness
//		color += 10;
//		if(color > 255) {
//			doneAnimation = true;
//		}
	}

}