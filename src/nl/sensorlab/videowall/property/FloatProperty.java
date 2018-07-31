package nl.sensorlab.videowall.property;

import processing.data.JSONObject;

public class FloatProperty extends Property {
	
	protected float value;
	
	private final static float EPSILON = 0.001f;
	
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
		System.out.println("APPLY VAL" + value);
		setValue(((Number) value).floatValue());
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
		if (!nearlyEqual(this.value, value)) {
			this.value = value;
			emitChangeEvent();
		}
	}
	
	private static boolean nearlyEqual(float a, float b) {
		final float absA = Math.abs(a);
		final float absB = Math.abs(b);
		final float diff = Math.abs(a - b);

		if (a == b) { // shortcut, handles infinities
			return true;
		} else if (a == 0 || b == 0 || diff < Float.MIN_NORMAL) {
			// a or b is zero or both are extremely close to it
			// relative error is less meaningful here
			return diff < (EPSILON * Float.MIN_NORMAL);
		} else { // use relative error
			return diff / Math.min((absA + absB), Float.MAX_VALUE) < EPSILON;
		}
	}

}
