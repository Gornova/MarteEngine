package com.rightanglegames.starcleaner;

import it.randomtower.engine.RessourceManager;
import it.randomtower.engine.entity.Entity;

public class Background extends Entity {

	public Background(float x, float y) {
		super(x, y);
		name = "background";
		depth = 0;
		currentImage = RessourceManager.getImage("background");
	}

}
