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
			setAnimation(ME.WALK_UP);
			moveUp();
		} else if (check(ME.WALK_DOWN)) {
			setAnimation(ME.WALK_DOWN);
			moveDown();
		} else
			verticalMovement = false;

		if (check(ME.WALK_RIGHT)) {
			setAnimation(ME.WALK_RIGHT);
			moveRight();
		} else if (check(ME.WALK_LEFT)) {
			setAnimation(ME.WALK_LEFT);
			moveLeft();
		} else
			horizontalMovement = false;

		if (!horizontalMovement && !verticalMovement) {
			if (isCurrentAnim(ME.WALK_DOWN)) {
				setAnimation(STAND_DOWN);
			} else if (isCurrentAnim(ME.WALK_UP)) {
				setAnimation(STAND_UP);
			} else if (isCurrentAnim(ME.WALK_RIGHT)) {
				setAnimation(STAND_RIGHT);
			} else if (isCurrentAnim(ME.WALK_LEFT)) {
				setAnimation(STAND_LEFT);
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
		return isCurrentAnim(ME.WALK_RIGHT);
	}

	public boolean isLeftMoving() {
		return isCurrentAnim(ME.WALK_LEFT);
	}

	public boolean isUpMoving() {
		return isCurrentAnim(ME.WALK_UP);
	}

	public boolean isDownMoving() {
		return isCurrentAnim(ME.WALK_DOWN);
	}

	public boolean isRightStanding() {
		return isCurrentAnim(TopDownActor.STAND_RIGHT);
	}

	public boolean isLeftStanding() {
		return isCurrentAnim(TopDownActor.STAND_LEFT);
	}

	public boolean isUpStanding() {
		return isCurrentAnim(TopDownActor.STAND_UP);
	}

	public boolean isDownStanding() {
		return isCurrentAnim(TopDownActor.STAND_DOWN);
	}
}
