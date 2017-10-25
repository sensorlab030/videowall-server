package com.cleverfranke.ledwall.animation;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.video.*;

public class VideoStream extends CanvasAnimation {
	Movie myMovie;
	
	public VideoStream(boolean inDefaultRotation, PApplet applet) {
		super(inDefaultRotation, applet);
		
		// myMovie = new Movie(applet, "/Users/agathelenclen/Projects/led-wall/src/data/CLEVER°FRANKE – Data driven experiences-HD.mp4");
		// myMovie = new Movie(applet, "/Users/agathelenclen/Projects/led-wall/src/data/transit.mov");
		myMovie = new Movie(applet, "/Users/agathelenclen/Projects/led-wall/src/data/lionKing.mov");
//		myMovie.play();
	}
	

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return (myMovie.time() == myMovie.duration());
	}


	@Override
	public void drawAnimationFrame(PGraphics g) {
		if (myMovie.available()) {
		    myMovie.read();
		    g.image(myMovie, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);		    
		}
	}


	@Override
	public void prepareForQueueRotation() {
		// TODO Auto-generated method stub
		myMovie.jump(0);
		myMovie.play();
	}

}
