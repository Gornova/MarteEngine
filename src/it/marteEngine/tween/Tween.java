package it.marteEngine.tween;

import it.marteEngine.entity.Entity;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;

/**
 * Tween can change Entity status
 * 
 * @author Gornova
 */
public class Tween {

	private Motion motion;
	private boolean active = true;
	private Vector2f startingPosition;

	/**
	 * Create a tween with given Motion, active by default, use another
	 * constructor instead
	 * 
	 * @param motion
	 */
	public Tween(Motion motion) {
		this(motion, true);
	}

	/**
	 * Create a tween with given Motion and specify if is active
	 * 
	 * @param motion
	 * @param active
	 */
	public Tween(Motion motion, boolean active) {
		this.motion = motion;
		this.active = active;
	}

	/**
	 * Get updated position from Tween
	 * 
	 * @param parent
	 * @return updated x and y, null if not active
	 */
	public Vector2f apply(Entity parent) {
		if (active && !motion.completed) {
			if (startingPosition == null) {
				startingPosition = new Vector2f(parent.x, parent.y);
			}
			Vector2f result = motion.update();
			Log.debug("move to :" + result.toString());
			return result;
		} else {
			active = false;
			return null;
		}
	}

	/**
	 * Start tween
	 */
	public void start() {
		active = true;
	}

	/**
	 * Pause tween
	 */
	public void pause() {
		active = false;
	}

	/**
	 * Reset tween to initial position
	 * 
	 * @return initial position of tween
	 */
	public Vector2f reset() {
		if (startingPosition != null) {
			active = false;
			return startingPosition;
		}
		return null;
	}

	/**
	 * @return true if tween is active
	 */
	public boolean isActive() {
		return active;
	}
	
	public void setStartPosition(float x, float y){
		startingPosition = new Vector2f(x,y);
		motion.fromX = x;
		motion.fromY = y;
	}

}
