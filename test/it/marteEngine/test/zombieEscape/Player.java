package it.marteEngine.test.zombieEscape;

import it.marteEngine.ME;
import it.marteEngine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Player extends Entity {

	public static final String PLAYER = "player";

	private Vector2f mySpeed = new Vector2f(3, 3);

	public Player(float x, float y, Image image) {
		super(x, y, image);

		setHitBox(0, 0, image.getWidth(), image.getHeight());

		addType(PLAYER);
		name = PLAYER;
		defineControls();
	}

	private void defineControls() {
		define(ME.WALK_UP, Input.KEY_UP, Input.KEY_W);
		define(ME.WALK_DOWN, Input.KEY_DOWN, Input.KEY_S);
		define(ME.WALK_LEFT, Input.KEY_LEFT, Input.KEY_A);
		define(ME.WALK_RIGHT, Input.KEY_RIGHT, Input.KEY_D);
	}

	private void updateMovements() {

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
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		updateMovements();
	}

}
