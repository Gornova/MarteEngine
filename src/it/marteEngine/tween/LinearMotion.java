package it.marteEngine.tween;

public class LinearMotion extends Motion {

	private float fromX, fromY;
	private float moveX, moveY;

	public LinearMotion(float fromX, float fromY, float toX, float toY,
			float duration, int easeFunction) {
		this(fromX, fromY, toX, toY, 1.0f, TweenerMode.ONESHOT, duration,
				easeFunction);
	}

	public LinearMotion(float fromX, float fromY, float toX, float toY,
			float speed, TweenerMode type, float duration, int easeFunction) {
		super(duration / speed, type, easeFunction);
		super.setPosition(fromX, fromY);
		this.fromX = fromX;
		this.fromY = fromY;
		this.moveX = toX - fromX;
		this.moveY = toY - fromY;
		start();
	}

	public void update(int delta) {
		if (!active)
			return;

		super.update(delta);
		x = fromX + moveX * t;
		y = fromY + moveY * t;
	}

	@Override
	public void reset() {
		super.reset();
		x = fromX;
		y = fromY;
	}
}
