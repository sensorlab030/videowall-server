package nl.sensorlab.videowall.property;

import processing.data.JSONObject;

public class IntProperty extends Property {
	
	private int value;
	
	public static IntProperty wallProperty(String name) {
		return new IntProperty(Scope.Wall, null, name);
	}
	
	public static IntProperty animationProperty(int animationId, String name) {
		return new IntProperty(Scope.Animation, animationId, name);
	}
	
	protected IntProperty(Scope scope, Integer animationId, String name) {
		super(scope, animationId, name);
	}
	
	@Override
	protected void setJsonValue(String key, JSONObject object) {
		object.setInt(key, value);
	}
	
	
	@Override
	public void applyValue(Object value) {
		setValue((Integer) value);
	}
	
	/**
	 * Get the current value
	 * @return
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Update the current value
	 * @param value
	 */
	public void setValue(int value) {
		if (this.value != value) {
			this.value = value;
			emitChangeEvent();
		}
	}
	
}
