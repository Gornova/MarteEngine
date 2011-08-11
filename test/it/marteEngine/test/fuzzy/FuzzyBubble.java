package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class FuzzyBubble extends Entity {

	private static final String BUBBLE = "bubble";

	public FuzzyBubble(float x, float y, Vector2f dir) {
		super(x, y);

		// setGraphic(ResourceManager.getSpriteSheet("bubbleSheet"));
		// addAnimation(ResourceManager.getSpriteSheet("bubbleSheet"),
		// "bubbleSheet", true, 0, 0, 1, 2, 3);
		setGraphic(ResourceManager.getSpriteSheet("bubbleSheet")
				.getSprite(0, 0));
		setHitBox(30, 33, 27, 27);
		speed = new Vector2f(3, 3);
		speed.x *= dir.x;
		speed.y *= dir.y;
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);
		Entity ent = collide(SOLID, x, y);
		if (ent != null && !ent.isType(FuzzyBoss.FUZZY_BOSS)) {
			ME.world.remove(this);
		}
	}

}
