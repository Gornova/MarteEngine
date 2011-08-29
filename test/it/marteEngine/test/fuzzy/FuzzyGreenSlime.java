package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import it.marteEngine.entity.PhysicsEntity;
import it.marteEngine.entity.PlatformerEntity;
import it.marteEngine.tween.Tweener;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class FuzzyGreenSlime extends PhysicsEntity {

	public static final String SLIME = "slime";

	private float moveSpeed = 1;

	protected boolean faceRight = false;

	private boolean toRemove;

	/** To handle effects **/
	private Tweener tweener = FuzzyFactory.getFadeMoveTweener();

	private float ty;

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
		if (!toRemove) {
			super.update(container, delta);
			checkGround(true, false);
			ty = y;
			if (speed.x > 0)
				this.faceRight = true;
			else
				this.faceRight = false;

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

		} else {
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
