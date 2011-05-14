package it.randomtower.engine.tween;


/**
 * base class for motion tweens
 *
 */
public abstract class Motion extends Tween {

	/** the coordinates of the motion */
	protected float x, y;
	
	public Motion(float duration, TweenerMode type, int easingType, boolean active) {
		super(duration, type, easingType, active);
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


}
