package it.randomtower.test.pong;

import it.randomtower.engine.ME;
import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class PongBarActor extends Entity {

	public static final String STAND_DOWN = "stand_down";
	public static final String STAND_UP = "stand_up";

	public Vector2f mySpeed = new Vector2f(6, 6);

	public boolean attacking = false;

	public PongBarActor(float x, float y, String name, int up,
			int down) {
		super(x, y);

		// set id
		this.name = name;

		// load spriteSheet
		setGraphic(ResourceManager.getImage("bar"));

		// player rendered above everything
		depth = ME.Z_LEVEL_TOP;

		// define labels for the key
		defineControls(up, down);

		// define collision box and type
		setHitBox(0, 0, currentImage.getWidth(), currentImage.getHeight());
		addType(name, Entity.SOLID);
	}

	private void defineControls(int up, int down) {
		define(ME.WALK_UP, up);
		define(ME.WALK_DOWN, down);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		// movements
		updateMovements();
	}

	private void updateMovements() {

		if (check(ME.WALK_UP)) {
			moveUp();
		} else if (check(ME.WALK_DOWN)) {
			moveDown();
		}

	}

	public void moveDown() {
		if (collide(Entity.SOLID, x, y + mySpeed.y) == null
				&& y + mySpeed.y + height < 600) {
			y += mySpeed.y;
		}
	}

	public void moveUp() {
		if (collide(Entity.SOLID, x, y - mySpeed.y)== null && y - mySpeed.y > 0) {
			y -= mySpeed.y;
		}
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		super.render(container, g);
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

}
