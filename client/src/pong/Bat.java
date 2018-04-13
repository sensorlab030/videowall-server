package pong;

import processing.core.PGraphics;
import processing.core.PVector;

public class Bat {
	
	public PVector position;
	public float width;
	public float height;
	
	public Bat(float _x, float _y, float _w, float _h) {
		this.position = new PVector(_x, _y);
		this.width = _w;
		this.height = _h;
	}

	// Update the position of the bat
	public void update(float _inputX) {
		this.position.x = _inputX - width/2;
	}
	
	public void display(PGraphics g) {
		g.noStroke();
		g.fill(220,250,250);
		g.rect(this.position.x, this.position.y, this.width, this.height);
	}
	
}
