package it.marteEngine.test.fuzzy;

import it.marteEngine.ME;
import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class FuzzyArrow extends Entity {

	private static final String ARROW = "arrow";

	protected boolean faceRight;

	public FuzzyArrow(float x, float y, boolean faceRight) {
		super(x, y);
		this.faceRight = faceRight;
		if (faceRight) {
			setGraphic(ResourceManager.getImage("arrow"));
		} else {
			setGraphic(ResourceManager.getImage("arrow").getFlippedCopy(true,
					false));
		}
		addType(ARROW, SOLID);
		setHitBox(0, 0, 10, 10);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {

		super.update(container, delta);

		float tx;
		if (faceRight) {
			tx = x + 5;
		} else {
			tx = x - 5;
		}

		Entity ent = collide(new String[] { PLAYER, SOLID }, tx, y);
		if (ent != null) {
			if (ent instanceof FuzzyPlayer) {
				FuzzyPlayer fp = (FuzzyPlayer) ent;
				fp.damage(-1);
			}
			ME.world.remove(this);
			return;
		}
		x = tx;

	}
}
