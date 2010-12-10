package com.rightanglegames.starcleaner;

import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.entity.Entity;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class Star extends Entity {

	public static final String STAR_TYPE = "Star";
	
	private Light myLight = null;
	
	public Star(float x, float y, Light light) {
		super(x, y);
		addType(STAR_TYPE);
		depth = 10;
		currentImage = ResourceManager.getImage("star");
		myLight = light;
		setHitBox(4,4,32,32);
	}
	
	public void collisionResponse(Entity other) {
		if (other.isType(Entity.PLAYER)) {
			Globals.lightMap.removeLight(myLight);
			this.destroy();
		}
	}
	
	public void update(GameContainer container, int delta) throws SlickException {
		// stars are invisible during the day
		if (Globals.lightMap.isDay())
			this.visible = false;
		else
			this.visible = true;
	}

}
