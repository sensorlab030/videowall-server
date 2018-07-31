package nl.sensorlab.videowall.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import nl.sensorlab.videowall.AnimationManager;
import nl.sensorlab.videowall.AnimationManager.AnimationEntry;
import nl.sensorlab.videowall.LedWallApplication;
import nl.sensorlab.videowall.property.Property;
import nl.sensorlab.videowall.property.Property.PropertyListener;
import nl.sensorlab.videowall.property.Property.PropertyValueListener;
import nl.sensorlab.videowall.property.Property.Scope;
import processing.data.JSONArray;
import processing.data.JSONObject;

@WebSocket(maxIdleTime=3)
public class Socket extends WebSocketAdapter implements PropertyValueListener, PropertyListener {
	
	private final static HashMap<String, Socket> clientSockets = new HashMap<>();
	private Session session;
	private String uuid;
	
	@Override
	public void onWebSocketConnect(Session sess) {
		super.onWebSocketConnect(sess);
		
		// Store/setup session
		this.session = sess;
		this.uuid = UUID.randomUUID().toString();
		clientSockets.put(uuid, this);
		
		// Send the current configuration
		sendWallConfiguration();
		
		// Connect client to all property changes
		for (Property p: Property.getApplicationProperties()) {
			p.addValueListener(this);
		}
		
	}

	@Override
	public void onWebSocketText(String message) {
		super.onWebSocketText(message);

		JSONObject msg = JSONObject.parse(message);
		if (msg.getString("msg").equals("props")) {
			JSONArray props = msg.getJSONArray("data");
			for (int i = 0; i < props.size(); i++) {
				JSONObject property = props.getJSONObject(i);

				try {

					// Parse property
					String scopeVal = property.getString("scope", null);
					if (scopeVal == null) {
						throw new Exception("invalid 'scope' property");
					}
					Scope scope = (scopeVal.equals("wall")) ? Scope.Wall : Scope.Animation;
					
					Integer animationId = null;
					if (scope == Scope.Animation) {
						animationId = property.getInt("animationId", -1);
						if (animationId == -1) {
							throw new Exception("invalid 'animationId' property");
						}
					}
					
					String name = property.getString("name", null);
					if (name == null) {
						throw new Exception("invalid 'name' property");
					}
					
					Object value = property.get("value");
					if (value == null) {
						throw new Exception("invalid 'value' property");
					}
					
					// Fetch property
					String propertyId = Property.createId(scope, animationId, name);
					Property p = Property.findProperty(propertyId);
					
					// Apply the value
					p.applyValue(value);

				} catch (Exception e) {
					System.err.println("Failed to parse property: " + e.getMessage());
				}
			}
		}

	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		super.onWebSocketClose(statusCode, reason);
		
		// Disconnect from listeners
		for (Property p: Property.getApplicationProperties()) {
			p.removeValueListener(this);
		}
		
		clientSockets.remove(uuid);
		
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		super.onWebSocketError(cause);
		cause.printStackTrace(System.err);
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
	
	public void sendProperty(Property p) {
		ArrayList<Property> properties = new ArrayList<>();
		properties.add(p);
		sendProperties(properties);
	}
	
	public void sendProperties(List<Property> properties) {
		JSONArray data = new JSONArray();
		for (Property property : properties) {
			data.append(property.toJson());
		}
		sendJsonData("props", data);
	}
	
	private void sendJsonData(String message, JSONObject data) {
		
		try {
			
			JSONObject msg = new JSONObject();
			msg.setString("msg", message);
			msg.setJSONObject("data", data);
			session.getRemote().sendString(msg.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void sendJsonData(String message, JSONArray data) {
		try {
			JSONObject msg = new JSONObject();
			msg.setString("msg", message);
			msg.setJSONArray("data", data);
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

	@Override
	public void onPropertyAdded(Property property) {
		sendProperty(property);
	}

	@Override
	public void onPropertyChange(Property property) {
		sendProperty(property);
	}

}
