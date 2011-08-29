package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import it.marteEngine.tween.Tweener;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Star extends Entity {

	public static final String STAR = "star";
	private Sound pickupstar;

	/** To handle effects **/
	private Tweener tweener = FuzzyFactory.getFadeMoveTweener();

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
			FuzzyGameWorld.addPoints(100);
			FuzzyGameWorld.stars -= 1;
		}

		if (toRemove) {
			tweener.update(delta);
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
			setAlpha(tweener.getTween(FuzzyFactory.FADE).getValue());
			ty -= tweener.getTween(FuzzyFactory.MOVE_UP).getValue();
			FuzzyMain.font.drawString(x, ty, "100");
		}
	}

}
