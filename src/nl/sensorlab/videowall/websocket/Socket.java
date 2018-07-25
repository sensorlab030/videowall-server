package nl.sensorlab.videowall.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import nl.sensorlab.videowall.AnimationManager;
import nl.sensorlab.videowall.AnimationManager.AnimationEntry;
import nl.sensorlab.videowall.AnimationManager.AnimationEventListener;
import nl.sensorlab.videowall.LedWallApplication;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Socket extends WebSocketAdapter implements AnimationEventListener {
	
	private final static HashMap<String, Socket> clientSockets = new HashMap<>();
	private Session session;
	private String uuid;
	
	@Override
	public void onWebSocketConnect(Session sess) {
		super.onWebSocketConnect(sess);
		
		// Store/setup session
		this.session = sess;
		this.uuid = UUID.randomUUID().toString();
		
		// Add to clients
		clientSockets.put(uuid, this);
		
		// Connect to events
		AnimationManager.getInstance().addListener(this);
		
		System.out.println("Socket Connected: " + sess + " " + uuid);
		
		// Send the current configuration
		sendWallConfiguration();
		
	}

	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);
		System.out.println("Received TEXT message: " + message);
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		System.out.println("Socket Closed: [" + statusCode + "] " + reason);
		clientSockets.remove(uuid);
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		cause.printStackTrace(System.err);
	}

	@Override
	public void onCurrentAnimationChanged(int index) {
		try {
			System.out.println("SOCKET SEND EVENT CHNG");
			session.getRemote().sendString("YOLO : " + index);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send the static configuration of the wall to the client
	 */
	public void sendWallConfiguration() {
		
		JSONObject data = new JSONObject();
		
		// Add wall properties
		JSONObject wallProperties = new JSONObject();
		wallProperties.setString("server-version", LedWallApplication.VERSON_STRING);
		data.put("wall", wallProperties);
		
		// Animations
		JSONArray animationList = new JSONArray();
		for (AnimationEntry entry : AnimationManager.getInstance().getAvailableAnimations()) {
			JSONObject animationObject = new JSONObject();
			animationObject.setInt("id", entry.getId());
			animationObject.setString("name", entry.getLabel());
			animationObject.setString("description", "Lorem ipsum for " + entry.getLabel());
			animationList.append(animationObject);
		}
		data.put("animations", animationList);
		
		sendJsonData("config", data);

	}
	
	private void sendJsonData(String message, JSONObject data) {
		
		try {
			
			JSONObject msg = new JSONObject();
			msg.setString("msg", message);
			msg.setJSONObject("data", data);
			System.out.println("SENDING" + msg.toString());
			session.getRemote().sendString(msg.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void disconnect() throws IOException {
		this.session.disconnect();
	}
	
	public static void disconnectAllSockets() throws IOException {
		for (Socket s: clientSockets.values()) {
			s.disconnect();
		}
	}

}
