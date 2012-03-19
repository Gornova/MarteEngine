package it.marteEngine.test.tank;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * Simple tank controlled using keyboard and mouse. A turret can be put on top
 * of this tank, {@link TankTurret}
 * 
 * @author Gornova
 */
public class Tank extends Entity {
	private static final int MOVE_SPEED = 2;
	private static final int ROTATE_SPEED = 2;
	private static final String FORWARD = "forward";
	private static final String BACKWARD = "backward";
	private static final String ROTATE_RIGHT = "rotate right";
	private static final String ROTATE_LEFT = "rotate left";

	// main turret of this tank
	public TankTurret turret;

	public Tank(float x, float y) {
		super(x, y);
		name = "Tank";

		setGraphic(ResourceManager.getSpriteSheet("tank").getSubImage(1, 0));
		setCentered(true);

		bindToKey(FORWARD, Input.KEY_W, Input.KEY_UP);
		bindToKey(BACKWARD, Input.KEY_S, Input.KEY_DOWN);
		bindToKey(ROTATE_LEFT, Input.KEY_A, Input.KEY_LEFT);
		bindToKey(ROTATE_RIGHT, Input.KEY_D, Input.KEY_RIGHT);

		// If the tank goes out of the world bounds make sure it
		// reappears on the opposite side.
		wrapHorizontal = true;
		wrapVertical = true;
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		// check player commands
		if (check(FORWARD)) {
			move(true);
		} else if (check(BACKWARD)) {
			move(false);
		}
		if (check(ROTATE_LEFT)) {
			angle -= ROTATE_SPEED;
		} else if (check(ROTATE_RIGHT)) {
			angle += ROTATE_SPEED;
		}

		// update entity logic
		super.update(container, delta);
		// update turret logic
		if (turret != null) {
			turret.update(container, delta);
		}
	}

	/**
	 * Update tank position forward or backward
	 */
	private void move(boolean forward) {
		Vector2f speed = calculateVector(angle, MOVE_SPEED * (forward ? 1 : -1));
		x += speed.x;
		y += speed.y;
	}

}
