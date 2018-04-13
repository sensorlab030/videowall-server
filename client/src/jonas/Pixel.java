package jonas;
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
		//		aniAlpha = new Ani(this, speed, "alpha", alphaTarget, Ani.LINEAR, Ani.OVERWRITE);
	}

	public void update() {
		if(!doneUpdating) {
			if(allowAni) {
				allowAni = false;
				//aniAlpha.to(this, speed, "alpha", alphaTarget, Ani.LINEAR);
				//				aniAlpha = new Ani(this, speed, "alpha", alphaTarget, Ani.LINEAR, Ani.OVERWRITE);
				Ani.to(this, speed, "alpha", alphaTarget, Ani.LINEAR);
				//				System.err.println((float)alpha + " " +alphaTarget);
				//				Ani.to(aniAlpha, speed, "alpha", alphaTarget, Ani.LINEAR);
			}else {
				// Check if ANI is done
				//				if(aniAlpha.isEnded()) doneUpdating = true;
			}
		}else {
			allowAni = true;
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
}
