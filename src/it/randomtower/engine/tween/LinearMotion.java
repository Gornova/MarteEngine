package it.randomtower.engine.tween;

import org.newdawn.slick.geom.Vector2f;

public class LinearMotion extends Motion {

	private float fromX;
	private float fromY;
	private float toX;
	private float toY;
	private float moveX;
	private float moveY;
	private float speed = 1.0f;
	
	public LinearMotion(float fromX, float fromY, float toX, float toY,
			float duration, int easeFunction) {
		this(fromX,fromY,toX,toY,1.0f, TweenerMode.ONESHOT, duration,easeFunction);
	}

	public LinearMotion(float fromX, float fromY, float toX, float toY,
			float speed, float duration, int easeFunction) {
		this(fromX, fromY, toX, toY, speed, TweenerMode.ONESHOT, duration, easeFunction);
	}

	public LinearMotion(float fromX, float fromY, float toX, float toY,
			float speed, TweenerMode type, float duration, int easeFunction) {
		super(duration, type, easeFunction, true);
		this.x = this.fromX = fromX;
		this.y = this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
		this.moveX = this.toX - fromX;
		this.moveY = this.toY - fromY;
		this.speed = speed;
		this.target = duration / this.speed;
		this.easingFunction = easeFunction;
		this.mode = type;
		this.start();
	}

	public void update(int delta) {
		super.update(delta);
		x = fromX + moveX * t;
		y = fromY + moveY * t;
	}
	
	public Vector2f getPosition() {
		return new Vector2f(x,y);
	}

}
