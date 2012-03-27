package it.marteEngine.test.resize;

import it.marteEngine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Bullet extends Entity {

	public static final String NAME = "BULLET";

	public Bullet(float x, float y, String ref, int angle)
			throws SlickException {
		super(x, y);
		name = NAME;
		this.angle = angle;
		setGraphic(new Image(ref));

		addType(SOLID);
		setHitBox(0, 0, 8, 8);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		float angle = getAngle();
		Vector2f vectorSpeed = calculateVector(angle, 8);
		x += vectorSpeed.x;
		y += vectorSpeed.y;

		collide(SOLID, x, y);

		super.update(container, delta);
	}

}
