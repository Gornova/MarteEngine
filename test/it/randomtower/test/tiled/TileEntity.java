package it.randomtower.test.tiled;

import org.newdawn.slick.Image;

import it.randomtower.engine.entity.Entity;

/**
 * Simple entity 
 * @author Gornova
 */
public class TileEntity extends Entity {

	public TileEntity(float x, float y, Image image) {
		super(x, y, image);
		width = image.getWidth();
		height = image.getHeight();
	}

}
