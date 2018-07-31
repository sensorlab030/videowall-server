package nl.sensorlab.videowall.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import processing.data.JSONObject;

public abstract class Property {
	
	public enum Scope {
		Wall,
		Animation,
	};
	
	/**
	 * Application wide unique id for this property
	 */
	private String id;
	
	/**
	 * Scope for this property, either Scope.Wall or Scope.Animation
	 */
	private Scope scope;
	
	/**
	 * Animation id if this property has Scope.Animation scope
	 */
	private Integer animationId = null;
	
	/**
	 * Unique name for the property within the scope
	 */
	private String name;
	
	/**
	 * Listeners for property creation
	 */
	private List<PropertyListener> propertyListeners = new ArrayList<>();
	
	/** 
	 * Value change listeners for this property
	 */
	private List<PropertyValueListener> valueListeners = new ArrayList<>();
	
	/**
	 * List of all properties in the application
	 */
	private static Map<String, Property> properties = new HashMap<String, Property>();
	
	/**
	 * Initialize a property
	 * 
	 * @param scope			scope of the property (wall or animation)
	 * @param animationId	animation id for animation properties or null for wall properties
	 * @param name			name of the property
	 */
	protected Property(Scope scope, Integer animationId, String name) {
		this.scope = scope;
		this.animationId = animationId;
		this.name = name;
		this.id = createId(scope, animationId, name);
		
		// Add property to property list
		properties.put(this.getId(), this);
		
		// Signal creation of property to all property listeners
		for (PropertyListener listener : propertyListeners) {
			listener.onPropertyAdded(this);
		}
		
	}
	
	/**
	 * Get a list of all properties in the application
	 * @return
	 */
	public static List<Property> getApplicationProperties() {
		return new ArrayList<Property>(properties.values());
	}
	
	/**
	 * Serialize the property to JSONObject
	 * @return
	 */
	final public JSONObject toJson() {
		
		JSONObject obj = new JSONObject();
		
		// Set base properties
		obj.setString("name", name);
		if (scope == Scope.Animation) {
			obj.setString("scope", "animation");
			obj.setInt("animationId", animationId);
		} else {
			obj.setString("scope", "wall");
		}
		
		// Set the value by calling the abstract set method
		setJsonValue("value", obj);
		
		return obj;
	}
	
	/**
	 * Get the unique id for this property
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Get the property name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the value property to the given JSON Object for the 
	 * specialized property with the given key. E.g. an IntProperty
	 * will call object.setInt(key, value);
	 * 
	 * @param object
	 */
	protected abstract void setJsonValue(String key, JSONObject object);
	
	/**
	 * Update the current value (as incoming value from external API)
	 * @param value
	 */
	public abstract void applyValue(Object value);
	
	/**
	 * Emit property change event to all listeners, only
	 * call this method when the value actually has changed to
	 * prevent congestion
	 */
	protected final void emitChangeEvent() {
		for (PropertyValueListener listener : valueListeners) {
			listener.onPropertyChange(this);
		}
	}
	
	/**
	 * Add listener to subscribe to property value events
	 * 
	 * @param listener
	 */
	public void addValueListener(PropertyValueListener listener) {
		valueListeners.add(listener);
		
		// Send initial value
		listener.onPropertyChange(this);
		
	}
	
	/**
	 * Remove listener to unsubscribe from property value events 
	 * 
	 * @param listener
	 */
	public void removeValueListener(PropertyValueListener listener) {
		valueListeners.remove(listener);
	}
	
	/**
	 * Add listener to subscribe to property value events
	 * 
	 * @param listener
	 */
	public void addPropertyListener(PropertyListener listener) {
		propertyListeners.add(listener);
	}
	
	/**
	 * Remove listener to unsubscribe from property value events 
	 * 
	 * @param listener
	 */
	public void removePropertyListener(PropertyListener listener) {
		propertyListeners.remove(listener);
	}

	/**
	 * Create application wide unique id for the given property 
	 * 
	 * @param scope
	 * @param animationId
	 * @param name
	 * @return
	 */
	public static String createId(Scope scope, Integer animationId, String name) {
		
		return String.format("%s%s_%s", 
				scope == Scope.Wall ? "w" : "a", 
						animationId == null ? "" : "_" + animationId.toString(), 
						name);
		
	}
	
	/**
	 * Find property by id in application scope
	 * 
	 * @param id
	 * @return
	 */
	public static Property findProperty(String id) {
		return properties.get(id);
	}
	
	/**
	 * Event listener class to handle the creation of properties
	 */
	public interface PropertyListener {
		void onPropertyAdded(Property property);
	}
	
	/**
	 * Event listener class to handle value changes of the property
	 */
	public interface PropertyValueListener {
		void onPropertyChange(Property property);
	}
	
}
