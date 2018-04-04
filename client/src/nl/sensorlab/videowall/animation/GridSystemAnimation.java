package nl.sensorlab.videowall.animation;

import java.util.ArrayList;
import java.util.List;

import com.cleverfranke.util.PColor;

import de.looksgood.ani.Ani;
import jonas.Node;
import jonas.Pixel;
import jonas.TriggerObject;
import jonas.TriggerSequence;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class GridSystemAnimation extends BaseAnimation {

	public PApplet parent;

	ArrayList<Node> nodes;

	public float[] directions = {(float)Math.PI, (float)Math.PI/2, 0, (float)(Math.PI*1.5)}; // <-- Array with angles

	public int amountNodes = 20;
	public int triggerIncrements = 10; // <-- When to toggle direction
	public float triggerIncreaseIncrements = 0.15f; // <-- 'speed'

	public float baseColor = 0;

	public GridSystemAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;
		nodes = new ArrayList<Node>();
		generateNodes(amountNodes);
	}

	public void generateNodes(int _amountNodes) {
		for (int i = 0; i <  _amountNodes; i++) {
			float x, y;

			// Toggle between x and y
			if (parent.random(1) > 0.5) {
				x = parent.random(PIXEL_RESOLUTION_X);
				y = parent.random(1) > 0.5 ? 0 : PIXEL_RESOLUTION_Y;
			} else {
				x = parent.random(1) > 0.5 ? 0 : PIXEL_RESOLUTION_X;
				y = parent.random(PIXEL_RESOLUTION_Y);
			}
			// Add the Node
			float d = directions[parent.floor(parent.random(directions.length))];
			nodes.add(new Node(this, new PVector(x, y), d));
		}
	}


	@Override
	protected void drawAnimationFrame(PGraphics g) {
		// Add some fade effect
		g.noStroke();
		g.fill(0, 5);
		g.rect(0, 0, PIXEL_RESOLUTION_X, PIXEL_RESOLUTION_Y);

		// Update the nodes
		for(Node n : nodes) {
			n.update();
			n.display(g);
		}
		baseColor += 0.25f;
		if(baseColor > 1020) baseColor = 0;
	}
}