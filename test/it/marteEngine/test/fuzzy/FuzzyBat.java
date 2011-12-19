package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import it.marteEngine.entity.PlatformerEntity;
import it.marteEngine.tween.Tweener;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class FuzzyBat extends Entity {

	public static final String BAT = "bat";

	private float moveSpeed = 3;

	protected boolean faceRight = true;

	private boolean toRemove;

	/** To handle effects **/
	private Tweener tweener = FuzzyFactory.getFadeMoveTweener();

	private float ty;

	public FuzzyBat(float x, float y) throws SlickException {
		super(x, y);
		addAnimation(ResourceManager.getSpriteSheet("batLeft"), "moveLeft",
				true, 0, 0, 1, 2);
		addAnimation(ResourceManager.getSpriteSheet("batRight"), "moveRight",
				true, 0, 0, 1, 2);
		addType(BAT, SOLID);
		setHitBox(0, 0, 32, 32);
		speed.x = moveSpeed;
		setAnimation("moveRight");
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {

		if (!toRemove) {
			super.update(container, delta);

			if (faceRight && collide(SOLID, x + 1, y) != null) {
				faceRight = false;
				setAnimation("moveLeft");
			} else if (!faceRight && collide(SOLID, x - 1, y) != null) {
				faceRight = true;
				setAnimation("moveRight");
			}
			ty = y;
			Entity player = collide(PLAYER, x, y - 1);
			if (player != null) {
				toRemove = true;
				((PlatformerEntity) player).jump();
				FuzzyGameWorld.addPoints(100);
			}
			player = collide(PLAYER, x + 1, y);
			damagePlayer(player);
			player = collide(PLAYER, x - 1, y);
			damagePlayer(player);

			if (faceRight) {
				speed.x = moveSpeed;
			} else {
				speed.x = moveSpeed * -1;
			}
		} else {
			tweener.update(delta);
		}

		if (getAlpha() == 0f) {
			if (!FuzzyGameWorld.killSound.playing()) {
				FuzzyGameWorld.killSound.play();
			}
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

	private boolean damagePlayer(Entity player) {
		if (player != null) {
			FuzzyPlayer pl = (FuzzyPlayer) ME.world.find(PLAYER);
			pl.damage(-1);
			// change direction
			if (faceRight) {
				this.x -= 5;
				faceRight = false;
				this.speed.x = -moveSpeed;
			} else {
				this.x += 5;
				faceRight = true;
				this.speed.x = +moveSpeed;
			}
			return true;
		}
		return false;
	}

}
