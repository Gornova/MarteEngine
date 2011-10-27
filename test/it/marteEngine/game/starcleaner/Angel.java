package it.marteEngine.game.starcleaner;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import it.marteEngine.entity.PhysicsEntity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;

/**
 * this class only works with a fixed frame rate of 60. All calculations are
 * based on that assumption. It's okay for this little sample game
 * 
 * @author Thomas
 * 
 */
public class Angel extends PhysicsEntity {

	public static final String ANGEL = "angel";

	// constants for the commands
	public static final String CMD_LEFT = "left";
	public static final String CMD_RIGHT = "right";
	public static final String CMD_JUMP = "jump";

	public int moveSpeed = 1;
	public int jumpSpeed = 8;
	public boolean faceRight = true;

	public boolean onGround = false;

	// did we jump or just fell off some solid?
	public boolean didJump = false;

	// are we walljumping? (0 = no, 1 = left, 2 = right)
	public int wallJumping = 0;
	// can we double jump? (false = no, true = yes)
	public boolean doubleJump = false;

	public boolean dead = false;
	private Light myLight = null;

	public static String[] enemies = { Spikes.SPIKES, Crow.CROW };

	private Sound jumpSnd, hitSnd, pickupstarSnd, allPickedUpSnd;

	private int frame = 0;
	private int frameTimer = 0;
	private int frameInterval = 100; // switch frame every 100 ms if walking

	public Angel(float x, float y, Light light) {
		super(x, y);
		depth = 10;
		this.name = ANGEL;
		this.addType(Entity.PLAYER);
		sheet = ResourceManager.getSpriteSheet(ANGEL);
		currentImage = sheet.getSprite(0, 0);
		myLight = light;
		width = 40;
		height = 40;

		// set different speeds and such
		gravity = 0.4f;
		maxSpeed = new Vector2f(4, 8);
		Globals.originalPlayerMaxSpeed = new Vector2f(maxSpeed);
		friction = new Vector2f(0.5f, 0.5f);
		// set hitbox
		setHitBox(6, 2, 24, 38);
		defineCommands();
		// set sounds
		// bumpSnd = ResourceManager.getSound("bump");
		jumpSnd = ResourceManager.getSound("jump");
		hitSnd = ResourceManager.getSound("hit");
		pickupstarSnd = ResourceManager.getSound("pickupstar");
		allPickedUpSnd = ResourceManager.getSound("allpickedup");
	}

	private void defineCommands() {
		define(CMD_JUMP, Input.KEY_X, Input.KEY_W, Input.KEY_UP);
		define(CMD_LEFT, Input.KEY_LEFT, Input.KEY_A);
		define(CMD_RIGHT, Input.KEY_RIGHT, Input.KEY_D);

	}

