package it.marteEngine.test.tank;

import it.marteEngine.ME;
import it.marteEngine.entity.Entity;
import it.marteEngine.resource.ResourceManager;

/**
 * Simple destroyable target
 * 
 * @author Gornova
 */
public class RedTarget extends Entity {

	public RedTarget(float x, float y) {
		super(x, y);
		setGraphic(ResourceManager.getImage("blockRed"));

		setHitBox(0, 0, 32, 32);
		addType(SOLID);
	}

	@Override
	public void collisionResponse(Entity other) {
		ME.world.remove(this);
	}

}
