package it.marteEngine.game.starcleaner;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Cloud extends Entity {

	public static final String CLOUD = "cloud";

	private float speed = 0.05f;

	private Vector2f slowPlayerSpeed = null;
	private Vector2f normalPlayerSpeed = null;

	public Cloud(float x, float y, boolean moveRight) {
		super(x, y);
		name = CLOUD;
		this.addType(CLOUD);
		this.wrapHorizontal = true;

		depth = 10 + (int) (Math.random() * 10);
		setGraphic(ResourceManager.getImage("cloud"));
		setHitBox(0, 0, width, height);
	}

	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);
		x += (speed * delta);

		Entity player = collide(Entity.PLAYER, x, y);
		if (player != null) {
			Angel angel = (Angel) player;
			if (normalPlayerSpeed == null) {
				// store the player speed and give him half the amount to slow
				// him down
				normalPlayerSpeed = new Vector2f(angel.maxSpeed);
				slowPlayerSpeed = new Vector2f(angel.maxSpeed.x * 0.5f,
						angel.maxSpeed.y * 2.0f);
				angel.maxSpeed = slowPlayerSpeed;
				angel.speed.x = 0;
			}
		} else {
			// no more collision so restore player friction
			if (normalPlayerSpeed != null) {
				Angel angel = (Angel) this.world.find(Angel.ANGEL);
				if (angel != null) {
					angel.maxSpeed = new Vector2f(
							Globals.originalPlayerMaxSpeed);
					normalPlayerSpeed = null;
					slowPlayerSpeed = null;
				}
			}
		}
	}
}
