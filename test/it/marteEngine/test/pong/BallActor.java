package it.marteEngine.test.pong;

import it.marteEngine.ME;
import it.marteEngine.actor.StaticActor;
import it.marteEngine.entity.Entity;
import it.marteEngine.resource.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class BallActor extends Entity {

	public static final String NAME = "ball";

	public static final String STAND_DOWN = "stand_down";
	public static final String STAND_UP = "stand_up";

	public Vector2f mySpeed = new Vector2f(5, 5);

	public boolean attacking = false;

	public BallActor(float x, float y) {
		super(x, y);

		// set id
		this.name = NAME;

		setGraphic(ResourceManager.getImage("ball"));

		// player rendered above everything
		depth = ME.Z_LEVEL_TOP;

		// define collision box and type
		setHitBox(0, 0, width, height);
		addType(NAME, SOLID);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		// movements
		updateMovements();

		// check ball ends
		if (x < 0) {
			int score = (Integer) ME.attributes.get("score2");
			ME.attributes.put("score2", ++score);
			ME.remove(this);
		} else if (x > this.world.container.getWidth()) {
			int score = (Integer) ME.attributes.get("score1");
			ME.attributes.put("score1", ++score);
			ME.remove(this);
		}
	}

	private void updateMovements() {
		if (collide(SOLID, x + mySpeed.x, y) == null) {
			x += mySpeed.x;
		}
		if (collide(SOLID, x, y + mySpeed.y) == null) {
			y += mySpeed.y;
		}
	}

	@Override
	public void collisionResponse(Entity entity) {
		if (entity.name.equalsIgnoreCase("player2")
				|| entity.name.equalsIgnoreCase("player1")) {
			mySpeed.x = -1 * mySpeed.x;
		}
		if (entity.name.equalsIgnoreCase(StaticActor.NAME)) {
			mySpeed.y = -1 * mySpeed.y;
		}
	}

}
