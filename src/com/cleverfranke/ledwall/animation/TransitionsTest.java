
package com.cleverfranke.ledwall.animation;

import com.cleverfranke.ledwall.WallConfiguration;
import com.cleverfranke.util.PColor;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.event.KeyEvent;
import de.looksgood.ani.*;

public class TransitionsTest extends Animation{
	float x, y, diameter;
	AniSequence seq;
	int color = generateRandomRGBColor();
	
	public TransitionsTest(PApplet applet) {
		super(applet);
		this.applet = applet;
		x = 0;
		y = 0;
		diameter = 0;
		
		
		// you have to call always Ani.init() first!
		Ani.init(applet);
	  
		  // create a sequence
		  // dont forget to call beginSequence() and endSequence()
		  seq = new AniSequence(applet);
		  seq.beginSequence();
		  
		  // step 0
		  seq.add(Ani.to(this, 1, "diameter", 100));

		  // step 1
		  seq.add(Ani.to(this, 2, "x:400,y:100"));
		  
		  // step 2
		  seq.add(Ani.to(this, 1, "x:450,y:400"));
		  
		  // step 3
		  seq.add(Ani.to(this, 1, "x:100,y:450"));
		  
	  
		  // step 4
		  seq.beginStep();
		  seq.add(Ani.to(this, 1, "x:50,y:50"));
		  seq.add(Ani.to(this, 2, "diameter", 5));
		  seq.endStep();

		  seq.endSequence();
		  seq.start();
		  
	}
	
	public void keyEvent(KeyEvent event) {
		char key = event.getKey();
		if (key == 's' || key == 'S') {
			System.out.println("pressed");
			// start the whole sequence
			  seq.start();
		}
	}
	
	@Override
	protected void drawAnimationFrame(PGraphics g) {
		g.smooth();
		g.background(255);
		g.fill(color);
		
		g.rect(x, y, diameter, diameter);
	}

}
