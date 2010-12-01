package com.rightanglegames.starcleaner;

import it.randomtower.engine.RessourceManager;
import it.randomtower.engine.entity.Solid;

public class Block extends Solid {

	public Block(float x, float y, int w, int h) {
		super(x, y, w, h);
		name = "block";
		depth = 5;
		currentImage = RessourceManager.getImage("block");
	}

}
