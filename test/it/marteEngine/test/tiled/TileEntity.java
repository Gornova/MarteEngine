package it.marteEngine.test.tiled;

import it.marteEngine.entity.Entity;

import org.newdawn.slick.Image;

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
