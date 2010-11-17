package it.randomtower.engine;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class AIControlledSpaceShip extends Entity {

	public static final String NAME = "player";

	private static final int HEIGHT = 28;
	private static final int WIDTH = 23;

	public static final String STAND_DOWN = "stand_down";
	public static final String STAND_UP = "stand_up";
	public static final String STAND_RIGHT = "stand_right";
	public static final String STAND_LEFT = "stand_left";

	public Vector2f mySpeed = new Vector2f(10, 10);

	public boolean attacking = false;

	public AIControlledSpaceShip(float x, float y, String ref) {
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
	}

	public void setupAnimations(String ref) {
		try {
			setGraphic(new Image(ref));
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
		if (collide(ME.SOLID, x + mySpeed.x * 2, y)) {
			if (!collide(ME.SOLID, x, y + mySpeed.y)) {
				moveDown();
			} else {
				moveUp();
			}
		}
	}

	public void moveLeft() {
		if (!collide(ME.SOLID, x - mySpeed.x, y)) {
			x -= mySpeed.x;
		}
	}

	public void moveRight() {
		if (!collide(ME.SOLID, x + mySpeed.x, y)) {
			x += mySpeed.x;
		}
	}

	public void moveDown() {
		if (!collide(ME.SOLID, x, y + mySpeed.y)
				&& y + mySpeed.y < ME.container.getHeight()) {
			y += mySpeed.y;
		}
	}

	public void moveUp() {
		if (!collide(ME.SOLID, x, y - mySpeed.y) && y + mySpeed.y > 0) {
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
		if (currentAnim.equalsIgnoreCase(AIControlledSpaceShip.STAND_RIGHT)) {
			return true;
		}
		return false;
	}

	public boolean isLeftStanding() {
		if (currentAnim.equalsIgnoreCase(AIControlledSpaceShip.STAND_LEFT)) {
			return true;
		}
		return false;
	}

	public boolean isUpStanding() {
		if (currentAnim.equalsIgnoreCase(AIControlledSpaceShip.STAND_UP)) {
			return true;
		}
		return false;
	}

	public boolean isDownStanding() {
		if (currentAnim.equalsIgnoreCase(AIControlledSpaceShip.STAND_DOWN)) {
			return true;
		}
		return false;
	}

}
