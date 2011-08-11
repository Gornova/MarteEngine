package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.World;
import it.marteEngine.entity.Entity;
import it.marteEngine.tween.Tween;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class FuzzyBlock extends Entity {

	public static String FUZZY_BLOCK = "fuzzyBlock";

	private boolean fade;

	private Tween fadeTween = FuzzyFactory.getFadeTween();

	public FuzzyBlock(float x, float y, Image image) {
		super(x, y, image);
		setHitBox(0, 0, 32, 32);
		addType(SOLID, FUZZY_BLOCK);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		if (!fade) {
			Entity player = collide(PLAYER, x, y + height + 1);
			if (player != null && player instanceof FuzzyPlayer) {
				((FuzzyPlayer) player).y = y + height + 1;
				fade = true;
				if (Math.random() > 0.5) {
					ME.world.add(new Star(x, y), World.GAME);
					FuzzyGameWorld.stars++;
					FuzzyGameWorld.total++;
				} else {
					ME.world.add(new Heart(x, y), World.GAME);
				}
			}
		} else {
			fadeTween.update(delta);
			setAlpha(fadeTween.getValue());
			if (getAlpha() == 0) {
				ME.world.remove(this);

			}
		}

	}

}