	public void update(GameContainer container, int delta)
			throws SlickException {

		// are we on the ground?
		onGround = false;
		if (collide(SOLID, x, y + 1) != null) {
			onGround = true;
			wallJumping = 0;
			doubleJump = true;
			didJump = false;
		}

		// set acceleration to nothing
		acceleration.x = 0;

		// increase acceeration, if we're not going too fast
		if (check(CMD_LEFT) && speed.x > -maxSpeed.x) {
			acceleration.x = -moveSpeed;
			faceRight = false;
		}
		if (check(CMD_RIGHT) && speed.x < maxSpeed.x) {
			acceleration.x = moveSpeed;
			faceRight = true;
		}

		// friction (apply if we're not moving, or if our speed.x is larger than
		// maxspeed)
		if ((!check(CMD_LEFT) && !check(CMD_RIGHT))
				|| Math.abs(speed.x) > maxSpeed.x) {
			friction(true, false);
		}

		// jump
		if (pressed(CMD_JUMP)) {
			boolean jumped = false;

			// normal jump
			if (onGround) {
				speed.y = -jumpSpeed;
				jumped = true;
				didJump = true;
				jumpSnd.play();
			}

			// wall jump
			if ((collide(SOLID, x - 1, y) != null) && !jumped
					&& wallJumping != 3) {
				speed.y = -jumpSpeed; // jump up
				speed.x = maxSpeed.x * 2; // move right fast
				wallJumping = 2; // and set wall jump direction
				jumped = true; // so we don't "use up" or double jump
				jumpSnd.play();
			}
			// same as above
			if ((collide(SOLID, x + 1, y) != null) && !jumped
					&& wallJumping != 3) {
				speed.y = -jumpSpeed;
				speed.x = -maxSpeed.x * 2;
				wallJumping = 1;
				jumped = true;
				jumpSnd.play();
			}

			// set double jump to false
			if (!onGround && !jumped && didJump && doubleJump) {
				speed.y = -jumpSpeed;
				doubleJump = false;
				// set walljumping to 0 so we can move back in any direction
				// again
				// incase we were wall jumping prior to this double jump.
				// if you don't want to allow walljumping after a double jump,
				// set this to 3.
				wallJumping = 0;
			}
		}

		// if we ARE walljumping, make sure we can't go back
		if (wallJumping > 0) {
			if (wallJumping == 2 && speed.x < 0) {
				speed.x = 0;
			}
			if (wallJumping == 1 && speed.x > 0) {
				speed.x = 0;
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

		// set the sprites according to if we're on the ground, and if we are
		// moving or not
		if (onGround) {
			if (speed.x != 0) {
				// update frametimer
				frameTimer -= delta;
				while (frameTimer < 0) {
					frame++;
					frameTimer += frameInterval;
					if (frame > 1) // just two frames for angel's walk animation
						frame = 0;
				}
			}
			if (speed.x < 0) {
				currentImage = sheet.getSprite(frame, 0).getFlippedCopy(true,
						false);
				// sprPlayer.play("walkLeft");
			}
			if (speed.x > 0) {
				currentImage = sheet.getSprite(frame, 0);
				// sprPlayer.play("walkRight");
			}

			if (speed.x == 0) {
				if (faceRight)
					currentImage = sheet.getSprite(0, 0);
				else
					currentImage = sheet.getSprite(0, 0).getFlippedCopy(true,
							false);
				// if (direction) { sprPlayer.play("standRight"); } else {
				// sprPlayer.play("standLeft"); }
			}
		} else {
			if (faceRight)
				currentImage = sheet.getSprite(2, 0);
			else
				currentImage = sheet.getSprite(2, 0)
						.getFlippedCopy(true, false);
			// if (direction) { sprPlayer.play("jumpRight"); } else {
			// sprPlayer.play("jumpLeft"); }

			// are we sliding on a wall?
			// if (collide(solid, x - 1, y)) { sprPlayer.play("slideRight"); }
			// if (collide(solid, x + 1, y)) { sprPlayer.play("slideLeft"); }
		}

		// set the motion. We set this later so it stops all movement if we
		// should be stopped
		motion(true, true);

		// update our light to our position
		if (previousx != x || previousy != y)
			myLight.setLocation(x + StarCleaner.TILESIZE / 2, y
					+ StarCleaner.TILESIZE / 2);

		// did we collect a star?
		Entity star = collide(Star.STAR_TYPE, x, y);
		if (star != null)
			pickupstarSnd.play();

		Entity sunOrMoon = collide(Sun.SUN_AND_MOON, x, y);

		// did we collide with a spike or a crow?
		if (collide(enemies, x, y) != null) {
			hitSnd.play();
			Globals.playerDead = true;
		}

		if (sunOrMoon != null
				&& this.world.getNrOfEntities(Star.STAR_TYPE) == 0) {
			Globals.levelDone = true;
			if (!allPickedUpSnd.playing())
				allPickedUpSnd.play();
		}
		previousx = x;
		previousy = y;

	}

	public void leftWorldBoundaries() {
		// the player unfortunately left the screen - so we retry
		Globals.playerDead = true;
	}

	public void animEnd() {
	}

}
