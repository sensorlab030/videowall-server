package jonas;

import com.cleverfranke.util.PColor;

import processing.core.PGraphics;
import processing.core.PVector;

public class Brush {
	
	private float angle;
	private int components[];
	private PVector position;
	private int color;
	
	public Brush(PVector _p){
		this.position = _p;
		this.angle = (float)(Math.random() * (Math.PI * 2));
		this.color = PColor.color(50 + (int)(Math.random() * 200), 0, 50 + (int)(Math.random() * 150), 20);
		this.components = new int[2];
		for(int i = 0; i < components.length; i++) components[i] = (int)(1 + Math.random() * 5);
	}	
	
	
	public void paint(PGraphics g) {
		float a = 0;
		float r = 0;
		float u = (float)(0.5 + (Math.random() * 0.5));
		
		PVector paintPosition = new PVector(position.x, position.y);
		
		g.noStroke();
		g.fill(color);
		g.beginShape();
		while(a < (Math.PI * 2)) {
			g.vertex(paintPosition.x, paintPosition.y);
			float v = (float)(0.85 + (Math.random() * 0.15));
			paintPosition.x = (float)(position.x + r * Math.cos(angle + a) * u * v);
			paintPosition.y = (float)(position.y + r * Math.sin(angle + a) * u * v);
			a+= (float)(Math.PI/180);
			for(int i = 0; i <  components.length; i++) r += Math.sin(a * components[i]);
		}
		g.endShape(g.CLOSE);
		
		// Keep within bounds
		if(position.x < 0 ||position. x > g.width || position.y < 0 || position.y > g.height) {
			angle += Math.PI * 0.5;
		}
		
		// Add some more randomness...
		position.x += 2 * Math.cos(angle);
		position.y += 2 * Math.sin(angle);
		angle += -0.15 + Math.random() * 0.3;
		
	}

}
