package nl.sensorlab.videowall.animation;

import pong.Pong;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.net.Client;
import processing.net.Server;



public class PongGameAnimation extends BaseAnimation {

	public PApplet parent;
	
	Pong pong;

	private Server server;
	private Client client;

	private int clientInput = 0;

	public PongGameAnimation(PApplet applet) {
		super(applet);
		this.parent = applet;

		// Create the Server with the port 4000
		server = new Server(parent, 4000);
		
		// Create a new pong instance
		pong = new Pong(this, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y);
	}

	protected void update() {
		// Get the client data
		client = server.available();

		// Check if available
		if(client != null) {
			String input = client.readString();
			// Convert to int
			clientInput = Integer.parseInt(input.substring(0, input.indexOf("\n")));
		}else {
			clientInput = (int)parent.map(parent.mouseX, 0, parent.width, 0, PIXEL_RESOLUTION_X);
		}
	
		
		// Update the ponggame
		pong.update(clientInput);
	}


	@Override
	protected void drawAnimationFrame(PGraphics g, double t) {
		update();
		
		// Fade
		g.noStroke();
		g.fill(0, 10);
		g.rect(0, 0, PIXEL_RESOLUTION_X,  PIXEL_RESOLUTION_Y);
		
		// Display
		pong.display(g);
	}
}
