package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import it.marteEngine.tween.Tweener;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Heart extends Entity {

	private static final String HEART = "heart";
	private boolean toRemove;
	private Sound heartSnd;
	private float ty;

	/** To handle effects **/
	private Tweener tweener = FuzzyFactory.getFadeMoveTweener();

	public Heart(float x, float y) {
		super(x, y);

		setGraphic(ResourceManager.getImage("heart"));
		setHitBox(0, 0, currentImage.getWidth(), currentImage.getHeight());
		addType(HEART);

		heartSnd = ResourceManager.getSound("heart");
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		if (!toRemove && collide(PLAYER, x, y) != null) {
			if (!heartSnd.playing()) {
				heartSnd.play();
			}
			toRemove = true;
			ty = y;
			FuzzyGameWorld.addPoints(100);
			if (FuzzyPlayer.life + 1 <= 3) {
				FuzzyPlayer.life++;
			}
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
			FuzzyMain.font.drawString(x, ty, "1 UP");
		}
	}

}
