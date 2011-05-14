package it.randomtower.engine.tween;

public class NumTween extends Tween {

	private float value = 0f;
	private float start = 0f;
	private float range = 0f;
	
	
	public NumTween(float from, float to, float duration, TweenerMode type, int easingType) {
		super(duration, type, easingType, true);
		start = value = from;
		range = to - value;
		target = duration;
		easingFunction = easingType;
		mode = type;
		start();
	}
	
	public void update(int delta) {
		super.update(delta);
		value = start + range * t;
	}
	
	public float getValue() {
		return value;
	}

}
