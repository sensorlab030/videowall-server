package alphabet;

import processing.core.PGraphics;

public class Block {
	private int x;
	private int y;
	private int width;
	private int height;

	public Block(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void draw(PGraphics g) {
		g.rect(this.x, this.y, this.width, this.height);
	}
}
