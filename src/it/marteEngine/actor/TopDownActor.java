package it.marteEngine.actor;

import it.marteEngine.ME;
import it.marteEngine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class TopDownActor extends Entity {

	public static final String NAME = "player";

	private static final int HEIGHT = 28;
	private static final int WIDTH = 23;

	public static final String STAND_DOWN = "stand_down";
	public static final String STAND_UP = "stand_up";
	public static final String STAND_RIGHT = "stand_right";
	public static final String STAND_LEFT = "stand_left";

	public Vector2f mySpeed = new Vector2f(2, 2);

	public boolean attacking = false;

	public TopDownActor(float x, float y, String ref) {
		super(x, y);

		// set id
		name = NAME;

		// load spriteSheet
		if (ref != null)
			setupAnimations(ref);

		// player rendered above everything
		depth = ME.Z_LEVEL_TOP;

		// define labels for the key
		defineControls();

		// define collision box and type
		setHitBox(0, 0, WIDTH, HEIGHT);
		addType(NAME);
	}

	private void defineControls() {
		define(ME.WALK_UP, Input.KEY_UP, Input.KEY_W);
		define(ME.WALK_DOWN, Input.KEY_DOWN, Input.KEY_S);
		define(ME.WALK_LEFT, Input.KEY_LEFT, Input.KEY_A);
		define(ME.WALK_RIGHT, Input.KEY_RIGHT, Input.KEY_D);
	}

	public void setupAnimations(String ref) {
		try {
			setGraphic(new SpriteSheet(ref, WIDTH, HEIGHT));
			duration = 150;
			addAnimation(STAND_DOWN, false, 0, 0);
			addAnimation(ME.WALK_DOWN, true, 0, 0, 1, 2, 3, 4, 5, 6, 7);
			addAnimation(ME.WALK_UP, true, 1, 0, 1, 2, 3, 4, 5, 6, 7);
			addAnimation(ME.WALK_RIGHT, true, 2, 0, 1, 2, 3, 4, 5);
			addAnimation(ME.WALK_LEFT, true, 3, 0, 1, 2, 3, 4, 5);
			addAnimation(STAND_UP, false, 1, 0);
			addAnimation(STAND_RIGHT, false, 2, 0);
			addAnimation(STAND_LEFT, false, 3, 0);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		// movements
		updateMovements();
	}

	private void updateMovements() {
		boolean horizontalMovement = true;
		boolean verticalMovement = true;

		if (check(ME.WALK_UP)) {
			currentAnim = ME.WALK_UP;

			moveUp();
		} else if (check(ME.WALK_DOWN)) {
			currentAnim = ME.WALK_DOWN;

			moveDown();
		} else
			verticalMovement = false;

		if (check(ME.WALK_RIGHT)) {
			currentAnim = ME.WALK_RIGHT;

			moveRight();
		} else if (check(ME.WALK_LEFT)) {
			currentAnim = ME.WALK_LEFT;

			moveLeft();
		} else
			horizontalMovement = false;

		if (!horizontalMovement && !verticalMovement) {
			if (currentAnim.equalsIgnoreCase(ME.WALK_DOWN)) {
				currentAnim = STAND_DOWN;
			} else if (currentAnim.equalsIgnoreCase(ME.WALK_UP)) {
				currentAnim = STAND_UP;
			} else if (currentAnim.equalsIgnoreCase(ME.WALK_RIGHT)) {
				currentAnim = STAND_RIGHT;
			} else if (currentAnim.equalsIgnoreCase(ME.WALK_LEFT)) {
				currentAnim = STAND_LEFT;
			}
		}
	}

	public void moveLeft() {
		if (collide(SOLID, x - mySpeed.x, y) == null) {
			x -= mySpeed.x;
		}
	}

	public void moveRight() {
		if (collide(SOLID, x + mySpeed.x, y) == null) {
			x += mySpeed.x;
		}
	}

	public void moveDown() {
		if (collide(SOLID, x, y + mySpeed.y) == null) {
			y += mySpeed.y;
		}
	}

	public void moveUp() {
		if (collide(SOLID, x, y - mySpeed.y) == null) {
			y -= mySpeed.y;
		}
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		super.render(container, g);
	}

	public boolean isRightMoving() {
		if (currentAnim.equalsIgnoreCase(ME.WALK_RIGHT)) {
			return true;
		}
		return false;
	}

	public boolean isLeftMoving() {
		if (currentAnim.equalsIgnoreCase(ME.WALK_LEFT)) {
			return true;
		}
		return false;
	}

	public boolean isUpMoving() {
		if (currentAnim.equalsIgnoreCase(ME.WALK_UP)) {
			return true;
		}
		return false;
	}

	public boolean isDownMoving() {
		if (currentAnim.equalsIgnoreCase(ME.WALK_DOWN)) {
			return true;
		}
		return false;
	}

	public boolean isRightStanding() {
		if (currentAnim.equalsIgnoreCase(TopDownActor.STAND_RIGHT)) {
			return true;
		}
		return false;
	}

	public boolean isLeftStanding() {
		if (currentAnim.equalsIgnoreCase(TopDownActor.STAND_LEFT)) {
			return true;
		}
		return false;
	}

	public boolean isUpStanding() {
		if (currentAnim.equalsIgnoreCase(TopDownActor.STAND_UP)) {
			return true;
		}
		return false;
	}

	public boolean isDownStanding() {
		if (currentAnim.equalsIgnoreCase(TopDownActor.STAND_DOWN)) {
			return true;
		}
		return false;
	}

}
