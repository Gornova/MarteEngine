package it.marteEngine.entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.Log;

/**
 * this class only works with a fixed frame rate of 60. All calculations are
 * based on that assumption. It's okay for this little sample game
 * 
 * @author Thomas
 * 
 */
public class PhysicsEntity extends Entity {

	/** speed vector (x,y) **/
	public Vector2f speed = new Vector2f(0, 0);

	/** acceleration vector (x,y) **/
	public Vector2f acceleration = new Vector2f(0, 0);

	public float gravity = 0.2f;

	public Vector2f friction = new Vector2f(0.5f, 0.5f);

	/** height of a slope a PhysicsEntity can run up */
	public int slopeHeight = 1;

	public Vector2f maxSpeed = new Vector2f(3, 8);

	public PhysicsEntity(float x, float y) {
		super(x, y);
		// physics entities are solid
		addType(SOLID);
	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		// update possible animation stuff
		super.update(container, delta);
		motion(true, true);
		gravity(delta);

	}

	/**
	 * Moves this entity at it's current speed (speed.x, speed.y) and increases
	 * speed based on acceleration (acceleration.x, acceleration.y)
	 * 
	 * @param moveX
	 *            include horizontal movement
	 * @param moveY
	 *            include vertical movement
	 */
	public void motion(boolean moveX, boolean moveY) {
		// if we should move horizontally
		if (moveX) {
			// make us move, and stop us on collision
			if (!motionx(this, speed.x)) {
				speed.x = 0;
			}

			// increase velocity/speed
			speed.x += acceleration.x;
		}

		// if we should move vertically
		if (moveY) {
			// make us move, and stop us on collision
			if (!motiony(this, speed.y)) {
				speed.y = 0;
			}

			// increase velocity/speed
			speed.y += acceleration.y;
		}
	}

	/**
	 * Increases this entity's vertical speed, based on its gravity (gravity)
	 * 
	 * @return void
	 */
	public void gravity(int delta) {
		// increase velocity/speed based on gravity
		speed.y += gravity;
	}

	/**
	 * Slows this entity down, according to its friction (friction.x,
	 * friction.y)
	 * 
	 * @param mx
	 *            Include horizontal movement
	 * @param my
	 *            Include vertical movement
	 * @return void
	 */
	public void friction(boolean mx, boolean my) {
		// if we should use friction, horizontally
		if (mx) {
			// speed > 0, then slow down
			if (speed.x > 0) {
				speed.x -= friction.x;
				// if we go below 0, stop.
				if (speed.x < 0) {
					speed.x = 0;
				}
			}
			// speed < 0, then "speed up" (in a sense)
			if (speed.x < 0) {
				speed.x += friction.x;
				// if we go above 0, stop.
				if (speed.x > 0) {
					speed.x = 0;
				}
			}
		}

	}

	/**
	 * Stops entity from moving to fast, according to maxspeed (mMaxspeed.x,
	 * mMaxspeed.y)
	 * 
	 * @param mx
	 *            Include horizontal movement
	 * @param my
	 *            Include vertical movement
	 * @return void
	 */
	public void maxspeed(boolean mx, boolean my) {
		if (mx) {
			if (Math.abs(speed.x) > maxSpeed.x) {
				speed.x = maxSpeed.x * Math.signum(speed.x);
			}
		}

		if (my) {
			if (Math.abs(speed.y) > maxSpeed.y) {
				speed.y = maxSpeed.y * Math.signum(speed.y);
			}
		}
	}

	/**
	 * Moves the set entity horizontal at a given speed, checking for collisions
	 * and slopes
	 * 
	 * @param e
	 *            The entity you want to move
	 * @param spdx
	 *            The speed at which the entity should move
	 * @return True (didn't hit a solid) or false (hit a solid)
	 */
	public boolean motionx(Entity e, float spdx) {
		// check each pixel before moving it
		for (int i = 0; i < Math.abs(spdx); i++) {
			// if we've moved
			boolean moved = false;
			boolean below = true;

			if (e.collide(SOLID, e.x, e.y + 1) == null) {
				below = false;
			}

			// run through how high a slope we can move up
			for (int s = 0; s <= slopeHeight; s++) {
				// if we don't hit a solid in the direction we're moving,
				// move....
				if (e.collide(SOLID, e.x + Math.signum(spdx), e.y - s) == null) {
					// increase/decrease positions
					// if the player is in the way, simply don't move (but don't
					// count it as stopping)
					if (e.collide(PLAYER, e.x + Math.signum(spdx), e.y - s) == null) {
						e.x += Math.signum(spdx);
					}

					// move up the slope
					e.y -= s;

					// we've moved
					moved = true;

					// stop checking for slope (so we don't fly up into the
					// air....)
					break;
				}

			}

			// if we are now in the air, but just above a platform, move us
			// down.
			if (below && e.collide(SOLID, e.x, e.y + 1) == null) {
				e.y += 1;
			}

			// if we haven't moved, set our speed to 0
			if (!moved) {
				return false;
			}
		}

		// hit nothing!
		return true;
	}

	/**
	 * Moves the set entity vertical at a given speed, checking for collisions
	 * 
	 * @param e
	 *            The entity you want to move
	 * @param spdy
	 *            The speed at which the entity should move
	 * @return True (didn't hit a solid) or false (hit a solid)
	 */
	public boolean motiony(Entity e, float spdy) {
		// for each pixel that we will move...
		for (int i = 0; i < Math.abs(spdy); i++) {
			// if we DON'T collide with solid
			if (e.collide(SOLID, e.x, e.y + Math.signum(spdy)) == null) {
				// if we don't run into a player, them move us
				if (e.collide(PLAYER, e.x, e.y + Math.signum(spdy)) == null) {
					e.y += Math.signum(spdy);
				}
				// but note that we wont stop our movement if we hit a player.
			} else {
				// stop movement if we hit a solid
				return false;
			}
		}

		// hit nothing!
		return true;
	}

	/**
	 * Moves an entity of the given type that is on top of this entity (if any).
	 * Also moves player if it's on top of the entity on top of this one.
	 * (confusing.. eh?). Mostly used for moving platforms
	 * 
	 * @param type
	 *            Entity type to check for
	 * @param speed
	 *            The speep at which to move the thing above you
	 * @return void
	 */
	public void moveontop(String type, float speed) {
		Entity e = collide(type, x, y - 1);
		if (e != null) {
			motionx(e, speed);

			// if the player is on top of the thing that's being moved, move
			// him/her too.
			PhysicsEntity p = null;
			try {
				p = (PhysicsEntity) e;
			} catch (ClassCastException cce) {
				Log.debug("PhysicsEntity.moveontop(): failed to cast entity '"
						+ e.toString() + "' to physics entity!");
			}
			if (p != null) {
				p.moveontop(PLAYER, speed);
			}
		}
	}

}
