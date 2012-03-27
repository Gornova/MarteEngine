package it.marteEngine.game.starcleaner;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;

import org.newdawn.slick.SlickException;

public class Block extends Entity {

	public Block(float x, float y, int w, int h) throws SlickException {
		super(x, y);
		name = "block";
		depth = 5;
		setGraphic(ResourceManager.getImage("block"));
		addType(SOLID);
		setHitBox(0, 0, width, height);
		width = w;
		height = h;
	}

}
