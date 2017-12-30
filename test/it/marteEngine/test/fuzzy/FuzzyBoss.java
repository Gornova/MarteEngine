package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.entity.Entity;
import it.marteEngine.resource.ResourceManager;
import it.marteEngine.tween.Ease;
import it.marteEngine.tween.LinearMotion;
import it.marteEngine.tween.Tweener;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class FuzzyBoss extends Entity {

	public static String FUZZY_BOSS = "fuzzyBoss";
	private static final String MOVE_FIRE_ALARM = "moveAlarm";

	private List<Vector2f> path = new ArrayList<>();

	private LinearMotion motion;

	private int index = 0;

	public int life = 3;

	/** To handle effects **/
	private Tweener tweener = FuzzyFactory.getFadeMoveTweener(40);
	private boolean toRemove;
	private float ty;

	public FuzzyBoss(float x, float y) {
		super(x, y);
		setGraphic(ResourceManager.getImage("boss1"));

		addType(SOLID, FUZZY_BOSS);
		setHitBox(0, 0, width, height);

		loadPath();

		addAlarm(MOVE_FIRE_ALARM, 60, false, true);
	}

	/**
	 * Load path from resource file
	 */
	private void loadPath() {
		int num = ResourceManager.getInt("pathNum");
		for (int i = 0; i < num; i++) {
			path.add(new Vector2f(ResourceManager.getFloat("x" + i),
					ResourceManager.getFloat("y" + i)));
		}
		x = path.get(0).x;
		y = path.get(0).y;
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);

		if (!toRemove) {
			if (motion != null) {
				motion.update(delta);
				setPosition(motion.getPosition());
			}
			ty = y;
			Entity player = collide(PLAYER, x, y);
			if (player != null) {
				((FuzzyPlayer) player).damage(30);
			}

			if (ME.world.getNrOfEntities(TargetBlock.TARGET_BLOCK) == 0) {
				toRemove = true;
			}
		}

		if (toRemove) {
			tweener.update(delta);
		}
		if (getAlpha() == 0f) {
			ME.world.remove(this);
			List<Entity> ent = ME.world
					.findEntityWithType(FuzzyDestroyableBlock.TAPPO);
			if (ent != null && !ent.isEmpty()) {
				ME.world.remove(ent.get(0));
			}
		}
	}

	@Override
	public void alarmTriggered(String name) {
		if (name.equalsIgnoreCase(MOVE_FIRE_ALARM)) {
			if (index + 1 < path.size()) {
				index++;
				motion = new LinearMotion(x, y, path.get(index).x,
						path.get(index).y, 30, Ease.QUAD_IN);

				Vector2f pos = ((FuzzyGameWorld) ME.world).getPlayerCenter();
				pos.y += 32;
				ME.world.add(new FuzzyBubble(x, y, pos));

			} else {
				index = -1;
			}
		}
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		super.render(container, g);
		if (toRemove) {
			// if to remove, apply effects
			setAlpha(tweener.getTween(FuzzyFactory.FADE).getValue());
			ty += tweener.getTween(FuzzyFactory.MOVE_UP).getValue();
			FuzzyMain.font.drawString(x, ty, "1000");
		}
	}
}
