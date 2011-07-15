package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import it.marteEngine.entity.PlatformerEntity;
import it.marteEngine.tween.Ease;
import it.marteEngine.tween.LinearMotion;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class FuzzyBat extends Entity {

	public static final String BAT = "bat";

	private float moveSpeed = 3;

	protected boolean faceRight = true;

	private Vector2f distanceFromPlayer = null;

	private LinearMotion motion;

	private Bresenham myEyes = new Bresenham();

	private int sight = 32 * 4;

	public FuzzyBat(float x, float y) throws SlickException {
		super(x, y);
		addAnimation(ResourceManager.getSpriteSheet("batLeft"), "moveLeft",
				true, 0, 0, 1, 2);
		addAnimation(ResourceManager.getSpriteSheet("batRight"), "moveRight",
				true, 0, 0, 1, 2);
		addType(BAT, SOLID);
		setHitBox(0, 0, 32, 32);
		speed.x = moveSpeed;
		currentAnim = "moveRight";
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		// distanceFromPlayer= getDistanceFromPlayer();

		// Entity p = ME.world.find(Entity.PLAYER);
		// if (canSee(p,false,false)){
		// if (motion==null){
		// setMoveTo(ME.world.find(Entity.PLAYER));
		// } else {
		// if (!motion.isFinished()){
		// motion.update(delta);
		// this.x = motion.getX();
		// this.y = motion.getY();
		// updateAnimation(delta);
		// return ;
		// } else {
		// motion = null;
		// }
		// }
		// }
		//

		super.update(container, delta);

		if (faceRight && collide(SOLID, x + 1, y) != null) {
			faceRight = false;
			currentAnim = "moveLeft";
		} else if (!faceRight && collide(SOLID, x - 1, y) != null) {
			faceRight = true;
			currentAnim = "moveRight";
		}

		Entity player = collide(PLAYER, x, y - 1);
		if (player != null) {
			((PlatformerEntity) player).jump();
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
	}

	private Vector2f getDistanceFromPlayer() {
		Entity p = ME.world.find(Entity.PLAYER);
		if (p == null) {
			return null;
		}
		Vector2f distance = new Vector2f();
		distance.x = this.x - p.x;
		distance.y = this.y - p.y;
		return distance;
	}

	private void setMoveTo(Entity e) {
		if (Math.abs(getDistanceFromPlayer().x) > 32
				&& Math.abs(getDistanceFromPlayer().y) > 32) {
			motion = new LinearMotion(this.x, this.y, e.x, e.y, 100,
					Ease.QUAD_OUT);
		}
	}

	/**
	 * just use tilebased line of sight, using the blocked array of the level
	 * 
	 * @param target
	 * @param onlyHorizontal
	 *            - entity and target must be on the same y tile position
	 * @param onlyVertical
	 *            - entity and target must be on the same x tile position
	 * @return
	 */
	public boolean canSee(Entity target, boolean onlyHorizontal,
			boolean onlyVertical) {
		Vector2f myTile = this.getTile(this);
		Vector2f targetTile = this.getTile(target);
		if (onlyHorizontal) {
			if (myTile.y != targetTile.y)
				return false;
		}
		if (onlyVertical) {
			if (myTile.x != targetTile.x)
				return false;
		}
		// is enemy looking in the right direction
		if (this.faceRight && myTile.x > targetTile.x)
			return false;
		if (!this.faceRight && myTile.x < targetTile.x)
			return false;
		// now check with Bresenham if there is nothing solid blocking our view
		FuzzyGameWorld level = ((FuzzyGameWorld) world);
		int length = myEyes.plot((int) myTile.x, (int) myTile.y,
				(int) targetTile.x, (int) targetTile.y) * 32;
		if (length > sight)
			return false; // too far away!
		while (myEyes.next()) {
			int linex = myEyes.getX();
			int liney = myEyes.getY();
			if (level.blocked(linex, liney))
				return false;
		}
		return true;
	}

	protected Vector2f getTile(Entity entity) {
		int tilex, tiley;
		if (entity.centered) {
			tilex = (int) (entity.x / 32);
			tiley = (int) (entity.y / 32);
		} else {
			tilex = (int) ((entity.x + entity.width / 2) / 32);
			tiley = (int) ((entity.y + entity.height / 2) / 32);
		}
		return new Vector2f(tilex, tiley);
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
