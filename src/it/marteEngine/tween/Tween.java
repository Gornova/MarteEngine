package it.marteEngine.tween;

import it.marteEngine.ME;

/**
 * Tween allows an object to move with smooth acceleration and deceleration. It
 * does not change the object directly, instead the object should update it's
 * position based on the position of the tween.
 * 
 * @author Gornova
 */
public abstract class Tween {

	public enum TweenerMode {
		ONESHOT, LOOPING, PERSIST
	}

	protected String name = null;
	protected TweenerMode mode = TweenerMode.PERSIST;

	protected Tweener parent = null;
	protected boolean active = true;
	protected boolean finished = false;

	protected float t = 0f;

	/** how much time proceeded */
	private float time = 0f;
	/** when is this tweener done */
	protected float target = 0f;

	/** which easing function does this Tween use */
	protected int easingFunction = Ease.NONE;

	public Tween(float duration, TweenerMode mode, int easingType) {
		this.target = duration;
		this.mode = mode;
		this.easingFunction = easingType;
	}

	public void update(int delta) {
		if (!active)
			return;

		if (ME.useDeltaTiming)
			time += delta;
		else
			time++;
		t = time / target;
		if (easingFunction != Ease.NONE && t > 0 && t < 1)
			t = Ease.ease(easingFunction, t);
		if (time > target) {
			t = 1;
			finished = true;
		}
	}

	/**
	 * Start tween
	 */
	public void start() {
		active = true;
		time = 0;
		if (target == 0)
			active = false;
	}

	/**
	 * Pause tween
	 */
	public void pause() {
		active = false;
	}

	/**
	 * Reset tween to initial position
	 */
	public void reset() {
		time = 0;
		active = false;
	}

	/**
	 * internal function, called by the Tweener if this Tween is finished
	 */
	void finish() {
		switch (mode) {
			case PERSIST :
				time = target;
				active = false;
				break;
			case ONESHOT :
				time = target;
				active = false;
				if (parent != null)
					parent.remove(this);
				break;
			case LOOPING :
				time %= target;
				t = time / target;
				break;
		}
		finished = false;
	}

	public float getValue() {
		return t; // after the ease function has been applied
	}

	/**
	 * how many percent is this tween done? value between 0 and 1
	 * 
	 * @return a value between 0 and 1 where 1 means hundred percent
	 */
	public float getPercentage() {
		return time / target;
	}

	/**
	 * sets the progress of this tween.
	 * 
	 * @param val
	 *            must be a value between 0 and 1.
	 */
	public void setPercentage(float val) {
		time = target * val;
	}

	/**
	 * @return true if tween is active
	 */
	public boolean isActive() {
		return active;
	}

	public TweenerMode getMode() {
		return mode;
	}

	public void setMode(TweenerMode mode) {
		this.mode = mode;
	}

	public Tweener getParent() {
		return parent;
	}

	public void setParent(Tweener parent) {
		this.parent = parent;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isFinished() {
		return finished;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
