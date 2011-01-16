package it.randomtower.test.tank;

import it.randomtower.engine.ME;
import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.entity.Entity;

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
