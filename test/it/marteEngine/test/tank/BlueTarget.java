package it.marteEngine.test.tank;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;

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
