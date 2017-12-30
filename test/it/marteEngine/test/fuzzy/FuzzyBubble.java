package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.entity.Entity;
import it.marteEngine.resource.ResourceManager;
import it.marteEngine.tween.Ease;
import it.marteEngine.tween.LinearMotion;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class FuzzyBubble extends Entity {

	public static final String BUBBLE = "bubble";
	private LinearMotion motion;

	public FuzzyBubble(float x, float y, Vector2f to) {
		super(x, y);

		setGraphic(ResourceManager.getImage("player"));

		setHitBox(0, 0, 32, 32);
		addType(BUBBLE);

		motion = new LinearMotion(x, y, to.x, to.y, 50, Ease.NONE);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		motion.update(delta);

		Entity ent = collide(TargetBlock.TARGET_BLOCK, x, y);
		if (ent != null) {
			ME.world.remove(this);
			ME.world.remove(ent);
			return;
		}

		Entity player = collide(PLAYER, x, y);
		if (player != null) {
			((FuzzyPlayer) player).damage(30);
		}

		if (motion != null) {
			setPosition(motion.getPosition());
			if (motion.isFinished()) {
				ME.world.remove(this);
			}
		}
	}
}
