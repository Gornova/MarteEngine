package it.marteEngine.tween;

public class NumTween extends Tween {

	private float value = 0f;
	private float start = 0f;
	private float range = 0f;

	public NumTween(float from, float to, float duration, TweenerMode type,
			int easingType) {
		super(duration, type, easingType);
		super.target = duration;
		start = value = from;
		range = to - value;
		start();
	}

	public void update(int delta) {
		if (!active)
			return;

		super.update(delta);
		value = start + range * t;
	}

	public float getValue() {
		return value;
	}

}
