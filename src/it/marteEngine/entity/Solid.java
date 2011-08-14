package it.marteEngine.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Solid extends Entity {

	public Solid(float x, float y, int width, int height) throws SlickException {
		super(x, y);
		addType(SOLID);
		setHitBox(0, 0, width, height);
		name = "solid";
	}

	public Solid(float x, float y, int width, int height, int depth)
			throws SlickException {
		this(x, y, width, height, depth, "");
	}

	public Solid(float x, float y, int width, int height, int depth,
			String refImage) throws SlickException {
		this(x, y, width, height);
		this.depth = depth;
		if (refImage != null)
			currentImage = new Image(refImage);
		name = "solid";
	}

	public Solid(float x, float y, int width, int height, int depth, Image image)
			throws SlickException {
		super(x, y);
		addType(SOLID);
		setHitBox(0, 0, width, height);
		this.depth = depth;
		currentImage = image;
		name = "solid";
	}

}
