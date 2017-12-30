package it.marteEngine.game.starcleaner;

import it.marteEngine.entity.Entity;
import it.marteEngine.resource.ResourceManager;

public class Block extends Entity {

	public Block(float x, float y) {
		super(x, y);
		name = "block";
		depth = 5;
		setGraphic(ResourceManager.getImage("block"));
		addType(SOLID);
		setHitBox(0, 0, width, height);
	}

}
