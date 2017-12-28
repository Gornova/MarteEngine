package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.World;
import it.marteEngine.entity.Entity;
import it.marteEngine.entity.PlatformerEntity;
import it.marteEngine.tween.Tweener;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.util.Log;

public class FuzzyArrowTrap extends Entity {

	private static final int SHOOT_TIME = 1300;

	public static final String ARROW_TRAP = "arrowTrap";

	private static final int SIGHT_SIZE = 10;

	protected boolean faceRight = false;

	private float sx;

	private float sy;

	private Line sight;

	private int shootTimer;

	private static Sound fireSnd;

	private boolean toRemove;

	/** To handle effects **/
	private Tweener tweener = FuzzyFactory.getFadeMoveTweener();

	private float ty;

	public FuzzyArrowTrap(float x, float y) throws SlickException {
		super(x, y);
		setGraphic(ResourceManager.getImage("arrowTrap"));
		addType(ARROW_TRAP, SOLID);
		setHitBox(0, 0, 32, 32);
		shootTimer = SHOOT_TIME; // Fire on sight

		sx = x;
		sy = y + height / 2;

		if (faceRight) {
			sight = new Line(sx, sy, sx + 32 * SIGHT_SIZE, sy);
		} else {
			sight = new Line(sx, sy, sx - 32 * SIGHT_SIZE, sy);
		}

		fireSnd = ResourceManager.getSound("fireArrow");
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {

		super.update(container, delta);

		if (!toRemove) {
			super.update(container, delta);
			ty = y;
			Entity player = collide(PLAYER, x, y - 1);
			if (player != null) {
				toRemove = true;
				((PlatformerEntity) player).jump();
				FuzzyGameWorld.addPoints(100);
			}
			// shooting time!
			shoot(delta);

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

		if (ME.debugEnabled) {
			g.setColor(Color.red);
			g.draw(sight);
			g.setColor(Color.black);
		}

		if (toRemove) {
			// if to remove, apply effects
			setAlpha(tweener.getTween(FuzzyFactory.FADE).getValue());
			ty -= tweener.getTween(FuzzyFactory.MOVE_UP).getValue();
			FuzzyMain.font.drawString(x, ty, "100");
		}
	}

	private void shoot(int delta) throws SlickException {
		shootTimer += delta;
		if (shootTimer > SHOOT_TIME && playerInSight()) {
			shootTimer = 0;
			fireArrow();
		}
	}

	private boolean playerInSight() {
		for (Entity entity : ME.world.intersect(sight)) {
			if (entity.isType(FuzzyPlayer.PLAYER)) {
				return true;
			}
		}
		return false;
	}

	private void fireArrow() throws SlickException {
		Log.info("Firing arrow");
		ME.world.add(new FuzzyArrow(sx - 20, sy - 5, faceRight), World.GAME);
		if (!fireSnd.playing()) {
			fireSnd.play();
		}
	}
}
