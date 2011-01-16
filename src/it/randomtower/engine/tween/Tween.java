package it.randomtower.engine.tween;

import it.randomtower.engine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;

public class Tween {

	private Motion motion;
	private Entity parent;
	private boolean active = true;
	private Vector2f startingPosition;

	/**
	 * Create a tween with given Motion, active by default, use another
	 * constructor instead
	 * 
	 * @param parent
	 * @param motion
	 */
	public Tween(Entity parent, Motion motion) {
		this(parent, motion, true);
	}

	public Tween(Entity parent, Motion motion, boolean active) {
		this.parent = parent;
		this.motion = motion;
		this.active = active;
	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		if (active && !motion.completed) {
			if (startingPosition == null) {
				startingPosition = new Vector2f(parent.x, parent.y);
			}
			Vector2f result = motion.update();
			Log.debug("move to :" + result.toString());
			parent.x = result.x;
			parent.y = result.y;
		} else {
			active = false;
		}
	}

	public void pause() {
		active = false;
	}

	public void start() {
		active = true;
	}

	public void reset() {
		if (startingPosition != null) {
			parent.x = startingPosition.x;
			parent.y = startingPosition.y;
		}
	}

}
