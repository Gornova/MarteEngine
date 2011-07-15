package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import it.marteEngine.entity.PhysicsEntity;
import it.marteEngine.entity.PlatformerEntity;
import it.marteEngine.tween.Ease;
import it.marteEngine.tween.NumTween;
import it.marteEngine.tween.Tween.TweenerMode;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class FuzzyGreenSlime extends PhysicsEntity {

	public static final String SLIME = "slime";

	private float moveSpeed = 1;

	protected boolean faceRight = false;

	private NumTween fadeTween = new NumTween(1, 0, 10, TweenerMode.ONESHOT,
			Ease.CUBE_OUT, false);

	private boolean fade;

	public FuzzyGreenSlime(float x, float y) throws SlickException {
		super(x, y);
		addAnimation(ResourceManager.getSpriteSheet("slime"), "move", true, 0,
				0, 1, 2, 3);
		addType(SLIME);
		setHitBox(0, 0, 40, 20);
		// make Slime sloow
		maxSpeed.x = 1;
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		if (!fade) {
			super.update(container, delta);
			checkGround(true, false);

			if (speed.x > 0)
				this.faceRight = true;
			else
				this.faceRight = false;

			Entity player = collide(PLAYER, x, y - 1);
			if (player != null) {
				fade = true;
				((PlatformerEntity) player).jump();
			}
			player = collide(PLAYER, x + 1, y);
			damagePlayer(player);
			player = collide(PLAYER, x - 1, y);
			damagePlayer(player);

		} else {
			fadeTween.update(delta);
			setAlpha(fadeTween.getValue());
			if (getAlpha() == 0) {
				ME.world.remove(this);
			}
		}
	}

	/**
	 * Check if falling
	 * 
	 * @param revertHorizontal
	 * @param revertVertical
	 */
	public void checkGround(boolean revertHorizontal, boolean revertVertical) {
		boolean blocked = ((FuzzyGameWorld) world).blocked(this.x
				+ this.speed.x + ((faceRight) ? this.width : 0), this.y
				+ this.height + 1);
		if (!blocked) {
			if (revertHorizontal && speed.x != 0)
				speed.x = -speed.x;
			if (revertVertical && speed.y != 0)
				speed.y = -speed.y;
		}
	}

	@Override
	public void collisionResponse(Entity other) {
		// try to move in old direction
		if (faceRight && this.speed.x == 0) {
			this.speed.x = -moveSpeed;
			faceRight = false;
		} else if (!faceRight && this.speed.x == 0) {
			this.speed.x = moveSpeed;
			faceRight = true;
		}
	}

	private boolean damagePlayer(Entity player) {
		if (player != null) {
			FuzzyPlayer pl = (FuzzyPlayer) ME.world.find(PLAYER);
			pl.damage();
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
