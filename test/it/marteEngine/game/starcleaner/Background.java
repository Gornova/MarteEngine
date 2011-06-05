package it.marteEngine.game.starcleaner;

import it.marteEngine.ResourceManager;
import it.marteEngine.entity.Entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Background extends Entity {

	public Background(float x, float y) {
		super(x, y);
		name = "background";
		depth = -100;
		currentImage = ResourceManager.getImage("background");
	}
	
	public Background(float x, float y, String refImage) throws SlickException {
		super(x, y);
		name = "background";
		depth = -100;
		currentImage = new Image(refImage);
	}
	
}
