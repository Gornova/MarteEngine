package it.randomtower.engine.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Solid extends Entity {

	public Solid(float x, float y, int width, int height) {
		super(x, y);
		addType(SOLID);
		setHitBox(0, 0, width, height);
	}

	public Solid(float x, float y, int width, int height, String refImage) throws SlickException {
		super(x, y);
		addType(SOLID);
		setHitBox(0, 0, width, height);
		currentImage = new Image(refImage);
	}
	
	
}
