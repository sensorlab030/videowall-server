package nl.sensorlab.videowall.websocket;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
public class Servlet extends WebSocketServlet {

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(Socket.class);
	}

}
