package it.marteEngine.entity;

import it.marteEngine.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class PlatformerEntity extends PhysicsEntity {

	protected static final String CMD_LEFT = "left";
	protected static final String CMD_RIGHT = "right";
	protected static final String CMD_JUMP = "jump";
	protected boolean onGround = false;
	private int moveSpeed = 1;
	private int jumpSpeed = 6;

	/**
	 * Create a new PlatformerEntity able to jump and move around. Create a
	 * default hitbox on image
	 * 
	 * @param x
	 * @param y
	 * @param ref
	 *            , name of resource into resource.xml or path to image
	 * @throws SlickException
	 *             if image is not found
	 */
	public PlatformerEntity(float x, float y, String ref) throws SlickException {
		super(x, y);

		currentImage = ResourceManager.getImage(ref);
		if (currentImage == null) {
			currentImage = new Image(ref);
		}
		setHitBox(0, 0, currentImage.getWidth(), currentImage.getHeight());
		depth = 10;
		defineControls();
	}

	public PlatformerEntity(float x, float y, int width, int height)
			throws SlickException {
		super(x, y);

		setHitBox(0, 0, width, height);
		depth = 10;
		defineControls();
	}

	/**
	 * Define standard platformer controls (CMD_JUMP, CMD_RIGHT, CMD_LEFT)
	 * Override it to change default controls
	 */
	public void defineControls() {
		define(CMD_JUMP, Input.KEY_UP, Input.KEY_X, Input.KEY_W);
		define(CMD_RIGHT, Input.KEY_RIGHT, Input.KEY_D);
		define(CMD_LEFT, Input.KEY_LEFT, Input.KEY_A);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		// are we on the ground?
		onGround = false;
		if (collide(SOLID, x, y + 1) != null) {
			onGround = true;
		}

		// set acceleration to nothing
		acceleration.x = 0;

		// increase acceeration, if we're not going too fast
		if (check(CMD_LEFT) && speed.x > -maxSpeed.x) {
			acceleration.x = -moveSpeed;
		}
		if (check(CMD_RIGHT) && speed.x < maxSpeed.x) {
			acceleration.x = moveSpeed;
		}

		// friction (apply if we're not moving, or if our speed.x is larger than
		// maxspeed)
		if ((!check(CMD_LEFT) && !check(CMD_RIGHT))
				|| Math.abs(speed.x) > maxSpeed.x) {
			friction(true, false);
		}

		// jump
		if (pressed(CMD_JUMP)) {
			// normal jump
			if (onGround) {
				jump();
			}
		}

		// set the gravity
		gravity(delta);

		// make sure we're not going too fast vertically
		// the reason we don't stop the player from moving too fast left/right
		// is because
		// that would (partially) destroy the walljumping. Instead, we just make
		// sure the player,
		// using the arrow keys, can't go faster than the max speed, and if we
		// are going faster
		// than the max speed, descrease it with friction slowly.
		maxspeed(false, true);

		// variable jumping (tripple gravity)
		if (speed.y < 0 && !check(CMD_JUMP)) {
			gravity(delta);
			gravity(delta);
		}

		// set the motion. We set this later so it stops all movement if we
		// should be stopped
		motion(true, true);

		previousx = x;
		previousy = y;
	}

	public void jump() {
		speed.y = -jumpSpeed;
	}

}
