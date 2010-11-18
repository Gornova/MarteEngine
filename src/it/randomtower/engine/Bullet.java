package it.randomtower.engine;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Bullet extends Entity {

	private Vector2f direction;
	public float fireSpeed = 0.5f;
	public static final String NAME = "BULLET";

	public Bullet(float startx, float starty, String ref, Vector2f direction) {
		super(startx, starty);
		this.direction = direction;

		try {
			setGraphic(new Image(ref));
		} catch (SlickException e) {
			e.printStackTrace();
		}

		setHitBox(0, 0, currentImage.getWidth(), currentImage.getHeight());

		addType(NAME, ME.SOLID);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		x += fireSpeed * direction.x;
		y += fireSpeed * direction.y;
	}

	@Override
	public void collisionResponse(Entity entity) {
		if (entity.name.equalsIgnoreCase(StaticActor.NAME)) {
			ME.world.remove(this);
		}
	}

}
