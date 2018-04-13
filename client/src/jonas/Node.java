package jonas;

import com.cleverfranke.util.PColor;

import nl.sensorlab.videowall.animation.GridSystemAnimation;
import processing.core.PGraphics;
import processing.core.PVector;

public class Node {

	GridSystemAnimation parent;

	public PVector position;
	public float direction;
	public int currentTriggerCounter = 0;
	public int nodeColor;

	public Node(GridSystemAnimation _parent, PVector _position, float _direction) {
		this.parent = _parent;
		this.position = _position;
		this.direction = _direction;

		// Update the color
		updateColor();
	}

	public void updateColor(){
		nodeColor = PColor.color((int)(((50 + (Math.random() * 30)) + parent.baseColor) % 255), (int)(100 + (Math.random() * 155)), 255, 150);
	}

	public void display(PGraphics g) {
		g.noFill();
		g.stroke(nodeColor);
		g.point(position.x, position.y);
	}

	public void update() {
		// Update the positions
		position.x += parent.triggerIncreaseIncrements * Math.cos(direction);
		position.y += parent.triggerIncreaseIncrements * Math.sin(direction);

		// Update the trigger
		currentTriggerCounter ++;

		// Toggle direction
		if (currentTriggerCounter > parent.triggerIncrements || position.x  > parent.PIXEL_RESOLUTION_X || position.x  < 0 || position.y < 0 || position.y > parent.PIXEL_RESOLUTION_Y) {
			currentTriggerCounter = 0;
			direction =  parent.directions[parent.parent.floor(parent.parent.random(parent.directions.length))];
			updateColor();
		}
	}
}
