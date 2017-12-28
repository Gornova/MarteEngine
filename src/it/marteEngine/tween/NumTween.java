package it.marteEngine.tween;

public class NumTween extends Tween {

	private float value;
	private float start;
	private float range;

	public NumTween(float from, float to, float duration, TweenerMode type,
			Ease easingType, boolean deltaTiming) {
		super(duration, type, easingType, deltaTiming, true);
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
