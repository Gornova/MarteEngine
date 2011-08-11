package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.entity.Entity;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class FuzzyBoss extends Entity {

	public static String FUZZY_BOSS = "fuzzyBoss";
	private boolean done = true;

	public FuzzyBoss(float x, float y) {
		super(x, y);
		setGraphic(ResourceManager.getImage("boss1"));

		addType(SOLID, FUZZY_BOSS);
		setHitBox(0, 0, currentImage.getWidth(), currentImage.getHeight());

	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);
		if (done) {
			done = false;

			ME.world.add(new FuzzyBubble(x - 50, y, getPlayerDirection()),
					World.GAME);
		}
	}

	private Vector2f getPlayerDirection() {
		List<Entity> entities = ME.world.findEntityWithType(PLAYER);
		if (entities != null && !entities.isEmpty()) {
			Entity e = entities.get(0);
			if (e != null && e instanceof FuzzyPlayer) {
				float dx = x - e.x;
				float dy = y - e.y;
				Vector2f result = new Vector2f(dx, dy);
				return result.normalise();
			}
		}
		return null;
	}

}
