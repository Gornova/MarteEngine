package it.marteEngine.game.starcleaner;

import it.marteEngine.ResourceManager;

import org.newdawn.slick.SlickException;

public class Block extends Solid {

	public Block(float x, float y, int w, int h) throws SlickException {
		super(x, y, w, h);
		name = "block";
		depth = 5;
		currentImage = ResourceManager.getImage("block");
	}

}
