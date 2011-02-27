package it.randomtower.test.tank;

import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.entity.Entity;

/**
 * Invulnerable target
 * 
 * @author Gornova
 */
public class BlueTarget extends Entity {

	public BlueTarget(float x, float y) {
		super(x, y);
		setGraphic(ResourceManager.getImage("blockBlue"));
		
		setHitBox(0, 0, 32, 32);
		addType(SOLID);
	}
	
	@Override
	public void collisionResponse(Entity other) {
		// blue target are invulnerable!
	}
	
}
