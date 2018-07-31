package nl.sensorlab.videowall.property;

import processing.data.JSONObject;

public class FloatProperty extends Property {
	
	protected float value;
	
	public static FloatProperty wallProperty(String name) {
		return new FloatProperty(Scope.Wall, null, name);
	}
	
	public static FloatProperty animationProperty(int animationId, String name) {
		return new FloatProperty(Scope.Animation, animationId, name);
	}
	
	protected FloatProperty(Scope scope, Integer animationId, String name) {
		super(scope, animationId, name);
	}

	@Override
	protected void setJsonValue(String key, JSONObject object) {
		object.setDouble(key, value);
	}

	@Override
	public void applyValue(Object value) {
		setValue((Float) value);
	}
	
	/**
	 * Get the current value
	 * @return
	 */
	public float getValue() {
		return value;
	}
	
	/**
	 * Update the current value
	 * @param value
	 */
	public void setValue(float value) {
		if (this.value != value) {
			this.value = value;
			emitChangeEvent();
		}
	}

}
