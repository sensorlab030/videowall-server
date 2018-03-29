package weathermetrics;
import de.looksgood.ani.Ani;
import processing.core.PVector;

public class Pixel {
	
	public PVector position;
	public int color;
	
	Ani aniAlpha;
	
	public float alpha = 0;
	public float alphaTarget = 0;
	
	public float speed;
	
	public boolean doneUpdating = false;
	public boolean allowAni = true;
	
	public Pixel(PVector _position) {
		this.position = _position;
	}
	
	public void update() {
		if(!doneUpdating) {
			if(allowAni) {
				allowAni = false;
				aniAlpha = new Ani(this, speed, "alpha", alphaTarget, Ani.LINEAR);
			}else {
				// Check if ANI is done
				if(aniAlpha.isEnded()) {
					doneUpdating = true;
					allowAni = true;
				}
			}
		}
	}
	
	public void fadeIn(float _speed, float _alphaTarget) {
		// Check if the alpha target is different then the current alpha target; if so allow overwrite
		if(_alphaTarget != alphaTarget) {
			allowAni = true;
			doneUpdating = false;
		}
		alphaTarget =  _alphaTarget;
		speed =  _speed;
	}
	
	public void fadeOut(float _speed, float _alphaTarget) {
		// Check if the alpha target is different then the current alpha target; if so allow overwrite
		if(_alphaTarget != alphaTarget) {
			allowAni = true;
			doneUpdating = false;
		}
		alphaTarget = (float) _alphaTarget;
		speed = (float) _speed;
	}
	
	public void toggleAlphaTarget() {
		alphaTarget = alphaTarget == 255 ? 0 : 255;
		allowAni = true;
		doneUpdating = false;
	}
}
