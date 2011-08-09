package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import it.marteEngine.tween.Ease;
import it.marteEngine.tween.NumTween;
import it.marteEngine.tween.Tween.TweenerMode;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Star extends Entity {

	public static final String STAR = "star";
	private Sound pickupstar;

	private NumTween fadeTween = new NumTween(1, 0, 60, TweenerMode.ONESHOT,
			Ease.QUAD_OUT, false);
	private NumTween moveUpTween = new NumTween(0, 2, 10, TweenerMode.ONESHOT,
			Ease.QUAD_IN, false);

	private boolean toRemove = false;
	private float ty;

	public Star(float x, float y) {
		super(x, y, ResourceManager.getImage("star"));
		setHitBox(0, 0, currentImage.getWidth(), currentImage.getHeight());

		addType(STAR);

		pickupstar = ResourceManager.getSound("pickupstar");
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		if (!toRemove && collide(PLAYER, x, y) != null) {
			if (!pickupstar.playing()) {
				pickupstar.play();
			}
			toRemove = true;
			ty = y;
			FuzzyGameWorld.stars -= 1;
			FuzzyGameWorld.points += 100;
		}

		if (toRemove) {
			fadeTween.update(delta);
			moveUpTween.update(delta);
		}
		if (getAlpha() == 0f) {
			ME.world.remove(this);
		}
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		super.render(container, g);
		if (toRemove) {
			// if to remove, apply effects
			setAlpha(fadeTween.getValue());

			ty -= moveUpTween.getValue();
			FuzzyMain.font.drawString(x, ty, "100");
		}
	}

}
