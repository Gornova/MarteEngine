package it.marteEngine.test.fuzzy;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class Spike extends Entity {

	public static final String SPIKE = "spike";

	public Spike(float x, float y) {
		super(x, y, ResourceManager.getImage("spike"));
		setHitBox(0, 0, width, height);

		addType(SPIKE);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		super.update(container, delta);
	}

}
