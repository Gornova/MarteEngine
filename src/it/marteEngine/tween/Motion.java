package it.marteEngine.tween;

import org.newdawn.slick.geom.Vector2f;

/**
 * base class for motion tweens
 * 
 */
public abstract class Motion extends Tween {

	/** the coordinates of the motion */
	protected float x, y;

	public Motion(float duration, TweenerMode type, int easingType) {
		super(duration, type, easingType);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setPosition(float x, float y) {
		setX(x);
		setY(y);
	}

	public Vector2f getPosition() {
		return new Vector2f(x, y);
	}
}
