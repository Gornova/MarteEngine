package it.randomtower.engine.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Solid extends Entity {

	public Solid(float x, float y, int width, int height) throws SlickException {
		this(x, y, width, height, -1, "");
	}

	public Solid(float x, float y, int width, int height, int depth)
			throws SlickException {
		this(x, y, width, height, depth, "");
	}

	public Solid(float x, float y, int width, int height, int depth,
			String refImage) throws SlickException {
		super(x, y);
		addType(SOLID);
		setHitBox(0, 0, width, height);
		this.depth = depth;
		if (refImage != null)
			currentImage = new Image(refImage);
	}
	
	public Solid(float x, float y, int width, int height, int depth,
			Image image) throws SlickException {
		super(x, y);
		addType(SOLID);
		setHitBox(0, 0, width, height);
		this.depth = depth;
		currentImage = image;
	}	

}
