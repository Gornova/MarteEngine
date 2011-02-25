package it.marteEngine.game.starcleaner;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;

public class Background extends Entity {

	public Background(float x, float y) {
		super(x, y);
		name = "background";
		depth = 0;
		currentImage = ResourceManager.getImage("background");
	}
	
	public Background(float x, float y, String refImage) throws SlickException {
		super(x, y);
		name = "background";
		depth = 0;
		currentImage = new Image(refImage);
	}
	
}
