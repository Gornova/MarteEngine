package it.marteEngine.test.fuzzy;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import it.marteEngine.entity.PhysicsEntity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class FuzzyGreenSlime extends PhysicsEntity {

	public static final String SLIME = "slime";
	// private Sound jumpSnd;

	private float moveSpeed = 1;

	protected boolean faceRight = false;

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
		super.update(container, delta);
		
		checkGround(true, false);
		
		if (speed.x > 0)
			this.faceRight = true;
		else
			this.faceRight = false;		
	}

	/**
	 * Check if falling
	 * @param revertHorizontal
	 * @param revertVertical
	 */
	public void checkGround(boolean revertHorizontal, boolean revertVertical) {
		boolean blocked = ((FuzzyGameWorld) world).blocked(this.x
				+ this.speed.x + ((faceRight) ? this.width : 0), this.y
				+ this.height + 1);
		if (!blocked) {
//			x = previousx;
//			y = previousy;
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

}
