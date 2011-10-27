package it.marteEngine.test.fuzzy;

import it.marteEngine.entity.Entity;

import org.newdawn.slick.Image;

public class TargetBlock extends Entity {

	public static String TARGET_BLOCK = "targetBlock";

	public TargetBlock(float x, float y, Image image) {
		super(x, y, image);

		setHitBox(0, 0, image.getWidth(), image.getHeight());
		addType(SOLID, TARGET_BLOCK);
	}

}
