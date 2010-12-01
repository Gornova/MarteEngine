package it.randomtower.engine.entity;

public class Solid extends Entity {

	public Solid(float x, float y, int width, int height) {
		super(x, y);
		addType(SOLID);
		setHitBox(0, 0, width, height);
	}

}
