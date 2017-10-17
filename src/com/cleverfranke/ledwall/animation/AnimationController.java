package com.cleverfranke.ledwall.animation;

import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.List;

import de.looksgood.ani.*;

public class AnimationController extends Animation {
	
	private int currentAnimation;
	AniSequence seq;
	List<Class> classList = new ArrayList<>();
	List<Float> durationList = new ArrayList<>();
	List<Animation> animationList = new ArrayList<>();
	
	public AnimationController(PApplet applet) {
		super(applet);
		this.applet = applet;
				
        classList.add(ChestBoardAnimation.class);
        classList.add(SensorLabAnimation.class);
        classList.add(LineGraphAnimation.class);
        
        for (Class thatClass : classList){
            try {
            	animationList.add( (Animation) thatClass.newInstance());
            } catch (Exception e){
            	e.printStackTrace();
                // :(
            }
        }
        
       
        for( Animation stat : animationList){
            durationList.add(stat.getTotalDuration());
        }
		
        for (Float duration: durationList){
        	System.out.println(duration);
        }

        Ani.init(applet);
		this.seq = new AniSequence(applet);
		this.setSequence();
	}
	
	public void setSequence() {
		this.seq.beginSequence();
		
		// First animation
		this.seq.add(Ani.to(this, (float) 0, (float) 0, "currentAnimation", 0));
		
		// For all following animation, add the animation to the sequence with the previous animation duration as delay
		for (int i=1; i<durationList.size(); i++){
			this.seq.add(Ani.to(this, (float) durationList.get(i), (float) durationList.get(i-1), "currentAnimation", i));
        }
			
		this.seq.endSequence();
		this.seq.start();
	}
	
	@Override
	protected void drawAnimationFrame(PGraphics g) {
		g.smooth();
		g.background(255);
		g.noStroke();
		
		animationList.get(currentAnimation).drawAnimationFrame(g);
		
		// LOOP
		if (this.seq.isEnded()) {
			this.seq.start();
		}
	}
}
