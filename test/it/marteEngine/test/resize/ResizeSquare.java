package it.marteEngine.test.resize;

import it.marteEngine.ME;
import it.marteEngine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class ResizeSquare extends Entity {

	public static final String NAME = "resize";
	private static final String ADD = "add";
	private static final String MINUS = "minus";

	private int value = 8;

	public Vector2f mySpeed = new Vector2f(2, 2);
	public float rotation;
	public static final Vector2f SLOWEST = new Vector2f(2, 2);
	public static final Vector2f SLOW = new Vector2f(4, 4);
	public static final Vector2f NORMAL = new Vector2f(6, 6);
	public static final Vector2f FAST = new Vector2f(8, 8);
	public static final Vector2f FASTEST = new Vector2f(10, 10);
	private static final String ATTACK1 = "ATTACK1";

	public ResizeSquare(float x, float y, int width, int height, String ref) {
		super(x, y);

		// set id
		name = NAME;

		// set image
		setupImage();

		// define collision box and type
		setHitBox(0, 0, width, height);
		addType(NAME, SOLID);

		// define labels for the key
		defineControls();

	}

	public void setupImage() {
		try {
			setGraphic(new Image("data/" + value + ".png"));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private void defineControls() {
		define(ME.WALK_UP, Input.KEY_UP, Input.KEY_W);
		define(ME.WALK_DOWN, Input.KEY_DOWN, Input.KEY_S);
		define(ME.WALK_LEFT, Input.KEY_LEFT, Input.KEY_A);
		define(ME.WALK_RIGHT, Input.KEY_RIGHT, Input.KEY_D);
		define(ADD, Input.KEY_Z);
		define(MINUS, Input.KEY_X);
		define(ATTACK1, Input.MOUSE_LEFT_BUTTON);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {

		if (check(ME.WALK_UP)) {
			moveUp();
		} else if (check(ME.WALK_DOWN)) {
			moveDown();
		}

		if (check(ME.WALK_RIGHT)) {
			moveRight();
		} else if (check(ME.WALK_LEFT)) {
			moveLeft();
		}

		if (pressed(ADD)) {
			sizeUp();
		}
		if (pressed(MINUS)) {
			sizeDown();
		}
		//
		width = value;
		height = value;
		setHitBox(0, 0, width, height);
		// updateSpeed();
		//
		setGraphic(new Image("data/" + value + ".png"));
		//
		// updateRotation();

		// calculate heading of turret
		// Input input = container.getInput();
		// float mx = input.getMouseX();
		// float my = input.getMouseY();
		// TODO Going to add the offset here! HACK
		// mx -= 20;
		// my -= 15;
		// angle = (int) calculateAngle(x, y, mx, my);

		// add new Missile when player fire
		// if (check(ATTACK1)) {
		// Bullet b = new Bullet(x, y, "data/bullet.png", angle);
		// b.setCentered(true);
		// ME.world.add(b);
		// }

		super.update(container, delta);

	}

	// private void updateSpeed() {
	// if (value == 8) {
	// mySpeed.set(FASTEST);
	// } else if (value == 16) {
	// mySpeed.set(FAST);
	// } else if (value == 32) {
	// mySpeed.set(NORMAL);
	// } else if (value == 64) {
	// mySpeed.set(SLOW);
	// } else if (value == 128) {
	// mySpeed.set(SLOWEST);
	// }
	// }

	public void sizeDown() {
		if (value / 2 >= 8) {
			value /= 2;
		}
	}

	public void sizeUp() {
		if (value * 2 <= 128) {
			value *= 2;
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

	// private void updateRotation() {
	// Input input = world.container.getInput();
	// int mx = input.getMouseX();
	// int my = input.getMouseY();
	// Vector2f position = new Vector2f(x, y);
	// rotation = (float) Math.toDegrees(Math.atan2(position.x - mx + value
	// / 2, position.y - my + value / 2)
	// * -1) + 180;
	// Log.info("rotation " + rotation);
	// }

}
