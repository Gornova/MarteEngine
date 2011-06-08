package it.marteEngine.test.fuzzy;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.PhysicsEntity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class FuzzyGreenSlime extends PhysicsEntity {

	public static final String SLIME = "slime";
	// private Sound jumpSnd;

	private boolean onGround = false;
	private float moveSpeed = 0.5f;
	private int jumpSpeed = 6;

	private boolean right = true;

	public FuzzyGreenSlime(float x, float y, String ref) throws SlickException {
		super(x, y);
		addAnimation(ResourceManager.getSpriteSheet("slime"), "move", true, 0,
				0, 1, 2, 3);

		addType(SLIME);
		setHitBox(0, 0, 40, 22);

		// jumpSnd = ResourceManager.getSound("jump");
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		// are we on the ground?
		onGround = false;
		if (collide(SOLID, x+width, y + 1) != null) {
			onGround = true;
		} 

		// set acceleration to nothing
		acceleration.x = 0;

		if (collide(SOLID, x + 1, y) == null) {
			right = right ? false : true;
		} else if (collide(SOLID, x - 1, y) == null) {
			right = right ? false : true;
		}

		if (!onGround){
			right = false;
		}
		
		if (right) {
			if (speed.x < maxSpeed.x) {
				acceleration.x = moveSpeed;
			}
		} else {
			if (speed.x > -maxSpeed.x) {
				acceleration.x = -moveSpeed;
			}
		}
		

		// increase acceeration, if we're not going too fast
		// if (check(CMD_LEFT) && speed.x > -maxSpeed.x) {
		// acceleration.x = - moveSpeed;
		// }
		// if (check(CMD_RIGHT) && speed.x < maxSpeed.x) {
		// acceleration.x = moveSpeed;
		// }

		// friction (apply if we're not moving, or if our speed.x is larger than
		// maxspeed)
		// if ( (! check(CMD_LEFT) && ! check(CMD_RIGHT)) || Math.abs(speed.x) >
		// maxSpeed.x ) {
		// friction(true, false);
		// }

		// jump
		// if ( pressed(CMD_JUMP) )
		// {
		// //normal jump
		// if (onGround) {
		// speed.y = -jumpSpeed;
		// }
		// }

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
		// if (speed.y < 0 && !check(CMD_JUMP)) {
		// gravity(delta);
		// gravity(delta);
		// }

		// set the motion. We set this later so it stops all movement if we
		// should be stopped
		motion(true, true);

		previousx = x;
		previousy = y;
	}

}
