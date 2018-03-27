package weathermetrics;
import processing.core.PVector;

public class Pixel {
	
	public PVector position;
	public int color;
	
	public float alpha;
	
	public float speed = 10;
	public int direction = 1;
	
	public boolean doneUpdating = false;
	
	public Pixel(PVector _position) {
		this.position = _position;
	}
	
	public void fadeIn(float _speed) {
		speed = _speed;
		direction = 1;
	}
	
	public void fadeOut(float _speed) {
		speed = _speed;
		direction = -1;
	}
	
	public void toggleDirection() {
		direction = direction == -1 ? 1 : -1;
	}
	
	public void update() {
		if(alpha > 255 || alpha < 0) { // We can add a target alpha later;
			alpha = alpha > 255 ? 255 : 0; // Make sure alpha is clamped so doneAnimating will not be set when exceeding
			doneUpdating = true;
		}else {
			alpha += (speed * direction);
		}
	}
	
}
