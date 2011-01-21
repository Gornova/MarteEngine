package it.randomtower.engine.tween;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;

public abstract class Motion {

	public float fromX;
	public float fromY;
	public float toX;
	public float toY;

	protected int duration;

	protected float lastX;
	protected float lastY;

	protected float timer;

	protected float t;

	public boolean completed;

	protected int easeFunction = -1;

	public Motion(float fromX, float fromY, float toX, float toY, int duration,
			int easeFunction) {
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
		this.duration = duration;

		this.lastX = fromX;
		this.lastY = fromY;

		completed = false;

		this.easeFunction = easeFunction;
	}

	public Vector2f update() {
		timer++;
		t = timer / duration;

		if (easeFunction > -1) {
			t = ease(t);
		}

		if (lastX == toX && lastY == toY) {
			completed = true;
			timer = 0;
			return new Vector2f(toX, toY);
		}
		return new Vector2f();
	}

	private float ease(float t) {
		switch (easeFunction) {
		case Ease.QUAD_IN:
			return Ease.quadIn(t);
		case Ease.QUAD_OUT:
			return Ease.quadOut(t);
		case Ease.QUAD_IN_OUT:
			return Ease.quadInOut(t);
		case Ease.CUBE_IN:
			return Ease.cubeIn(t);
		case Ease.CUBE_OUT:
			return Ease.cubeOut(t);
		case Ease.CUBE_IN_OUT:
			return Ease.cubeInOut(t);
		case Ease.QUART_IN:
			return Ease.quartIn(t);
		case Ease.QUART_OUT:
			return Ease.quartOut(t);
		case Ease.QUART_IN_OUT:
			return Ease.quartInOut(t);
		case Ease.QUINT_IN:
			return Ease.quintIn(t);
		case Ease.QUINT_OUT:
			return Ease.quintOut(t);
		case Ease.QUINT_IN_OUT:
			return Ease.quintInOut(t);
		case Ease.SINE_IN:
			return Ease.sineIn(t);
		case Ease.SINE_OUT:
			return Ease.sineOut(t);
		case Ease.SINE_IN_OUT:
			return Ease.sineInOut(t);
		case Ease.BOUNCE_IN:
			return Ease.bounceIn(t);
		case Ease.BOUNCE_OUT:
			return Ease.bounceOut(t);
		case Ease.BOUNCE_IN_OUT:
			return Ease.bounceInOut(t);
		case Ease.CIRC_IN:
			return Ease.circIn(t);
		case Ease.CIRC_OUT:
			return Ease.circOut(t);
		case Ease.CIRC_IN_OUT:
			return Ease.circInOut(t);
		case Ease.EXPO_IN:
			return Ease.expoIn(t);
		case Ease.EXPO_OUT:
			return Ease.expoOut(t);
		case Ease.EXPO_IN_OUT:
			return Ease.expoInOut(t);
		case Ease.BACK_IN:
			return Ease.backIn(t);
		case Ease.BACK_OUT:
			return Ease.backOut(t);
		case Ease.BACK_IN_OUT:
			return Ease.backInOut(t);
		default:
			Log.warn("Easing function not mapped " + easeFunction);
			break;
		}
		return 0;
	}

}
