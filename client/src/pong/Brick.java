package pong;

import processing.core.PGraphics;
import processing.core.PVector;

public class Brick {

	public PVector position;

	float width;
	float height;
	int color;

	boolean active = true;

	public Brick(float _x, float _y, float _w, float _h, int _c) {
		this.position = new PVector(_x, _y);
		this.width = _w;
		this.height = _h;
		this.color = _c;
	}

	public void display(PGraphics g) {
		g.fill(color);
		g. rect(this.position.x, this.position.y, this.width, this.height);
	}


}
